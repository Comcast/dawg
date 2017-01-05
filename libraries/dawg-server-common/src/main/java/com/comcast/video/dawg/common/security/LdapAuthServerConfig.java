package com.comcast.video.dawg.common.security;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Contains all configurable values for setting up ldap authentication on a dawg server
 * @author Kevin Pearson
 *
 */
public class LdapAuthServerConfig extends AuthServerConfig {
    private URI ldapUrl;
    private String domain;
    private String bindCn = "Manager";
    private String bindPassword;
    private String userDnPatterns = null;
    private String groupSearchBase = null;
    private String groupFilter = "member={0}";
    private String usersOrganizationalUnit = "people";
    
    public LdapAuthServerConfig() {
        try {
            ldapUrl = new URI("ldap://localhost:389");
        } catch (URISyntaxException e) { }
    }
    
    public String getLdapHost() {
        return ldapUrl.getHost();
    }
    
    public int getLdapPort() {
        return ldapUrl.getPort();
    }
    
    public boolean isLdapSsl() {
        return "ldaps".equals(ldapUrl.getScheme());
    }
    
    public String getLdapUrl() {
        return ldapUrl.toString();
    }

    public void setLdapUrl(String ldapUrl) throws URISyntaxException {
        this.ldapUrl = new URI(ldapUrl); 
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getBindCn() {
        return bindCn;
    }

    public void setBindCn(String bindCn) {
        this.bindCn = bindCn;
    }

    public String getBindPassword() {
        return bindPassword;
    }

    public void setBindPassword(String bindPassword) {
        this.bindPassword = bindPassword;
    }

    public String getUserDnPatterns() {
        return userDnPatterns != null ? userDnPatterns : "uid={0},ou=" + getUsersOrganizationalUnit() + "," + getDomain(); // default
    }

    public void setUserDnPatterns(String userDnPatterns) {
        this.userDnPatterns = userDnPatterns;
    }

    public String getGroupSearchBase() {
        return groupSearchBase != null ? groupSearchBase : "ou=group," + getDomain(); // default
    }

    public void setGroupSearchBase(String groupSearchBase) {
        this.groupSearchBase = groupSearchBase;
    }

    public String getGroupFilter() {
        return groupFilter;
    }

    public void setGroupFilter(String groupFilter) {
        this.groupFilter = groupFilter;
    }
    
    public String getBindDn() {
        return "cn=" + getBindCn() + "," + getDomain();
    }

    public String getUsersOrganizationalUnit() {
        return usersOrganizationalUnit;
    }

    public void setUsersOrganizationalUnit(String usersOrganizationalUnit) {
        this.usersOrganizationalUnit = usersOrganizationalUnit;
    }
}
