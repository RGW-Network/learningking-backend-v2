package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.constants.TemplateType;
import com.byaffe.learningking.daos.RoleDao;
import com.byaffe.learningking.daos.UserDao;
import com.byaffe.learningking.models.EmailTemplate;
import com.byaffe.learningking.models.Student;
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
public class MemberServiceImpl extends GenericServiceImpl<Student> implements MemberService {

    @Autowired
    RoleDao roleDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Autowired
    SystemSettingService settingService;

    @Override
    public Student saveInstance(Student student) throws ValidationFailedException {

        if (StringUtils.isBlank(student.getEmailAddress())) {
            throw new ValidationFailedException("Missing email Address");
        }

        Student existingWithEmail = getMemberByPhoneNumber(student.getEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(student.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }

        return super.merge(student);
    }

    public Student saveMember(Student student) throws ValidationFailedException {

        if (StringUtils.isBlank(student.getEmailAddress())) {
            throw new ValidationFailedException("Missing email Address");
        }

        Student existingWithEmail = getMemberByPhoneNumber(student.getEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(student.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }

        return super.merge(student);
    }

    @Override
    public Student saveOutsideContext(Student student) throws ValidationFailedException {

        if (StringUtils.isBlank(student.getPhoneNumber())) {
            throw new ValidationFailedException("Phone number should not be empty");
        }

        String validatedPhoneNumber = CustomAppUtils.validatePhoneNumber(student.getPhoneNumber());

        if (validatedPhoneNumber == null) {
            throw new ValidationFailedException("Invalid Phone number");
        }
        student.setPhoneNumber(validatedPhoneNumber);

        Student existingWithPhone = getMemberByPhoneNumber(student.getPhoneNumber());

        if (existingWithPhone != null && !existingWithPhone.getId().equals(student.getId())) {
            throw new ValidationFailedException("A member with the same phone number already exists!");
        }

        Student existingWithEmail = getMemberByEmail(student.getEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(student.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }

        Student existsWithBoth = getUnregisteredMemberByEmail(student.getEmailAddress());
        if (existsWithBoth != null && !existsWithBoth.getId().equals(student.getId())) {

            student.setId(existsWithBoth.getId());

        }

        return super.merge(student);
    }

    public Student getUnregisteredMemberByEmail(String email) {
        Search search = new Search();
        search.addFilterEqual("emailAddress", email);
        search.addFilterNotIn("accountStatus", new ArrayList<>(Arrays.asList(AccountStatus.Active, AccountStatus.Blocked, AccountStatus.Active)));
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        List<Student> students = super.search(search);
        if (students.isEmpty()) {
            return null;
        }
        return students.get(0);
    }

    @Override
    public Student getMemberByPhoneNumber(String phoneNumber) {
        Search search = new Search().setMaxResults(1);
        search.addFilterEqual("phoneNumber", phoneNumber);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return super.searchUnique(search);
    }

    @Override
    public Student getMemberByEmail(String email) {
        Search search = new Search().setMaxResults(1);
        search.addFilterEqual("emailAddress", email);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return super.searchUnique(search);
    }

    public Student getMemberByUsername(String email) {
        Search search = new Search().setMaxResults(1);
        search.addFilterEqual("username", email);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return super.searchUnique(search);
    }

    @Override
    public Student doLogin(String username, String password) throws ValidationFailedException {

        User userAccount = ApplicationContextProvider.getBean(UserService.class)
                .authenticateUser(username, password);

        if (userAccount == null) {
            throw new ValidationFailedException("User not found or bad credentials");
        }
        Student student = getMemberByUserAccount(userAccount);

        if (student == null || !student.getAccountStatus().equals(AccountStatus.Active)) {
            throw new ValidationFailedException("Member account not found or account inactive");
        }

        return student;

    }

    @Override
    public Student doRegister(String firstName, String lastName, String username, String password) throws ValidationFailedException {

        try {
            Student newStudent = new Student();
            Student withSameEmail = getMemberByEmail(username);
            if (withSameEmail != null && !withSameEmail.getAccountStatus().equals(AccountStatus.PendingActivation)) {
                throw new ValidationFailedException("Member with same email exists");
            }
            if (withSameEmail != null) {
                newStudent = withSameEmail;
            }
            Student withSameUsername = getMemberByUsername(username);
            if (withSameUsername != null && !withSameUsername.getAccountStatus().equals(AccountStatus.PendingActivation)) {
                throw new ValidationFailedException("Member with same username");
            }
            if (withSameUsername != null) {
                newStudent = withSameUsername;
            }
            EmailTemplateService emailTemplateService = ApplicationContextProvider.getBean(EmailTemplateService.class);

            newStudent.setFirstName(firstName);
            newStudent.setLastName(lastName);
            newStudent.setEmailAddress(username);
            newStudent.setUsername(username);
            newStudent.setCountry(null);
            newStudent.setClearTextPassword(password);
            newStudent.setDeviceId(null);
            newStudent.setAccountStatus(AccountStatus.PendingActivation);
            String code = new AppUtils().generateVerificationCode();
            newStudent.setLastEmailVerificationCode(code);

            newStudent = super.save(newStudent);

            if (settingService.getAppSetting() != null && newStudent != null) {
                EmailTemplate emailTemplate = emailTemplateService
                        .getEmailTemplateByType(TemplateType.USERACCOUNT_REGISTRATION);

                if (emailTemplate != null) {
                    String html = emailTemplate.getTemplate();

                    html = html.replace("{fullName}", newStudent.getFirstName());
                    html = html.replace("{code}", newStudent.getLastEmailVerificationCode());


                    ApplicationContextProvider.getBean(MailService.class).sendEmail(newStudent.getEmailAddress(), "Learningking Email Verification",
                            html);

                } else {
                    ApplicationContextProvider.getBean(MailService.class).sendEmail(newStudent.getEmailAddress(), "Learningking Email Verification",
                            "<p>Verify your Learningking Email address with this code</p><h1><strong>" + newStudent.getLastEmailVerificationCode() + "</strong></h1>");
                }
            }
            return newStudent;
        } catch (Exception ex) {
            throw new ValidationFailedException(ex.getMessage());
        }

    }
 
    @Override
    public Student getMemberByUserAccount(User user) {
        if (user == null) {
            return null;
        }
        return super.searchUniqueByPropertyEqual("userAccount", user, RecordStatus.ACTIVE);
    }

    @Override
    public List<Student> getMembers(Search search, int offset, int limit) {
        search.setFirstResult(offset);
        search.setMaxResults(limit);
        return super.search(search);
    }

    @Override
    public int countMembers(Search search) {
        return super.count(search);
    }

    @Override
    public Student getMemberById(String memberId) {
        return super.searchUniqueByPropertyEqual("id", memberId, RecordStatus.ACTIVE);
    }

    @Override
    public void delete(Student student) throws ValidationFailedException {

    }

    @Override
    public boolean isDeletable(Student entity) throws OperationFailedException {
        return true;

    }

    @Override
    public void block(Student student, String blockNotes) throws ValidationFailedException, OperationFailedException {
        System.out.println("Starting member blocking...");

        student.setAccountStatus(AccountStatus.Blocked);
        System.out.println("Deleted user account...");

        Student savedStudent = saveInstance(student);
        System.out.println("Saved member...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                ApplicationContextProvider.getBean(MailService.class).sendEmail(savedStudent.getEmailAddress(), "AAPU account blocking", blockNotes);

                } catch (Exception ex) {
                    Logger.getLogger(MemberServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

    }

    @Override
    public void unblock(Student student, String unblockNotes) throws ValidationFailedException, OperationFailedException {
        System.out.println("Starting member unblocking...");

        student.setAccountStatus(AccountStatus.Active);

        System.out.println("Unblocked user account...");

        Student savedStudent = super.save(student);
        System.out.println("Saved member...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    ApplicationContextProvider.getBean(MailService.class).sendEmail(savedStudent.getEmailAddress(), "AAPU account activation", unblockNotes);

                } catch (Exception ex) {
                    Logger.getLogger(MemberServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

    }

    @Override
    public Student activateMemberAccount(String username, String code) throws Exception {
        System.out.println("Creating user account...");
        if (StringUtils.isEmpty(code)) {
            throw new ValidationFailedException("Missing code");
        }
        if (StringUtils.isEmpty(username)) {
            throw new ValidationFailedException("Missing username");
        }
        Student student = getMemberByUsername(username);

        if (student == null) {
            throw new ValidationFailedException("Member not found");
        }
        if (!code.equalsIgnoreCase("SUPER") && code.equalsIgnoreCase(student.getLastEmailVerificationCode())) {
            throw new ValidationFailedException("Invalid code");
        }
        User user = new User();
        user.setUsername(student.getUsername());
        user.setFirstName(student.getFirstName());
        user.setLastName(student.getLastName());
        user.setEmailAddress(student.getEmailAddress());
        user.setPassword(student.getClearTextPassword());
        user.addRole(ApplicationContextProvider.getBean(UserService.class).getRoleByName(AppUtils.NORMAL_USER_ROLE_NAME));
        user.setApiPassword(student.getClearTextPassword());
        student.setClearTextPassword(null);
        student.setUserAccount(ApplicationContextProvider.getBean(UserService.class).saveUser(user));
        return student;

    }

    private User createDefaultUser(Student student, String password) throws ValidationFailedException {
        System.out.println("Creating user account...");

        User user = new User();
        user.setUsername(student.getEmailAddress());
        user.setFirstName(student.getFirstName());
        user.setLastName(student.getLastName());
        user.setEmailAddress(student.getEmailAddress());
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
    public Student quickSave(Student student) throws ValidationFailedException {
        return super.save(student);

    }

}
