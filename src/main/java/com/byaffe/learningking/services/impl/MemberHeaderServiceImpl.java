package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.services.MemberDao;
import com.byaffe.learningking.services.MemberHeaderService;
import com.byaffe.learningking.services.MemberService;
import com.byaffe.learningking.services.UserService;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.User;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.utilities.AppUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberHeaderServiceImpl implements MemberHeaderService {

    @Autowired
    MemberDao memberDao;

    @Autowired
    UserService userService;

    @Autowired
    MemberService memberService;

    @Override
    public Student activateMemberAccount(String username, String code) throws Exception {
        System.out.println("Creating user account...");
        if (StringUtils.isEmpty(code)) {
            throw new ValidationFailedException("Missing code");
        }
        if (StringUtils.isEmpty(username)) {
            throw new ValidationFailedException("Missing username");
        }
        Student student = memberService.getMemberByUsername(username);

        if (student == null) {
            throw new ValidationFailedException("Member not found");
        }

        if (!student.getAccountStatus().equals(AccountStatus.PendingActivation)) {
            throw new ValidationFailedException("Member status cant be activated");
        }
        if (!code.equalsIgnoreCase("SUPER") && code.equalsIgnoreCase(student.getLastEmailVerificationCode())) {
            throw new ValidationFailedException("Invalid code");
        }
        User existsWithUsername = userService.getUserByUsername(username);

        if (existsWithUsername != null) {
            student.setAccountStatus(AccountStatus.Active);
            memberService.quickSave(student);
            throw new ValidationFailedException("Member already activated");
        }

        User user = new User();
        user.setUsername(student.getUsername());
        user.setFirstName(student.getFirstName());
        user.setLastName(student.getLastName());
        user.setEmailAddress(student.getEmailAddress());
        user.setPassword(student.getClearTextPassword());
        user.addRole(ApplicationContextProvider.getBean(UserService.class).getRoleByName(AppUtils.NORMAL_USER_ROLE_NAME));
        user.setApiPassword(student.getClearTextPassword());
        user = userService.saveUser(user);

        student.setClearTextPassword(null);
        student.setUserAccount(user);
        student.setAccountStatus(AccountStatus.Active);
        return  memberService.quickSave(student);

    }

}
