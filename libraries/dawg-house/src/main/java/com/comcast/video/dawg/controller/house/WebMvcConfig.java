package com.comcast.video.dawg.controller.house;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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

import com.comcast.cereal.engines.JsonCerealEngine;
import com.comcast.video.dawg.common.ServerUtils;
import com.comcast.video.dawg.common.security.SecuritySwitchFilter;
import com.comcast.video.dawg.common.security.jwt.DawgJwtEncoder;
import com.comcast.video.dawg.filter.DawgCorsFilter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="com.comcast.video.dawg")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public ContentNegotiatingViewResolver contentViewResolver() throws Exception {
        ContentNegotiationManagerFactoryBean contentNegotiationManager = new ContentNegotiationManagerFactoryBean();
        contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        ContentNegotiatingViewResolver contentViewResolver = new ContentNegotiatingViewResolver();
        contentViewResolver.setContentNegotiationManager(contentNegotiationManager.getObject());
        contentViewResolver.setViewResolvers(Arrays.<ViewResolver>asList(viewResolver));
        return contentViewResolver;
    }
    
    @Bean
    public JsonCerealEngine jsonCerealEngine() {
        return new JsonCerealEngine();
    }
    
    @Bean
    public ServerUtils serverUtils() {
        return new ServerUtils();
    }
    
    @Bean
    @Autowired
    public DawgJwtEncoder jwtEncoder(DawgHouseConfiguration config) {
        return new DawgJwtEncoder(config.getAuthConfig().getJwtSecret(), "dawg-house", TimeUnit.HOURS.toMillis(1));
    }
    
    @Bean
    @Autowired
    public SecuritySwitchFilter securitySwitchFilter(DawgHouseConfiguration config) {
        return new SecuritySwitchFilter(!"none".equals(config.getAuthConfig().getMode()));
    }
    
    @Bean
    @Autowired
    public DawgCorsFilter dawgCorsFilter(DawgHouseConfiguration config) {
        return new DawgCorsFilter(config.getAuthConfig().getCorsDomains());
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
