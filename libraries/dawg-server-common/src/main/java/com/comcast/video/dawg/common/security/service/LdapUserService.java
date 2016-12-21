package com.comcast.video.dawg.common.security.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.directory.api.ldap.model.entry.DefaultAttribute;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.DefaultModification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

import com.comcast.video.dawg.common.exceptions.UserException;
import com.comcast.video.dawg.common.security.AuthServerConfig;
import com.comcast.video.dawg.common.security.model.DawgUser;

public class LdapUserService implements UserService {
	
	private AuthServerConfig config;

	private LdapConnection connection;
	
	public LdapUserService(AuthServerConfig config) {
		
	}

	void checkConnection() throws LdapException {
		if ((this.connection == null) || !connection.isConnected()) {
			connection = new LdapNetworkConnection( config.getLdapHost(), 389 );
			this.connection.bind("cn=" + config.getLdapBindCn() + "," + config.getLdapDomain(), config.getLdapBindPassword());
		}
	}

	@Override
	public void addUser(DawgUser user, Set<String> roles) throws UserException {
		try {
			checkConnection();
			String userdn = "uid=" + user.getUid() + ",ou=people," + config.getLdapDomain();
			connection.add( 
					new DefaultEntry( 
							userdn,
							"ObjectClass: inetOrgPerson",
							"cn: " + user.getFirstName() + " " + user.getLastName(),
							"sn: " + user.getLastName(),
							"givenName: " + user.getFirstName(),
							"userPassword: " + sha(user.getPassword()),
							"mail: " + user.getMail(),
							"ou: people"
							) );
			for (String role : roles) {
				String dn = "cn=" + role + ",ou=group," + config.getLdapDomain();
				connection.modify(dn, new DefaultModification(ModificationOperation.ADD_ATTRIBUTE, new DefaultAttribute("member", userdn)));
			}
		} catch (LdapException e) {
			throw new UserException(e);
		}
	}

	private static String sha(final String password) {
		String base64;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update(password.getBytes());
			base64 = Base64.encodeBase64String(digest.digest());
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return "{SHA}" + base64;
	}

}
