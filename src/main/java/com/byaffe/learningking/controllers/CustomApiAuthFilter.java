package com.byaffe.learningking.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.byaffe.systems.constants.AccountStatus;
import org.byaffe.systems.core.services.MemberService;
import org.byaffe.systems.models.Member;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.springframework.http.HttpStatus;

public class CustomApiAuthFilter extends GenericFilterBean {

    private static final Logger LOGGER = Logger.getLogger(CustomApiAuthFilter.class.getName());

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        LOGGER.info("In Custom apifilter.......");
        if (FilterUtils.allowedAuth(request.getRequestURI())) {
            LOGGER.log(Level.INFO, "No auth action on this endpoint .......{0}", request.getRequestURI());
            filterChain.doFilter(request, response);
        } else {
            LOGGER.log(Level.INFO, "Starting JWT auth action on request -> {0}", request.getRequestURI());
            try {
                String authorisationHeader = request.getHeader(HttpConstants.AUTHORIZATION);
                if (authorisationHeader != null && authorisationHeader.startsWith("Bearer ")) {

                    String token = authorisationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(HttpConstants.TOKEN_CIPHER.getBytes());
                    JWTVerifier jWTVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jWTVerifier.verify(token);
                    String username = decodedJWT.getSubject();
                    Member member = ApplicationContextProvider.getBean(MemberService.class).getMemberByEmail(username);

                    if (member.getAccountStatus().equals(AccountStatus.Active)) {
                        request.setAttribute(HttpConstants.MEMBER_OBJECT_ATTRIBUTE, member);
                        filterChain.doFilter(request, response);

                    } else {

                        LOGGER.log(Level.SEVERE, "Inactive / Blocked account..");
                        response.setHeader("error", "User account blocked/inactive");
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.sendError(HttpStatus.UNAUTHORIZED.value());
                    }

                } else {
                    LOGGER.log(Level.SEVERE, "No auth tokens..");
                    response.setHeader("error", "Missing auth Token");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.sendError(HttpStatus.UNAUTHORIZED.value());
                }

            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error logging in: {0}", ex.getMessage());
                response.setHeader("error", ex.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.sendError(HttpStatus.UNAUTHORIZED.value());

            }

        }
    }
}
