package com.comcast.video.dawg.common.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.pool.validation.DefaultDirContextValidator;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DawgSpringLdapConfigurer {
    public static final String CONNECT_TIMEOUT_ENV = "com.sun.jndi.ldap.connect.timeout";
    public static final String READ_TIMEOUT_ENV = "com.sun.jndi.ldap.read.timeout";
    
    public static void configureGlobal(AuthenticationManagerBuilder auth, LdapAuthServerConfig authCfg, PasswordEncoder passwordEncoder) throws Exception {
        if (authCfg != null) {
            LdapContextSource ldapCtx = new LdapContextSource();
            ldapCtx.setUrl(authCfg.getLdapUrl());
            ldapCtx.setUserDn(authCfg.getBindDn());
            ldapCtx.setPassword(authCfg.getBindPassword());
            Map<String, Object> baseEnv = new HashMap<String, Object>();
            baseEnv.put(CONNECT_TIMEOUT_ENV, "" + authCfg.getConnectTimeout());
            baseEnv.put(READ_TIMEOUT_ENV, "" + authCfg.getReadTimeout());
            ldapCtx.setBaseEnvironmentProperties(baseEnv);
            ldapCtx.afterPropertiesSet();
            
            /** Have connections to LDAP be pooled */
            LdapPoolingContextSourceAdapter pooling = new LdapPoolingContextSourceAdapter();
            pooling.setDirContextValidator(new DefaultDirContextValidator());
            pooling.setContextSource(ldapCtx);
            pooling.setTestOnBorrow(true);
            pooling.setTestWhileIdle(true);
            
            auth
                .ldapAuthentication()
                .passwordEncoder(passwordEncoder)
                .contextSource(pooling)
                .userDnPatterns(authCfg.getUserDnPatterns())
                .groupSearchBase(authCfg.getGroupSearchBase())
                .groupSearchFilter(authCfg.getGroupFilter())
            .and()
                .eraseCredentials(false);
        }
    }
}
