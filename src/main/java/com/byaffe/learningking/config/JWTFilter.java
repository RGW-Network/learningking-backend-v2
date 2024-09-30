package com.byaffe.learningking.config;

import com.byaffe.learningking.shared.security.TokenProvider;
import com.byaffe.learningking.shared.security.UserDetailsContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * '
 * The http request filter to authenticate and permit all incoming  http requests.
 */
@Component
public class JWTFilter extends GenericFilterBean {

    @Autowired
    TokenProvider tokenProvider;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            System.out.println("Endpoint hit on >>>" + httpServletRequest.getRequestURI());
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET,PUT, OPTIONS, DELETE");
            httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
            if ("OPTIONS".equals(((HttpServletRequest) servletRequest).getMethod())) {
                httpServletResponse.setStatus(200);
                return;
            }
            if (FilterUtils.allowedAuth(httpServletRequest.getRequestURI())) {
                String authorisationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
                processAuthDetails(authorisationHeader, false);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                try {
                    String authorisationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
                    processAuthDetails(authorisationHeader, true);
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                } catch (Exception ex) {
                    httpServletResponse.setHeader("error", ex.getMessage());
                    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                    httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());

                }
            }

        } finally {
            UserDetailsContext.clear();
        }
    }

    private void processAuthDetails(String authorisationHeader, boolean mandatory) {

        if (mandatory) {
            //throw appropriate errors
            String accessToken = authorisationHeader.substring("Bearer ".length());
            tokenProvider.validateToken(accessToken);
        } else
            try {
                if(StringUtils.isNotEmpty(authorisationHeader)) {

                    //Catch errors, no need to throw
                    String accessToken = authorisationHeader.substring("Bearer ".length());
                    tokenProvider.validateToken(accessToken);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

}
