package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.*;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.services.InstructorService;
import com.byaffe.learningking.services.StudentService;
import com.byaffe.learningking.services.UserService;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    StudentService studentService;

    @Autowired
    InstructorService instructorService;

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

    @PostMapping("/student/register")
    public ResponseEntity<BaseResponse> registerStudent(@RequestBody UserRegistrationRequestDTO userDTO) throws ValidationException {
        studentService.saveStudent(userDTO);
        return ResponseEntity.ok().body(new BaseResponse("Check your email for an OTP", true));
    }

    @PostMapping(path = "/student/{id}/update-profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseObject<Student>> updateProfile(@PathVariable(value = "id", required = true) long id,
                                                                 @RequestBody StudentProfileUpdateRequestDTO userDTO,
                                                                 @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                                                 @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) throws ValidationException {
        userDTO.setProfileImage(profileImage);
        userDTO.setCoverImage(coverImage);
        userDTO.setStudentId(id);
        Student student = studentService.updateProfile(userDTO);
        return ResponseEntity.ok().body(new ResponseObject<>(student));
    }

    @PostMapping("/student/verify-otp")
    public ResponseEntity<ResponseObject<Student>> verifyOtp(@RequestBody UserEmailVerificationRequestDTO userDTO) throws Exception {
        Student student = studentService.activateStudentAccount(userDTO.emailAddress, userDTO.getOtp());
        return ResponseEntity.ok().body(new ResponseObject<>(student));
    }

    @PostMapping("/student/resend-otp")
    public ResponseEntity<BaseResponse> sendOtp(@RequestBody UserEmailVerificationRequestDTO userDTO) throws ValidationException {
        studentService.sendOTP(userDTO.emailAddress);
        return ResponseEntity.ok().body(new BaseResponse("Verified OTP Successfully", true));
    }

    @PostMapping("/instructor/register")
    public ResponseEntity<BaseResponse> registerInstructor(@RequestBody InstructorRequestDTO userDTO) throws ValidationException {

        instructorService.doRegister(userDTO);
        return ResponseEntity.ok().body(new BaseResponse("Success", true));
    }


    @PostMapping("/instructor/verify-otp")
    public ResponseEntity<BaseResponse> verifyInstructorOtp(@RequestBody UserEmailVerificationRequestDTO userDTO) throws Exception {
        instructorService.activateCourseInstructorAccount(userDTO.emailAddress, userDTO.getOtp());
        return ResponseEntity.ok().body(new BaseResponse("Verified OTP Successfully", true));
    }


    @PostMapping("/instructor/resend-otp")
    public ResponseEntity<BaseResponse> resendInstructorOtp(@RequestBody UserEmailVerificationRequestDTO userDTO) throws ValidationException {
        instructorService.sendOTP(userDTO.emailAddress);
        return ResponseEntity.ok().body(new BaseResponse("Verified OTP Successfully", true));
    }


}
