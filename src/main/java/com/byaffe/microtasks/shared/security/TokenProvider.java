package com.byaffe.microtasks.shared.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.byaffe.microtasks.services.UserService;
import com.byaffe.microtasks.controllers.UserDetailsContext;
import com.byaffe.microtasks.dtos.UserDTO;
import com.byaffe.microtasks.shared.models.User;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

/**
 * Bean to handle JWT operations
 */
@Component
public class TokenProvider {
    private Algorithm algorithm;
    private String TOKEN_CIPHER = "JABUYA_TOKEN_CIPHER";
    private String TOKEN_ISSURER = "jabuyasystems.com";
    private static  final String PERMISSIONS_KEY="permissions";
    private static long ACCESS_TOKEN_DURATION = 1 * 86400000l;// 1 days;
    private static long REFRESH_TOKEN_DURATION = 90 * 86400000l;// 90 days;
    private static long REMEMBER_ME_ACCESS_TOKEN_DURATION = 7 * 86400000l;// 7 days;

    @Autowired
    UserService userService;

    public TokenProvider() {
        String secretKey = System.getProperty(TOKEN_CIPHER);
        if(StringUtils.isBlank(secretKey)){
            secretKey=TOKEN_CIPHER;
        }
        this.algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }


    /**
     * Validates a supplied Jwt
     *
     * @param authToken
     * @return
     * @throws JWTVerificationException
     */
    public User validateToken(String authToken) throws JWTVerificationException {
        try {
            JWTVerifier jWTVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jWTVerifier.verify(authToken);
            String username = decodedJWT.getSubject();
            User userAccount = userService.getUserByUsername(username);

            UserDetailsContext.setLoggedInUser(userAccount);
            if (userAccount == null) {
                throw new JWTVerificationException("Invalid Credentials in Token");
            }
            return userAccount;
        }catch (Exception exception){
            throw new JWTVerificationException(exception.getMessage());
        }
    }

    /**
     * Creates a new JWT token
     * @param user
     * @param rememberMe
     * @return
     */
    public TokenPair createToken(@NonNull UserDTO user, boolean rememberMe) {
        Date dateNow = new Date();
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(dateNow.getTime() + (rememberMe ? REMEMBER_ME_ACCESS_TOKEN_DURATION : ACCESS_TOKEN_DURATION)))
                .withIssuer(TOKEN_ISSURER)
                .withClaim(PERMISSIONS_KEY, user.getPermissions()!=null?new ArrayList<>(user.getPermissions()):null)
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(dateNow.getTime() + REFRESH_TOKEN_DURATION))
                .withIssuer(TOKEN_ISSURER)
                .withClaim(PERMISSIONS_KEY,user.getPermissions()!=null? new ArrayList<>(user.getPermissions()):null)
                .sign(algorithm);
        return new TokenPair(accessToken, refreshToken);
    }

    /**
     * Creates a new access token from the supplied reftesh token
     * @param refreshToken
     * @return
     */
    public TokenPair refreshAccessToken(String refreshToken)throws JWTVerificationException {
        User userAccount = validateToken(refreshToken);
        if(userAccount==null){
            throw new JWTVerificationException("Invalid user details in token");
        }
        String accessToken = JWT.create()
                .withSubject(userAccount.getEmailAddress())
                .withExpiresAt(new Date(new Date().getTime() + ACCESS_TOKEN_DURATION))
                .withIssuer(TOKEN_ISSURER)
                .withClaim(PERMISSIONS_KEY, new ArrayList<>(userAccount.findPermissions()))
                .sign(algorithm);

        return new TokenPair(accessToken, refreshToken);
    }


    @Data
    public static class TokenPair {
        private String accessToken;
        private String refreshToken;

        public TokenPair(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

}
