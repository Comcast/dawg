package com.comcast.video.dawg.common.security.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.DefaultAttribute;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.DefaultModification;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapNoSuchObjectException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

import com.comcast.video.dawg.common.exceptions.UserException;
import com.comcast.video.dawg.common.security.AuthServerConfig;
import com.comcast.video.dawg.common.security.model.DawgUser;
import com.comcast.video.dawg.common.security.model.DawgUserAndRoles;

public class LdapUserService implements UserService {

    private AuthServerConfig config;

    private LdapConnection connection;

    public LdapUserService(AuthServerConfig config) {
        this.config = config;
    }

    void checkConnection() throws LdapException {
        if ((this.connection == null) || !connection.isConnected()) {
            connection = new LdapNetworkConnection( config.getLdapHost(), 389 );
            this.connection.bind("cn=" + config.getLdapBindCn() + "," + config.getLdapDomain(), config.getLdapBindPassword());
        }
    }

    private static Map<String, String> toInetOrgPerson(DawgUser user) {
        Map<String, String> person = new HashMap<String, String>();
        putIfNotNull(person, "cn", cn(user));
        putIfNotNull(person, "sn", user.getLastName());
        putIfNotNull(person, "givenName", user.getFirstName());
        putIfNotNull(person, "userPassword", sha(user.getPassword()));
        putIfNotNull(person, "mail", user.getMail());
        return person;
    }

    private static String cn(DawgUser user) {
        if((user.getFirstName() != null) && (user.getLastName() != null)) {
            return user.getFirstName() + " " + user.getLastName();
        }
        return user.getUid();
    }

    private static void putIfNotNull(Map<String, String> map, String key, String val) {
        if (val != null) map.put(key, val);
    }

    private static Object[] toLdapElements(DawgUser user) {
        Map<String, String> person = toInetOrgPerson(user);

        person.put("objectClass", "inetOrgPerson");
        person.put("ou", "people");
        Object[] eles = new Object[person.keySet().size()];
        int i = 0;
        for (String key : person.keySet()) {
            eles[i++] = key + ": " + person.get(key);
        }
        return eles;
    }

    private String userDn(String uid) {
        return "uid=" + uid + ",ou=people," + config.getLdapDomain();
    }

    @Override
    public void addUser(DawgUser user, Set<String> roles) throws UserException {
        if (roles == null) throw new UserException("Missing roles field");
        if (user.getUid() == null) throw new UserException("Missing uid field");
        if (user.getPassword() == null) throw new UserException("Missing password field");
        try {
            checkConnection();
            String userdn = userDn(user.getUid());
            connection.add(new DefaultEntry(userdn, toLdapElements(user)));
            changeRoles(user.getUid(), roles, (Set<String>) null);
        } catch (LdapException e) {
            throw new UserException(e);
        }
    }

    public DawgUserAndRoles getUser(String userId) throws UserException {
        try {
            checkConnection();
            Entry entry = connection.lookup(userDn(userId));
            if (entry == null) return null;

            Set<String> roles = getUserRoles(userId);
            return new DawgUserAndRoles(userId, 
                    entry.get("givenName").getString(), 
                    entry.get("sn").getString(), 
                    entry.get("mail").getString(), 
                    null, roles);
        } catch (LdapException e) {
            throw new UserException(e);
        }
    }

    @Override
    public void modifyUser(DawgUser modifications) throws UserException {
        boolean fn = modifications.getFirstName() != null;
        boolean ln = modifications.getLastName() != null;
        /** cn is both fn and ln, so if we only allow changing one part of the name
            then we have to do a get on the db to get the other part, lets just
            not allow it */
        if (fn ^ ln) throw new UserException("Can only change full name");
        try {

            checkConnection();

            String userdn = userDn(modifications.getUid());
            Map<String, String> personMods = toInetOrgPerson(modifications);
            if (!fn && !ln) {
                /** Don't change cn if we didn't change first name and last name */
                personMods.remove("cn");
            }
            Set<Modification> mods = new HashSet<Modification>();
            for (String m : personMods.keySet()) {
                Attribute attr = new DefaultAttribute(m, personMods.get(m));
                mods.add(new DefaultModification(ModificationOperation.REPLACE_ATTRIBUTE, attr));
            }
            connection.modify(userdn, mods.toArray(new Modification[mods.size()]));
        } catch (LdapException e) {
            throw new UserException(e);
        }
    }

    @Override
    public void deleteUser(String userId) throws UserException {
        try {
            checkConnection();
            connection.delete(userDn(userId));
            changeRoles(userId, null, getUserRoles(userId));

        } catch (LdapException e) {
            throw new UserException(e);
        }
    }

    @Override
    public void changeRoles(String userId, Set<String> addRoles, Set<String> removeRoles) throws UserException {
        changeRoles(userId, addRoles, ModificationOperation.ADD_ATTRIBUTE);
        changeRoles(userId, removeRoles, ModificationOperation.REMOVE_ATTRIBUTE);
    }

    private void changeRoles(String userId, Set<String> roles, ModificationOperation op) throws UserException {
        try {
            checkConnection();
            if ((roles != null) && !roles.isEmpty()) {
                String userdn = userDn(userId);
                for (String role : roles) {
                    String dn = "cn=" + role + ",ou=group," + config.getLdapDomain();
                    try {
                        connection.modify(dn, new DefaultModification(op, new DefaultAttribute("member", userdn)));
                    }  catch (LdapNoSuchObjectException e) {
                        throw new UserException("Role '" + role + "' does not exist.", e);
                    }  
                }
            }
        } catch (LdapException e) {
            throw new UserException(e);
        }
    }

    private static String sha(final String password) {
        if (password == null) return null;
        if (password.startsWith("{SHA}")) return password;
        String base64;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(password.getBytes());
            base64 = new String(Base64.encodeBase64(digest.digest(), false));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return "{SHA}" + base64;
    }

    @Override
    public Set<String> getUserRoles(String userId) throws UserException {
        Set<String> roles = new HashSet<String>();
        try {
            EntryCursor cursor = connection.search("ou=group," + config.getLdapDomain(), "(member=" + userDn(userId) + ")", 
                    SearchScope.SUBTREE);

            while(cursor.next()) {
                Entry entry = cursor.get();
                roles.add(entry.get("cn").getString());
            }
        } catch (LdapException | CursorException e) {
            throw new UserException(e);
        }
        return roles;
    }

}
