package com.byaffe.learningking.controllers;

import com.byaffe.learningking.controllers.dtos.UserRequestDTO;
import com.byaffe.learningking.dtos.AuthDTO;
import com.byaffe.learningking.dtos.FullUserDTO;
import com.byaffe.learningking.dtos.UserDTO;
import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.services.MemberHeaderService;
import com.byaffe.learningking.services.MemberService;
import com.byaffe.learningking.services.UserService;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.models.User;
import com.byaffe.learningking.shared.security.TokenProvider;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/v1/articles")
public class ApiAuthService {
    private static final Logger LOGGER = Logger.getLogger(ApiAuthService.class.getName());
    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    UserService userService;
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> registerUser(UserRequestDTO apiUserModel) {
        Member newMember = ApplicationContextProvider.getBean(MemberService.class).doRegister(apiUserModel.getFirstName(), apiUserModel.getLastName(), apiUserModel.getEmail(), apiUserModel.getPassword());
        return ResponseEntity.ok().body(new BaseResponse("Registartion Successfull", true));
    }

    @PostMapping("/verifyAccount")
    public ResponseEntity<BaseResponse> verifyAccount(UserRequestDTO apiSecurity) throws Exception {
        Member member = ApplicationContextProvider.getBean(MemberHeaderService.class).activateMemberAccount(apiSecurity.getUsername(), apiSecurity.getCode());
        return ResponseEntity.ok().body(new BaseResponse("Activation Successfull", true));
    }


    @PostMapping("/login")
    public ResponseEntity<FullUserDTO> login(@RequestBody AuthDTO authDTO) throws ValidationException {
        UserDTO user = userService.authenticateUser(authDTO);
        TokenProvider.TokenPair tokenPair = tokenProvider.createToken(user, authDTO.isRememberMe());
        return ResponseEntity.ok().body(new FullUserDTO(user, tokenPair));
    }

    @PostMapping("/refreshToken")
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
    @GetMapping("/getUserProfile")
    public ResponseEntity<User> userById(@PathVariable(value = "id") long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok().body(user);

    }
}
