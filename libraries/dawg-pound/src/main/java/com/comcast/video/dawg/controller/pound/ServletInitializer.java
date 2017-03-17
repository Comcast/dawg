package com.comcast.video.dawg.controller.pound;

import javax.servlet.Filter;

import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Initializes the servlet. This is a replacement for a web.xml
 * @author Kevin Pearson
 *
 */
public class ServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected Filter[] getServletFilters() {
        DelegatingFilterProxy filter = new DelegatingFilterProxy("securitySwitchFilter");
        filter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");
        filter.setTargetFilterLifecycle(true);
        return new Filter[] { filter, new DelegatingFilterProxy("dawgCorsFilter"), new DelegatingFilterProxy("logFilter") };
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebMvcConfig.class };
    }

}
