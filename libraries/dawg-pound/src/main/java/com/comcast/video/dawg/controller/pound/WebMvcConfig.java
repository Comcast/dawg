package com.comcast.video.dawg.controller.pound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.comcast.video.dawg.common.ServerUtils;
import com.comcast.video.dawg.common.security.LdapAuthServerConfig;
import com.comcast.video.dawg.common.security.SecuritySwitchFilter;
import com.comcast.video.dawg.common.security.jwt.DawgJwtEncoder;
import com.comcast.video.dawg.common.security.service.LdapUserService;
import com.comcast.video.dawg.common.security.service.UserService;
import com.comcast.video.dawg.filter.DawgCorsFilter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="com.comcast.video.dawg")
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    
    @Bean
    public ServerUtils serverUtils() {
        return new ServerUtils();
    }
    
    @Bean
    @Autowired
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    @Autowired
    public UserService userService(DawgPoundConfiguration config, PasswordEncoder passwordEncoder) {
        LdapAuthServerConfig cfg = config.getLdapAuthConfig();
        return cfg == null ? null : new LdapUserService(cfg, passwordEncoder);
    }
    
    @Bean
    @Autowired
    public SecuritySwitchFilter securitySwitchFilter(DawgPoundConfiguration config) {
        return new SecuritySwitchFilter(!"none".equals(config.getAuthConfig().getMode()));
    }
    
    @Bean
    @Autowired
    public DawgJwtEncoder jwtEncoder(DawgPoundConfiguration config) {
        return new DawgJwtEncoder(config.getAuthConfig().getJwtSecret(), "dawg-pound", config.getAuthConfig().getJwtTtl());
    }
    
    @Bean
    @Autowired
    public DawgCorsFilter dawgCorsFilter(DawgPoundConfiguration config) {
        return new DawgCorsFilter(config.getAuthConfig().getCorsDomains());
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
