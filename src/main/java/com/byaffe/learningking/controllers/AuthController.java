package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.*;
import com.byaffe.learningking.services.UserService;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    UserService userService;

    /**
     * Endpoint to register a microservice
     *
     * @param authDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<FullUserDTO> login(@RequestBody AuthDTO authDTO) throws ValidationException {
        UserDTO user = userService.authenticateUser(authDTO);
        TokenProvider.TokenPair tokenPair = tokenProvider.createToken(user, authDTO.isRememberMe());
        return ResponseEntity.ok().body(new FullUserDTO(user, tokenPair));
    }

    @PostMapping("/login-with-google")
    public ResponseEntity<FullUserDTO> loginWithGoogle(@RequestBody AuthDTO authDTO) throws ValidationException {
        UserDTO user = userService.authenticateUser(authDTO);
        TokenProvider.TokenPair tokenPair = tokenProvider.createToken(user, authDTO.isRememberMe());
        return ResponseEntity.ok().body(new FullUserDTO(user, tokenPair));
    }
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody UserRegistrationRequestDTO userDTO) throws ValidationException {

        userService.registerUser(userDTO);
        return ResponseEntity.ok().body(new BaseResponse("Check your email for an OTP",true));
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<BaseResponse> verifyOtp(@RequestBody UserEmailVerificationRequestDTO userDTO) throws ValidationException {
        userService.verifyOTP(userDTO.emailAddress, userDTO.getOtp());
        return ResponseEntity.ok().body(new BaseResponse("Verified OTP Successfully",true));
    }
    @PostMapping("/send-otp")
    public ResponseEntity<BaseResponse> sendOtp(@RequestBody UserEmailVerificationRequestDTO userDTO) throws ValidationException {
        userService.sendOTP(userDTO.emailAddress);
        return ResponseEntity.ok().body(new BaseResponse("Verified OTP Successfully",true));
    }

    @PostMapping("/register-with-google")
    public ResponseEntity<FullUserDTO> registerWithGoogle(@RequestBody AuthDTO authDTO) throws ValidationException {
        UserDTO user = userService.authenticateUser(authDTO);
        TokenProvider.TokenPair tokenPair = tokenProvider.createToken(user, authDTO.isRememberMe());
        return ResponseEntity.ok().body(new FullUserDTO(user, tokenPair));
    }

    //Build get members
    @PostMapping("/refresh/token")
    public ResponseEntity<TokenProvider.TokenPair> getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws ValidationException {
        String authorisationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorisationHeader != null && authorisationHeader.startsWith("Bearer ")) {
            String refreshToken = authorisationHeader.substring("Bearer ".length());
            TokenProvider.TokenPair tokenPair = tokenProvider.refreshAccessToken(refreshToken);
            return ResponseEntity.ok().body(tokenPair);
        } else {
            throw new ValidationException("Missing Refresh Token");
        }
    }
}
