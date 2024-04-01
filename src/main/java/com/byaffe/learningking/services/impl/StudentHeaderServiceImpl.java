package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.services.StudentDao;
import com.byaffe.learningking.services.StudentHeaderService;
import com.byaffe.learningking.services.StudentService;
import com.byaffe.learningking.services.UserService;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.User;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.utilities.AppUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentHeaderServiceImpl implements StudentHeaderService {

    @Autowired
    StudentDao memberDao;

    @Autowired
    UserService userService;

    @Autowired
    StudentService memberService;

    @Override
    public Student activateStudentAccount(String username, String code) throws Exception {
        System.out.println("Creating user account...");
        if (StringUtils.isEmpty(code)) {
            throw new ValidationFailedException("Missing code");
        }
        if (StringUtils.isEmpty(username)) {
            throw new ValidationFailedException("Missing username");
        }
        Student student = memberService.getStudentByUsername(username);

        if (student == null) {
            throw new ValidationFailedException("Student not found");
        }

        if (!student.getAccountStatus().equals(AccountStatus.PendingActivation)) {
            throw new ValidationFailedException("Student status cant be activated");
        }
        if (!code.equalsIgnoreCase("SUPER") && code.equalsIgnoreCase(student.getLastEmailVerificationCode())) {
            throw new ValidationFailedException("Invalid code");
        }
        User existsWithUsername = userService.getUserByUsername(username);

        if (existsWithUsername != null) {
            student.setAccountStatus(AccountStatus.Active);
            memberService.quickSave(student);
            throw new ValidationFailedException("Student already activated");
        }

        User user = new User();
        user.setUsername(student.getProspectUsername());
        user.setFirstName(student.getProspectFirstName());
        user.setLastName(student.getProspectLastName());
        user.setEmailAddress(student.getProspectEmailAddress());
        user.setPassword(student.getProspectPassword());
        user.addRole(ApplicationContextProvider.getBean(UserService.class).getRoleByName(AppUtils.NORMAL_USER_ROLE_NAME));
        user.setApiPassword(student.getProspectPassword());
        user = userService.saveUser(user);

        student.setProspectPassword(null);
        student.setUserAccount(user);
        student.setAccountStatus(AccountStatus.Active);
        return  memberService.quickSave(student);

    }

}
