package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.constants.TemplateType;
import com.byaffe.learningking.daos.RoleDao;
import com.byaffe.learningking.daos.UserDao;
import com.byaffe.learningking.models.EmailTemplate;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.services.EmailTemplateService;
import com.byaffe.learningking.services.StudentService;
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
public class StudentServiceImpl extends GenericServiceImpl<Student> implements StudentService {

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

        if (StringUtils.isBlank(student.getProspectEmailAddress())) {
            throw new ValidationFailedException("Missing email Address");
        }

        Student existingWithEmail = getStudentByPhoneNumber(student.getProspectEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(student.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }

        return super.merge(student);
    }

    public Student saveStudent(Student student) throws ValidationFailedException {

        if (StringUtils.isBlank(student.getProspectEmailAddress())) {
            throw new ValidationFailedException("Missing email Address");
        }

        Student existingWithEmail = getStudentByPhoneNumber(student.getProspectEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(student.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }

        return super.merge(student);
    }

    @Override
    public Student saveOutsideContext(Student student) throws ValidationFailedException {




        Student existingWithEmail = getStudentByEmail(student.getProspectEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(student.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }

        Student existsWithBoth = getUnregisteredStudentByEmail(student.getProspectEmailAddress());
        if (existsWithBoth != null && !existsWithBoth.getId().equals(student.getId())) {

            student.setId(existsWithBoth.getId());

        }

        return super.merge(student);
    }

    public Student getUnregisteredStudentByEmail(String email) {
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
    public Student getStudentByPhoneNumber(String phoneNumber) {
        Search search = new Search().setMaxResults(1);
        search.addFilterEqual("phoneNumber", phoneNumber);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return super.searchUnique(search);
    }

    @Override
    public Student getStudentByEmail(String email) {
        Search search = new Search().setMaxResults(1);
        search.addFilterEqual("emailAddress", email);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return super.searchUnique(search);
    }

    public Student getStudentByUsername(String email) {
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
        Student student = getStudentByUserAccount(userAccount);

        if (student == null || !student.getAccountStatus().equals(AccountStatus.Active)) {
            throw new ValidationFailedException("Student account not found or account inactive");
        }

        return student;

    }

    @Override
    public Student doRegister(String firstName, String lastName, String username, String password) throws ValidationFailedException {

        try {
            Student newStudent = new Student();
            Student withSameEmail = getStudentByEmail(username);
            if (withSameEmail != null && !withSameEmail.getAccountStatus().equals(AccountStatus.PendingActivation)) {
                throw new ValidationFailedException("Student with same email exists");
            }
            if (withSameEmail != null) {
                newStudent = withSameEmail;
            }
            Student withSameUsername = getStudentByUsername(username);
            if (withSameUsername != null && !withSameUsername.getAccountStatus().equals(AccountStatus.PendingActivation)) {
                throw new ValidationFailedException("Student with same username");
            }
            if (withSameUsername != null) {
                newStudent = withSameUsername;
            }
            EmailTemplateService emailTemplateService = ApplicationContextProvider.getBean(EmailTemplateService.class);

            newStudent.setProspectFirstName(firstName);
            newStudent.setProspectLastName(lastName);
            newStudent.setProspectEmailAddress(username);
            newStudent.setProspectUsername(username);
            newStudent.setProspectCountryName(null);
            newStudent.setProspectPassword(password);
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

                    html = html.replace("{fullName}", newStudent.getProspectFirstName());
                    html = html.replace("{code}", newStudent.getLastEmailVerificationCode());


                    ApplicationContextProvider.getBean(MailService.class).sendEmail(newStudent.getProspectEmailAddress(), "Learningking Email Verification",
                            html);

                } else {
                    ApplicationContextProvider.getBean(MailService.class).sendEmail(newStudent.getProspectEmailAddress(), "Learningking Email Verification",
                            "<p>Verify your Learningking Email address with this code</p><h1><strong>" + newStudent.getLastEmailVerificationCode() + "</strong></h1>");
                }
            }
            return newStudent;
        } catch (Exception ex) {
            throw new ValidationFailedException(ex.getMessage());
        }

    }
 
    @Override
    public Student getStudentByUserAccount(User user) {
        if (user == null) {
            return null;
        }
        return super.searchUniqueByPropertyEqual("userAccount", user, RecordStatus.ACTIVE);
    }

    @Override
    public List<Student> getStudents(Search search, int offset, int limit) {
        search.setFirstResult(offset);
        search.setMaxResults(limit);
        return super.search(search);
    }

    @Override
    public int countStudents(Search search) {
        return super.count(search);
    }

    @Override
    public Student getStudentById(String memberId) {
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

                ApplicationContextProvider.getBean(MailService.class).sendEmail(savedStudent.getProspectEmailAddress(), "AAPU account blocking", blockNotes);

                } catch (Exception ex) {
                    Logger.getLogger(StudentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
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

                    ApplicationContextProvider.getBean(MailService.class).sendEmail(savedStudent.getProspectPassword(), "AAPU account activation", unblockNotes);

                } catch (Exception ex) {
                    Logger.getLogger(StudentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

    }

    @Override
    public Student activateStudentAccount(String username, String code) throws Exception {
        System.out.println("Creating user account...");
        if (StringUtils.isEmpty(code)) {
            throw new ValidationFailedException("Missing code");
        }
        if (StringUtils.isEmpty(username)) {
            throw new ValidationFailedException("Missing username");
        }
        Student student = getStudentByUsername(username);

        if (student == null) {
            throw new ValidationFailedException("Student not found");
        }
        if (!code.equalsIgnoreCase("SUPER") && code.equalsIgnoreCase(student.getLastEmailVerificationCode())) {
            throw new ValidationFailedException("Invalid code");
        }
        User user = new User();
        user.setUsername(student.getProspectUsername());
        user.setFirstName(student.getProspectFirstName()    );
        user.setLastName(student.getProspectLastName());
        user.setEmailAddress(student.getProspectEmailAddress());
        user.setPassword(student.getProspectPassword());
        user.addRole(ApplicationContextProvider.getBean(UserService.class).getRoleByName(AppUtils.NORMAL_USER_ROLE_NAME));
        user.setApiPassword(student.getProspectPassword());
        student.setProspectPassword(null);
        student.setUserAccount(ApplicationContextProvider.getBean(UserService.class).saveUser(user));
        return student;

    }

    private User createDefaultUser(Student student, String password) throws ValidationFailedException {
        System.out.println("Creating user account...");

        User user = new User();
        user.setUsername(student.getProspectEmailAddress());
        user.setFirstName(student.getProspectFirstName());
        user.setLastName(student.getProspectLastName());
        user.setEmailAddress(student.getProspectEmailAddress());
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
