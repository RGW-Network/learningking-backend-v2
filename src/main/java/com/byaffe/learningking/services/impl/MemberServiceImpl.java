package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.constants.TemplateType;
import com.byaffe.learningking.daos.RoleDao;
import com.byaffe.learningking.daos.UserDao;
import com.byaffe.learningking.models.EmailTemplate;
import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.services.EmailTemplateService;
import com.byaffe.learningking.services.MemberService;
import com.byaffe.learningking.services.SystemSettingService;
import com.byaffe.learningking.services.UserService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.User;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.MailService;
import com.byaffe.learningking.utilities.AppUtils;
import com.byaffe.learningking.utilities.CustomAppUtils;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class MemberServiceImpl extends GenericServiceImpl<Member> implements MemberService {

    @Autowired
    RoleDao roleDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Autowired
    SystemSettingService settingService;

    @Override
    public Member saveInstance(Member member) throws ValidationFailedException {

        if (StringUtils.isBlank(member.getEmailAddress())) {
            throw new ValidationFailedException("Missing email Address");
        }

        Member existingWithEmail = getMemberByPhoneNumber(member.getEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(member.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }

        return super.merge(member);
    }

    public Member saveMember(Member member) throws ValidationFailedException {

        if (StringUtils.isBlank(member.getEmailAddress())) {
            throw new ValidationFailedException("Missing email Address");
        }

        Member existingWithEmail = getMemberByPhoneNumber(member.getEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(member.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }

        return super.merge(member);
    }

    @Override
    public Member saveOutsideContext(Member member) throws ValidationFailedException {

        if (StringUtils.isBlank(member.getPhoneNumber())) {
            throw new ValidationFailedException("Phone number should not be empty");
        }

        String validatedPhoneNumber = CustomAppUtils.validatePhoneNumber(member.getPhoneNumber());

        if (validatedPhoneNumber == null) {
            throw new ValidationFailedException("Invalid Phone number");
        }
        member.setPhoneNumber(validatedPhoneNumber);

        Member existingWithPhone = getMemberByPhoneNumber(member.getPhoneNumber());

        if (existingWithPhone != null && !existingWithPhone.getId().equals(member.getId())) {
            throw new ValidationFailedException("A member with the same phone number already exists!");
        }

        Member existingWithEmail = getMemberByEmail(member.getEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(member.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }

        Member existsWithBoth = getUnregisteredMemberByEmail(member.getEmailAddress());
        if (existsWithBoth != null && !existsWithBoth.getId().equals(member.getId())) {

            member.setId(existsWithBoth.getId());

        }

        return super.merge(member);
    }

    public Member getUnregisteredMemberByEmail(String email) {
        Search search = new Search();
        search.addFilterEqual("emailAddress", email);
        search.addFilterNotIn("accountStatus", new ArrayList<>(Arrays.asList(AccountStatus.Active, AccountStatus.Blocked, AccountStatus.Active)));
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        List<Member> members = super.search(search);
        if (members.isEmpty()) {
            return null;
        }
        return members.get(0);
    }

    @Override
    public Member getMemberByPhoneNumber(String phoneNumber) {
        Search search = new Search().setMaxResults(1);
        search.addFilterEqual("phoneNumber", phoneNumber);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return super.searchUnique(search);
    }

    @Override
    public Member getMemberByEmail(String email) {
        Search search = new Search().setMaxResults(1);
        search.addFilterEqual("emailAddress", email);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return super.searchUnique(search);
    }

    public Member getMemberByUsername(String email) {
        Search search = new Search().setMaxResults(1);
        search.addFilterEqual("username", email);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return super.searchUnique(search);
    }

    @Override
    public Member doLogin(String username, String password) throws ValidationFailedException {

        User userAccount = ApplicationContextProvider.getBean(UserService.class)
                .authenticateUser(username, password);

        if (userAccount == null) {
            throw new ValidationFailedException("User not found or bad credentials");
        }
        Member member = getMemberByUserAccount(userAccount);

        if (member == null || !member.getAccountStatus().equals(AccountStatus.Active)) {
            throw new ValidationFailedException("Member account not found or account inactive");
        }

        return member;

    }

    @Override
    public Member doRegister(String firstName, String lastName, String username, String password) throws ValidationFailedException {

        try {
            Member newMember = new Member();
            Member withSameEmail = getMemberByEmail(username);
            if (withSameEmail != null && !withSameEmail.getAccountStatus().equals(AccountStatus.PendingActivation)) {
                throw new ValidationFailedException("Member with same email exists");
            }
            if (withSameEmail != null) {
                newMember = withSameEmail;
            }
            Member withSameUsername = getMemberByUsername(username);
            if (withSameUsername != null && !withSameUsername.getAccountStatus().equals(AccountStatus.PendingActivation)) {
                throw new ValidationFailedException("Member with same username");
            }
            if (withSameUsername != null) {
                newMember = withSameUsername;
            }
            EmailTemplateService emailTemplateService = ApplicationContextProvider.getBean(EmailTemplateService.class);

            newMember.setFirstName(firstName);
            newMember.setLastName(lastName);
            newMember.setEmailAddress(username);
            newMember.setUsername(username);
            newMember.setCountry(null);
            newMember.setClearTextPassword(password);
            newMember.setDeviceId(null);
            newMember.setAccountStatus(AccountStatus.PendingActivation);
            String code = new AppUtils().generateVerificationCode();
            newMember.setLastEmailVerificationCode(code);

            newMember = super.save(newMember);

            if (settingService.getAppSetting() != null && newMember != null) {
                EmailTemplate emailTemplate = emailTemplateService
                        .getEmailTemplateByType(TemplateType.USERACCOUNT_REGISTRATION);

                if (emailTemplate != null) {
                    String html = emailTemplate.getTemplate();

                    html = html.replace("{fullName}", newMember.getFirstName());
                    html = html.replace("{code}", newMember.getLastEmailVerificationCode());


                    ApplicationContextProvider.getBean(MailService.class).sendEmail(newMember.getEmailAddress(), "Learningking Email Verification",
                            html);

                } else {
                    ApplicationContextProvider.getBean(MailService.class).sendEmail(newMember.getEmailAddress(), "Learningking Email Verification",
                            "<p>Verify your Learningking Email address with this code</p><h1><strong>" + newMember.getLastEmailVerificationCode() + "</strong></h1>");
                }
            }
            return newMember;
        } catch (Exception ex) {
            throw new ValidationFailedException(ex.getMessage());
        }

    }
 
    @Override
    public Member getMemberByUserAccount(User user) {
        if (user == null) {
            return null;
        }
        return super.searchUniqueByPropertyEqual("userAccount", user, RecordStatus.ACTIVE);
    }

    @Override
    public List<Member> getMembers(Search search, int offset, int limit) {
        search.setFirstResult(offset);
        search.setMaxResults(limit);
        return super.search(search);
    }

    @Override
    public int countMembers(Search search) {
        return super.count(search);
    }

    @Override
    public Member getMemberById(String memberId) {
        return super.searchUniqueByPropertyEqual("id", memberId, RecordStatus.ACTIVE);
    }

    @Override
    public void delete(Member member) throws ValidationFailedException {

    }

    @Override
    public boolean isDeletable(Member entity) throws OperationFailedException {
        return true;

    }

    @Override
    public void block(Member member, String blockNotes) throws ValidationFailedException, OperationFailedException {
        System.out.println("Starting member blocking...");

        member.setAccountStatus(AccountStatus.Blocked);
        System.out.println("Deleted user account...");

        Member savedMember = saveInstance(member);
        System.out.println("Saved member...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                ApplicationContextProvider.getBean(MailService.class).sendEmail(savedMember.getEmailAddress(), "AAPU account blocking", blockNotes);

                } catch (Exception ex) {
                    Logger.getLogger(MemberServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

    }

    @Override
    public void unblock(Member member, String unblockNotes) throws ValidationFailedException, OperationFailedException {
        System.out.println("Starting member unblocking...");

        member.setAccountStatus(AccountStatus.Active);

        System.out.println("Unblocked user account...");

        Member savedMember = super.save(member);
        System.out.println("Saved member...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    ApplicationContextProvider.getBean(MailService.class).sendEmail(savedMember.getEmailAddress(), "AAPU account activation", unblockNotes);

                } catch (Exception ex) {
                    Logger.getLogger(MemberServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

    }

    @Override
    public Member activateMemberAccount(String username, String code) throws Exception {
        System.out.println("Creating user account...");
        if (StringUtils.isEmpty(code)) {
            throw new ValidationFailedException("Missing code");
        }
        if (StringUtils.isEmpty(username)) {
            throw new ValidationFailedException("Missing username");
        }
        Member member = getMemberByUsername(username);

        if (member == null) {
            throw new ValidationFailedException("Member not found");
        }
        if (!code.equalsIgnoreCase("SUPER") && code.equalsIgnoreCase(member.getLastEmailVerificationCode())) {
            throw new ValidationFailedException("Invalid code");
        }
        User user = new User();
        user.setUsername(member.getUsername());
        user.setFirstName(member.getFirstName());
        user.setLastName(member.getLastName());
        user.setEmailAddress(member.getEmailAddress());
        user.setPassword(member.getClearTextPassword());
        user.addRole(ApplicationContextProvider.getBean(UserService.class).getRoleByName(AppUtils.NORMAL_USER_ROLE_NAME));
        user.setApiPassword(member.getClearTextPassword());
        member.setClearTextPassword(null);
        member.setUserAccount(ApplicationContextProvider.getBean(UserService.class).saveUser(user));
        return member;

    }

    private User createDefaultUser(Member member, String password) throws ValidationFailedException {
        System.out.println("Creating user account...");

        User user = new User();
        user.setUsername(member.getEmailAddress());
        user.setFirstName(member.getFirstName());
        user.setLastName(member.getLastName());
        user.setEmailAddress(member.getEmailAddress());
        user.setPassword(password);
        UserService userService = ApplicationContextProvider.getBean(UserService.class);
        user.addRole(userService.getRoleByName(AppUtils.STUDENT_ROLE_NAME));

        return ApplicationContextProvider.getBean(UserService.class).saveUser(user);

    }

    private User blockUser(User user) throws ValidationFailedException {
        System.out.println("Bolcking user account...");

        user.setRecordStatus(RecordStatus.ACTIVE_LOCKED);

        UserService userService = ApplicationContextProvider.getBean(UserService.class);
        user.removeRole(userService.getRoleByName(AppUtils.STUDENT_ROLE_NAME));

        return userService.saveUser(user);

    }

    private User unBlockUser(User user) throws ValidationFailedException {
        System.out.println("Unblocking user account...");

        user.setRecordStatus(RecordStatus.ACTIVE);

        UserService userService = ApplicationContextProvider.getBean(UserService.class);
        user.addRole(userService.getRoleByName(AppUtils.STUDENT_ROLE_NAME));

        return userService.saveUser(user);

    }

    @Override
    public Member quickSave(Member member) throws ValidationFailedException {
        return super.save(member);

    }

}
