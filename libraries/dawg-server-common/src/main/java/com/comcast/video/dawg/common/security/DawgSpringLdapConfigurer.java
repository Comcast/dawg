package com.comcast.video.dawg.common.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DawgSpringLdapConfigurer {
    
    public static void configureGlobal(AuthenticationManagerBuilder auth, LdapAuthServerConfig authCfg, PasswordEncoder passwordEncoder) throws Exception {
        if (authCfg != null) {
            auth
                .ldapAuthentication()
                .passwordEncoder(passwordEncoder)
                .contextSource()
                    .url(authCfg.getLdapUrl())
                    .managerDn(authCfg.getBindDn())
                    .managerPassword(authCfg.getBindPassword())
            .and()
                .userDnPatterns(authCfg.getUserDnPatterns())
                .groupSearchBase(authCfg.getGroupSearchBase())
                .groupSearchFilter(authCfg.getGroupFilter())
            .and()
                .eraseCredentials(false);
        }
    }
}
