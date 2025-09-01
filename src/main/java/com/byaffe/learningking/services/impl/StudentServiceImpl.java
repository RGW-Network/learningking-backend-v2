package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.daos.CountryDao;
import com.byaffe.learningking.dtos.student.StudentProfileUpdateRequestDTO;
import com.byaffe.learningking.dtos.auth.UserRegistrationRequestDTO;
import com.byaffe.learningking.models.LookupType;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.*;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.shared.services.MessageTemplateService;
import com.byaffe.learningking.shared.services.MessageTemplateUtils;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
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
import java.util.*;
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
MessageTemplateService  messageTemplateService;
    @Autowired
    SystemSettingService settingService;

    public static Search generateSearchTermsForStudents(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("firstName", "lastName","username","emailAddress","phoneNumber"));

        return search;
    }

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

    public Student updateProfile(StudentProfileUpdateRequestDTO dto) throws ValidationFailedException {
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

        Student student = getInstanceByID(dto.getStudentId());
        if (student == null) {
            throw new ValidationFailedException("Student not found wiith id");
        }

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setCountry(countryDao.getReference(dto.getCountryId()));
        student.setLocation(dto.getLocation());
        student.setBioInformation(dto.getBioInformation());
        student.setTwitterHandle(dto.getTwitterHandle());
        student.setInstagramHandle(dto.getInstagramHandle());
        student.setFacebookUsername(dto.getFacebookUsername());
        student.setWebsite(dto.getWebsite());
        student.setProfession(ApplicationContextProvider.getBean(LookupValueService.class).getByType(LookupType.PROFESSIONS, dto.getProfessionId()));
        student.setInterestNames(dto.getInterestNames());
        if (dto.getCoverImage() != null) {
            String imageUrl = imageStorageService.uploadImage(dto.getCoverImage(), "students/profile-images/" + student.getId());
            student.setCoverImageUrl(imageUrl);
        }
        if (dto.getProfileImage() != null) {
            String imageUrl = imageStorageService.uploadImage(dto.getProfileImage(), "students/cover-images/" + student.getId());
            student.setProfileImageUrl(imageUrl);
        }
        return super.save(student);
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
        if (StringUtils.isBlank(dto.confirmPassword) || StringUtils.isBlank(dto.password)) {
            throw new ValidationFailedException("Passwords don't match");
        }
        if (!dto.confirmPassword.equals(dto.password)) {
            throw new ValidationFailedException("Passwords don't match");
        }
        if (dto.getCountryId() == null) {
            throw new ValidationFailedException("Missing country");
        }
        Country country = countryDao.findById(dto.countryId).orElseThrow(() -> new ValidationFailedException("Invalid Country"));
        if (country == null) {
            throw new ValidationFailedException("Invalid country");
        }

        Student existingInactive = getUnregisteredStudentByEmail(dto.getEmailAddress());
        Student student = new Student();
        if (existingInactive != null) {
            student = existingInactive;
        }
        User existEWithUserName = userService.getUserByUsername(dto.getEmailAddress());
        if (existEWithUserName != null && !Objects.equals(existEWithUserName.getId(), student.getId())) {
            throw new OperationFailedException("Active User with email exists");

        }
        student.setFirstName(dto.firstName);
        student.setLastName(dto.lastName);
        student.setEmailAddress(dto.emailAddress);
        student.setUsername(dto.emailAddress);
        student.setCountry(country);
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setPassKey(dto.password);
        student.setAccountStatus(AccountStatus.PendingActivation);
        student.setLastEmailVerificationCode(PassEncTech4.generateOTP(6));
        student = super.save(student);

        Student finalStudent = student;
        new Thread(() -> {
            try {

                MessageTemplate messageTemplate=messageTemplateService.getActiveTemplate(MessageTemplateChannel.EMAIL, MessageTemplateType.NEW_STUDENT_SIGNUP_OTP);
                if(messageTemplate!=null) {
                    String subject = MessageTemplateUtils.resolveOTPMessageTemplate(finalStudent, messageTemplate.getSubject());
                    String body = MessageTemplateUtils.resolveOTPMessageTemplate(finalStudent,messageTemplate.getBody());
                    mailService.sendEmail( finalStudent.getEmailAddress(),  subject,body);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).run();

        return student;
    }


    public Student getUnregisteredStudentByEmail(String email) {
        Search search = new Search()
                .setMaxResults(1)
                .addFilterEqual("username", email)
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
        search.addFilterEqual("username", email);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return super.searchUnique(search);
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
    public Student getStudentById(Long memberId) {
        return super.searchUniqueByPropertyEqual("id", memberId, RecordStatus.ACTIVE);
    }

    @Override
    public void delete(Student student) throws ValidationFailedException {

    }


    @Override
    public List<String> getStringFilterFields() {
        return Collections.emptyList();
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
        user.addRole(ApplicationContextProvider.getBean(UserService.class).getRoleByName(AppUtils.STUDENT_ROLE_NAME));
        user.setApiPassword(student.getPassKey());
        student.setPassKey(null);
        student.setAccountStatus(AccountStatus.Active);
        student.setUserAccount(ApplicationContextProvider.getBean(UserService.class).saveUser(user));
        return student;

    }


    @Override
    public Student quickSave(Student student) throws ValidationFailedException {
        return super.save(student);

    }

}
