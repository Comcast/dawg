package com.comcast.video.dawg.common.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * Filter that decides if security is enabled. If it is enabled then
 * direct to the next filter as the security filter. If not enabled, go to next filter in the chain.
 * @author Kevin Pearson
 *
 */
public class SecuritySwitchFilter implements Filter {
    private DelegatingFilterProxy securityFilter;
    private boolean enabled;
    
    public SecuritySwitchFilter(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
        if (enabled) {
            securityFilter.doFilter(request, response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }
    
    /**
     * Initialize the wrapped security filter here
     * @param filterConfig the configuration of filters
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        securityFilter = new DelegatingFilterProxy("springSecurityFilterChain");
        securityFilter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");
        securityFilter.init(filterConfig);
    }
    
    /**
     * Just delegate to destroying the wrapped security filter
     */
    @Override
    public void destroy() {
        if (securityFilter != null) {
            securityFilter.destroy();
            securityFilter = null;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public DelegatingFilterProxy getSecurityFilter() {
        return securityFilter;
    }
}
