package com.comcast.video.dawg.filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * A filter wrapper that causes a filter to only execute if the path does not match 
 * a given regular expression
 * @author Kevin Pearson
 *
 */
public class ExclusionFilter implements Filter {
    private String pathExclusionRegex;
    private Filter delegate;
    
    public ExclusionFilter(Filter delegate, String pathExclusionRegex) {
        this.delegate = delegate;
        this.pathExclusionRegex = pathExclusionRegex;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.delegate.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length());
        if ((path != null) && Pattern.matches(pathExclusionRegex, path)) {
            chain.doFilter(request, response);
        } else {
            this.delegate.doFilter(request, response, chain);
        }
    }

    @Override
    public void destroy() {
        this.delegate.destroy();
    }
}
