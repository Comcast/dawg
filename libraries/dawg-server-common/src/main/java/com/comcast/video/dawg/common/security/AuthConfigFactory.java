package com.comcast.video.dawg.common.security;

import java.net.URISyntaxException;
import java.util.Set;

import org.ini4j.OptionMap;

import com.google.common.collect.Sets;

/**
 * Generates the different types of auth configs based on the auth type.
 * @author Kevin Pearson
 *
 */
public class AuthConfigFactory {
    
    public static final String LDAP_URL = "ldap.url";
    public static final String LDAP_DOMAIN = "ldap.domain";
    public static final String LDAP_BIND_CN = "ldap.binddn";
    public static final String LDAP_BIND_PASSWORD = "ldap.bindpass";
    public static final String LDAP_USERDN_PATTERNS = "ldap.userdn.patterns";
    public static final String LDAP_GROUP_SEARCH_BASE = "ldap.group.searchbase";
    public static final String LDAP_GROUP_FILTER = "ldap.group.filter";
    public static final String LDAP_USERS_OU = "ldap.users.ou";
    public static final String AUTH_MODE = "mode";
    public static final String JWT_SECRET = "jwtSecret";
    public static final String CORS_DOMAINS = "corsDomains";

    public static final String MODE_NONE = "none";
    public static final String MODE_LDAP = "ldap";
    
    /**
     * Uses 'mode' field to determine which type of config to return.
     * Supported modes are ldap or none. none being default.
     * @param map The map containing all the config values
     * @return
     */
    public AuthServerConfig createConfig(OptionMap map) {
        String mode = (String) map.get(AUTH_MODE);
        AuthServerConfig cfg;
        if (MODE_LDAP.equals(mode)) {
            cfg = new LdapAuthServerConfig();
            setBaseConfig(cfg, map);
            setLdapConfig((LdapAuthServerConfig) cfg, map);
        } else {
            cfg = new AuthServerConfig();
            setBaseConfig(cfg, map);
            cfg.setMode(MODE_NONE);
        }
        return cfg;
    }
    
    private void setBaseConfig(AuthServerConfig cfg, OptionMap map) {
        cfg.setJwtSecret((String) map.get(JWT_SECRET));
        Set<String> domains = map.containsKey(CORS_DOMAINS) ? Sets.newHashSet(map.get(CORS_DOMAINS).split(",")) : null;
        cfg.setCorsDomains(domains);
    }
    
    private void setLdapConfig(LdapAuthServerConfig cfg, OptionMap map) {
        cfg.setMode(MODE_LDAP);
        cfg.setBindCn(map.get(LDAP_BIND_CN, cfg.getBindCn()));
        cfg.setBindPassword(map.get(LDAP_BIND_PASSWORD));
        cfg.setDomain(map.get(LDAP_DOMAIN));
        try {
            cfg.setLdapUrl(map.get(LDAP_URL, cfg.getLdapUrl()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        cfg.setUserDnPatterns(map.get(LDAP_USERDN_PATTERNS));
        cfg.setGroupSearchBase(map.get(LDAP_GROUP_SEARCH_BASE));
        cfg.setGroupFilter(map.get(LDAP_GROUP_FILTER, cfg.getGroupFilter()));
        cfg.setUsersOrganizationalUnit(map.get(LDAP_USERS_OU, cfg.getUsersOrganizationalUnit()));
    }
}
