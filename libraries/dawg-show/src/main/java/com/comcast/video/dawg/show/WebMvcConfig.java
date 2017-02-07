package com.comcast.video.dawg.show;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.comcast.video.dawg.common.security.SecuritySwitchFilter;
import com.comcast.video.dawg.common.security.jwt.DawgJwtEncoder;
import com.comcast.video.dawg.common.security.jwt.JwtDeviceAccessValidator;
import com.comcast.video.dawg.common.security.jwt.JwtSecurityProvider;
import com.comcast.video.dawg.house.DawgPoundClient;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="com.comcast.video.dawg")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ContentNegotiatingViewResolver contentViewResolver() throws Exception {
        ContentNegotiationManagerFactoryBean contentNegotiationManager = new ContentNegotiationManagerFactoryBean();
        contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/views/");
        viewResolver.setSuffix(".jsp");

        ContentNegotiatingViewResolver contentViewResolver = new ContentNegotiatingViewResolver();
        contentViewResolver.setContentNegotiationManager(contentNegotiationManager.getObject());
        contentViewResolver.setViewResolvers(Arrays.<ViewResolver>asList(viewResolver));
        return contentViewResolver;
    }
    
    @Bean
    @Autowired
    public DawgJwtEncoder jwtEncoder(DawgShowConfiguration config) {
        return new DawgJwtEncoder(config.getAuthConfig().getJwtSecret(), "dawg-show", config.getAuthConfig().getJwtTtl());
    }
    
    @Bean
    @Autowired
    public SecuritySwitchFilter securitySwitchFilter(DawgShowConfiguration config) {
        return new SecuritySwitchFilter(!"none".equals(config.getAuthConfig().getMode()));
    }
    
    @Bean
    @Autowired
    public JwtDeviceAccessValidator accessValidator(DawgJwtEncoder jwtEncoder, DawgShowConfiguration config) {
        boolean enabled = !"none".equals(config.getAuthConfig().getMode());
        DawgPoundClient deviceProvider = new DawgPoundClient(config.getDawgPoundUrl());
        JwtSecurityProvider jwtSecurityProvider = new JwtSecurityProvider(config.getDawgHouseUser(), config.getDawgHousePassword(), jwtEncoder);
        deviceProvider.getClient().setSecurityProvider(jwtSecurityProvider);
        return new JwtDeviceAccessValidator(jwtEncoder, deviceProvider, enabled);
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
