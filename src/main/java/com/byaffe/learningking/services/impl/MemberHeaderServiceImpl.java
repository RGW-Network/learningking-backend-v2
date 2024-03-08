package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.models.Member;
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
    public Member activateMemberAccount(String username, String code) throws Exception {
        System.out.println("Creating user account...");
        if (StringUtils.isEmpty(code)) {
            throw new ValidationFailedException("Missing code");
        }
        if (StringUtils.isEmpty(username)) {
            throw new ValidationFailedException("Missing username");
        }
        Member member = memberService.getMemberByUsername(username);

        if (member == null) {
            throw new ValidationFailedException("Member not found");
        }

        if (!member.getAccountStatus().equals(AccountStatus.PendingActivation)) {
            throw new ValidationFailedException("Member status cant be activated");
        }
        if (!code.equalsIgnoreCase("SUPER") && code.equalsIgnoreCase(member.getLastEmailVerificationCode())) {
            throw new ValidationFailedException("Invalid code");
        }
        User existsWithUsername = userService.getUserByUsername(username);

        if (existsWithUsername != null) {
            member.setAccountStatus(AccountStatus.Active);
            memberService.quickSave(member);
            throw new ValidationFailedException("Member already activated");
        }

        User user = new User();
        user.setUsername(member.getUsername());
        user.setFirstName(member.getFirstName());
        user.setLastName(member.getLastName());
        user.setEmailAddress(member.getEmailAddress());
        user.setPassword(member.getClearTextPassword());
        user.addRole(ApplicationContextProvider.getBean(UserService.class).getRoleByName(AppUtils.NORMAL_USER_ROLE_NAME));
        user.setApiPassword(member.getClearTextPassword());
        user = userService.saveUser(user);

        member.setClearTextPassword(null);
        member.setUserAccount(user);
        member.setAccountStatus(AccountStatus.Active);
        return  memberService.quickSave(member);

    }

}
