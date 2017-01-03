package com.comcast.video.dawg.controller.pound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.comcast.video.dawg.common.security.AuthServerConfig;
import com.comcast.video.dawg.common.security.jwt.DawgJwtEncoder;
import com.comcast.video.dawg.common.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private DawgPoundConfiguration config;
    
    @Autowired
    private DawgJwtEncoder jwtEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

	    JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter(jwtEncoder, authenticationManager());
		http
		    .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/admin/**").hasAnyRole("ADMIN")
			.antMatchers("/**").hasAnyRole("ADMIN", "POUND");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		AuthServerConfig authCfg = config.getAuthConfig();
		auth
		    .eraseCredentials(false)
			.ldapAuthentication()
			.contextSource().url("ldap://" + authCfg.getLdapHost() + ":" + authCfg.getLdapPort())
			.and()
				.userDnPatterns("uid={0},ou=people," + authCfg.getLdapDomain())
				.groupSearchBase("ou=group," + authCfg.getLdapDomain())
				.groupSearchFilter("member={0}");
		
	}
}