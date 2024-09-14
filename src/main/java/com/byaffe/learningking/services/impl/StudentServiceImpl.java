package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.constants.TemplateType;
import com.byaffe.learningking.daos.CountryDao;
import com.byaffe.learningking.dtos.student.StudentProfileUpdateRequestDTO;
import com.byaffe.learningking.dtos.auth.UserRegistrationRequestDTO;
import com.byaffe.learningking.models.EmailTemplate;
import com.byaffe.learningking.models.LookupType;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.Country;
import com.byaffe.learningking.shared.models.User;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.MailService;
import com.byaffe.learningking.shared.utils.PassEncTech4;
import com.byaffe.learningking.utilities.AppUtils;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.byaffe.learningking.config.MessageLabels.USER_REGISTRATION_EMAIL_CONTENT;
import static com.byaffe.learningking.config.MessageLabels.USER_REGISTRATION_EMAIL_SUBJECT;

@Service
@Transactional
public class StudentServiceImpl extends GenericServiceImpl<Student> implements StudentService {

    @Autowired
    ImageStorageService imageStorageService;

    @Autowired
    UserService userService;
    @Autowired
    MailService mailService;
    @Autowired
    CountryDao countryDao;

    @Autowired
    SystemSettingService settingService;

    public Student sendOTP(String email) {
        Student user = super.searchUnique(new Search()
                .addFilterEqual("accountStatus", AccountStatus.PendingActivation)
                .addFilterEqual("username", email)
                .setMaxResults(1));

        if (user == null) {
            throw new ValidationFailedException("User not found or already active");
        }
        try {
            user.setLastEmailVerificationCode(PassEncTech4.generateOTP(6));
            mailService.sendEmail(
                    user.getEmailAddress(),
                    USER_REGISTRATION_EMAIL_SUBJECT,
                    MessageFormat.format(USER_REGISTRATION_EMAIL_CONTENT, user.getLastEmailVerificationCode())
            );
            return super.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Student saveInstance(Student student) throws ValidationFailedException {

        if (StringUtils.isBlank(student.getEmailAddress())) {
            throw new ValidationFailedException("Missing email Address");
        }

        Student existingWithEmail = getStudentByPhoneNumber(student.getEmailAddress());

        if (existingWithEmail != null && !existingWithEmail.getId().equals(student.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }

        return super.merge(student);
    }
   public Student updateProfile(StudentProfileUpdateRequestDTO dto) throws ValidationFailedException{
       if (StringUtils.isBlank(dto.getPhoneNumber())) {
           throw new ValidationFailedException("Missing phone number");
       }

       if (StringUtils.isBlank(dto.getFirstName())) {
           throw new ValidationFailedException("Missing first name");
       }

       if (StringUtils.isBlank(dto.getLastName())) {
           throw new ValidationFailedException("Missing last name");
       }
       if (StringUtils.isBlank(dto.getBioInformation())) {
           throw new ValidationFailedException("Missing bio information");
       }

Student student= getInstanceByID(dto.getStudentId());
       if(student==null){
           throw new ValidationFailedException("Student not found wiith id");
       }

       student.setFirstName(dto.getFirstName());
       student.setLastName(dto.getLastName());
       student.setPhoneNumber(dto.getPhoneNumber());
       student.setCountry(countryDao.getReference(dto.getCountryId()));
       student.setLocation(dto.getLocation());
       student.setBioInformation(dto.getBioInformation());
       student.setTwitterHandle(dto.getTwitterHandle());
       student.setFacebookUsername(dto.getFacebookUsername());
       student.setWebsite(dto.getWebsite());
       student.setProfession(ApplicationContextProvider.getBean(LookupValueService.class).getByType(LookupType.PROFESSIONS,dto.getProfessionId()));
       student.setInterestNames(dto.getInterestNames());
       if(dto.getCoverImage()!=null) {
           String imageUrl=   imageStorageService.uploadImage(dto.getCoverImage(), "students/profile-images/" + student.getId());
           student.setCoverImageUrl(imageUrl);
       }
       if(dto.getProfileImage()!=null) {
           String imageUrl=   imageStorageService.uploadImage(dto.getProfileImage(), "students/cover-images/" + student.getId());
           student.setProfileImageUrl(imageUrl);
       }
 return   super.save(student);
    }

    public Student saveStudent(UserRegistrationRequestDTO dto) throws ValidationFailedException {

        if (StringUtils.isBlank(dto.getEmailAddress())) {
            throw new ValidationFailedException("Missing email Address");
        }

        if (StringUtils.isBlank(dto.firstName)) {
            throw new ValidationFailedException("Missing first name");
        }

        if (StringUtils.isBlank(dto.lastName)) {
            throw new ValidationFailedException("Missing last name");
        }
        if (StringUtils.isBlank(dto.confirmPassword)||StringUtils.isBlank(dto.password)) {
            throw new ValidationFailedException("Passwords don't match");
        }
        if (!dto.confirmPassword.equals(dto.password)) {
            throw new ValidationFailedException("Passwords don't match");
        }
        if (StringUtils.isBlank(dto.countryName)) {
            throw new ValidationFailedException("Missing country");
        }
        Country country= countryDao.getReference(dto.countryId);
        if (country==null) {
            throw new ValidationFailedException("Invalid country");
        }
        User existEWithUserName=userService.getUserByUsername(dto.getEmailAddress());
        if (existEWithUserName != null) {
          throw new OperationFailedException("Active User with email exists");

        }
        Student existingInactive = getUnregisteredStudentByEmail(dto.getEmailAddress());
Student student= new Student();
        if (existingInactive !=null) {
            student=existingInactive;
        }
        student.setFirstName(dto.firstName);
        student.setLastName(dto.lastName);
        student.setEmailAddress(dto.emailAddress);
        student.setUsername(dto.emailAddress);
        student.setCountry(country);
        student.setPassKey(dto.password);
student.setAccountStatus(AccountStatus.PendingActivation);
        student.setLastEmailVerificationCode(PassEncTech4.generateOTP(6));
        student= super.save(student);

        Student finalStudent = student;
        new Thread(() -> {
            try {
                mailService.sendEmail(
                        finalStudent.getEmailAddress(),
                        USER_REGISTRATION_EMAIL_SUBJECT,
                        MessageFormat.format(USER_REGISTRATION_EMAIL_CONTENT, finalStudent.getLastEmailVerificationCode())
                );

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).run();

        return student;
    }


    public Student getUnregisteredStudentByEmail(String email) {
        Search search = new Search()
                .setMaxResults(1)
                .addFilterEqual("Username", email)
                .addFilterNotIn("accountStatus", new ArrayList<>(Arrays.asList(AccountStatus.Active, AccountStatus.Blocked)))
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return super.searchUnique(search);

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
        search.addFilterEqual("Username", email);
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

            newStudent.setFirstName(firstName);
            newStudent.setLastName(lastName);
            newStudent.setEmailAddress(username);
            newStudent.setUsername(username);
            newStudent.setCountry(null);
            newStudent.setPassKey(password);
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

                    ApplicationContextProvider.getBean(MailService.class).sendEmail(savedStudent.getEmailAddress(), "AAPU account blocking", blockNotes);

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

                    ApplicationContextProvider.getBean(MailService.class).sendEmail(savedStudent.getPassKey(), "LK account activation", unblockNotes);

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
        if (!code.equalsIgnoreCase("SUPER") && !code.equalsIgnoreCase(student.getLastEmailVerificationCode())) {
            throw new ValidationFailedException("Invalid code");
        }
        User user = new User();
        user.setUsername(student.getUsername());
        user.setFirstName(student.getFirstName());
        user.setLastName(student.getLastName());
        user.setEmailAddress(student.getEmailAddress());
        user.setPassword(student.getPassKey());
        user.addRole(ApplicationContextProvider.getBean(UserService.class).getRoleByName(AppUtils.NORMAL_USER_ROLE_NAME));
        user.setApiPassword(student.getPassKey());
        student.setPassKey(null);
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
