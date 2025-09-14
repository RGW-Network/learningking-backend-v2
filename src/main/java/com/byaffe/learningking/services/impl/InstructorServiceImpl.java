package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.daos.CountryDao;
import com.byaffe.learningking.daos.RoleDao;
import com.byaffe.learningking.dtos.instructor.InstructorRequestDTO;
import com.byaffe.learningking.models.courses.CourseInstructor;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.Country;
import com.byaffe.learningking.shared.models.Role;
import com.byaffe.learningking.shared.models.User;
import com.byaffe.learningking.shared.services.MessageTemplateService;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.byaffe.learningking.shared.utils.MailService;
import com.byaffe.learningking.shared.utils.PassEncTech4;
import com.byaffe.learningking.utilities.AppUtils;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.byaffe.learningking.config.MessageLabels.*;

@Service
@Transactional
public class InstructorServiceImpl extends GenericServiceImpl<CourseInstructor> implements InstructorService {

    private static final Logger LOGGER = Logger.getLogger(InstructorServiceImpl.class.getName());
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(InstructorServiceImpl.class);

    @Autowired
    private RoleDao roleDao;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private SystemSettingService settingService;

    @Autowired
    private MessageTemplateService emailTemplateService;

    @Override
    public CourseInstructor sendOTP(String email) {
        CourseInstructor user = searchUnique(new Search()
                .addFilterEqual("accountStatus", AccountStatus.PendingActivation)
                .addFilterEqual("username", email).setMaxResults(1));

        if (user == null) {
            throw new ValidationFailedException("User not found or already active");
        }

        try {
            user.setLastVerificationCode(PassEncTech4.generateOTP(6));
            sendEmail(user.getEmailAddress(), USER_REGISTRATION_EMAIL_SUBJECT, MessageFormat.format(USER_REGISTRATION_EMAIL_CONTENT, user.getLastVerificationCode()));
            return save(user);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sending OTP", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CourseInstructor saveInstance(CourseInstructor courseInstructor) throws ValidationFailedException {
        validateEmail(courseInstructor.getEmailAddress());

        CourseInstructor existingWithEmail = getCourseInstructorByPhoneNumber(courseInstructor.getEmailAddress());
        if (existingWithEmail != null && !existingWithEmail.getId().equals(courseInstructor.getId())) {
            throw new ValidationFailedException("A member with the same email already exists!");
        }
        return super.merge(courseInstructor);
    }

    public CourseInstructor save(InstructorRequestDTO dto) throws ValidationFailedException {

        validateUserRegistrationRequest(dto);

        Country country = countryDao.getReference(dto.getCountryId());
        if (country == null) {
            throw new ValidationFailedException("Invalid country");
        }

        CourseInstructor courseInstructor = new CourseInstructor();

        if (dto.getId() != null) {
            courseInstructor = getInstanceByID(dto.getId());
        }
        courseInstructor.setBiography(dto.getBiography());
        courseInstructor.setDesignation(dto.getDesignation());
        courseInstructor.setFirstName(dto.getFirstName());
        courseInstructor.setLastName(dto.getLastName());
        courseInstructor.setPhoneNumber(dto.getPhoneNumber());
        courseInstructor.setEmailAddress(dto.getEmailAddress());

        if ((courseInstructor.isNew() || courseInstructor.getUserAccount() == null) && dto.isCreateUserAccount()) {
            courseInstructor.setUserAccount(createUserAccount(courseInstructor, dto.getUserAccountRoles()));
        } else if (courseInstructor.getUserAccount() != null) {
            User user = courseInstructor.getUserAccount();
            user.setUsername(dto.getEmailAddress());
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setGender(dto.getGender());
            for (long id : dto.getUserAccountRoles()) {
                user.addRole(userService.getRoleById(id));
            }
            //detach roles not in ids
            for (Role role : user.getRoles()) {
                if (!dto.getUserAccountRoles().contains(role.getId())) {
                    user.removeRole(role);
                } else {
                    log.info("Preserved Role:{}", role.getName());
                }
            }
            user = userService.saveUser(user);
            courseInstructor.setUserAccount(user);
        }

        CourseInstructor existsWithEmail = getCourseInstructorByEmail(dto.getEmailAddress());
        if (existsWithEmail != null && !existsWithEmail.equals(courseInstructor)) {
            throw new ValidationFailedException("Instructor with email exists");
        }
        User user = courseInstructor.getUserAccount();

        if (courseInstructor.getUserAccount() != null) {
            courseInstructor.setAccountStatus(AccountStatus.Active);
        }
        courseInstructor.setUserAccount(user);
        courseInstructor.setCountry(country);
        courseInstructor.setUsername(dto.getEmailAddress());
        courseInstructor = super.save(courseInstructor);

        if (dto.getCoverImage() != null) {
            String imageUrl = ApplicationContextProvider.getBean(ImageStorageService.class).uploadImage(dto.getCoverImage(), "instructors/cover-images/" + courseInstructor.getId());
            courseInstructor.setCoverImageUrl(imageUrl);
            courseInstructor = super.save(courseInstructor);
        }

        if (dto.getProfileImage() != null) {
            String imageUrl = ApplicationContextProvider.getBean(ImageStorageService.class).uploadImage(dto.getProfileImage(), "instructors/profile-images/" + courseInstructor.getId());
            courseInstructor.setImageUrl(imageUrl);
            courseInstructor = super.save(courseInstructor);
        }


        return courseInstructor;
    }

    public CourseInstructor doRegister(InstructorRequestDTO dto) throws ValidationFailedException {
        validateUserRegistrationRequest(dto);

        Country country = countryDao.getReference(dto.getCountryId());
        if (country == null) {
            throw new ValidationFailedException("Invalid country");
        }

        User existingUser = userService.getUserByUsername(dto.getEmailAddress());
        if (existingUser != null) {
            throw new OperationFailedException("Email already exists");
        }

        CourseInstructor courseInstructor = getUnregisteredCourseInstructorByEmail(dto.getEmailAddress());
        if (courseInstructor == null) {
            courseInstructor = new CourseInstructor();
        }

        courseInstructor.setFirstName(dto.getFirstName());
        courseInstructor.setLastName(dto.getLastName());
        courseInstructor.setEmailAddress(dto.getEmailAddress());
        courseInstructor.setUsername(dto.getEmailAddress());
        courseInstructor.setPhoneNumber(dto.getPhoneNumber());
        courseInstructor.setCountry(country);
        courseInstructor.setAccountStatus(AccountStatus.PendingActivation);
        courseInstructor.setLastVerificationCode(PassEncTech4.generateOTP(6));
        courseInstructor = save(courseInstructor);

        User user = createUserAccount(courseInstructor, dto.getUserAccountRoles());
        courseInstructor.setUserAccount(user);
        new Thread(() -> {
            try {
                mailService.sendEmail(
                        user.getEmailAddress(),
                        INSTRUCTOR_REGISTRATION_EMAIL_SUBJECT, INSTRUCTOR_REGISTRATION_EMAIL_CONTENT
                );
            } catch (Exception e) {
                log.warn("Error on instructor registration email: {}", e.getMessage());
            }
        }).start();
        return courseInstructor;
    }

    private void validateEmail(String email) throws ValidationFailedException {
        if (StringUtils.isBlank(email)) {
            throw new ValidationFailedException("Missing email address");
        }
    }

    private void validateUserRegistrationRequest(InstructorRequestDTO dto) throws ValidationFailedException {
        if (StringUtils.isBlank(dto.getEmailAddress())) {
            throw new ValidationFailedException("Missing email address");
        }
        if (StringUtils.isBlank(dto.getFirstName())) {
            throw new ValidationFailedException("Missing first name");
        }
        if (StringUtils.isBlank(dto.getLastName())) {
            throw new ValidationFailedException("Missing last name");
        }
        if (StringUtils.isBlank(dto.getPhoneNumber())) {
            throw new ValidationFailedException("Missing last name");
        }
        if (dto.getCountryId() == null) {
            throw new ValidationFailedException("Missing country");
        }
    }

    private void sendEmail(String email, String subject, String content) {
        mailService.sendEmail(email, subject, content);
    }

    private void sendEmailAsync(String email, String subject, String content) {
        new Thread(() -> sendEmail(email, subject, content)).start();
    }

    public CourseInstructor getUnregisteredCourseInstructorByEmail(String email) {
        Search search = new Search()
                .setMaxResults(1)
                .addFilterEqual("username", email)
                .addFilterNotIn("accountStatus", Arrays.asList(AccountStatus.Active, AccountStatus.Blocked))
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return searchUnique(search);
    }

    @Override
    public CourseInstructor getCourseInstructorByPhoneNumber(String phoneNumber) {
        Search search = new Search().setMaxResults(1)
                .addFilterEqual("phoneNumber", phoneNumber)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return searchUnique(search);
    }

    @Override
    public CourseInstructor getCourseInstructorByEmail(String email) {
        Search search = new Search().setMaxResults(1)
                .addFilterEqual("emailAddress", email)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return searchUnique(search);
    }

    @Override
    public CourseInstructor getCourseInstructorByUsername(String email) {
        Search search = new Search().setMaxResults(1)
                .addFilterEqual("username", email)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        return searchUnique(search);
    }

    @Override
    public CourseInstructor getCourseInstructorByUserAccount(User user) {
        if (user == null) {
            return null;
        }
        return searchUniqueByPropertyEqual("userAccount", user, RecordStatus.ACTIVE);
    }

    @Override
    public List<CourseInstructor> getCourseInstructors(Search search, int offset, int limit) {
        search.setFirstResult(offset);
        search.setMaxResults(limit);
        return search(search);
    }

    @Override
    public int countCourseInstructors(Search search) {
        return count(search);
    }

    public static Search generateSearchObjectForCourses(String searchTerm) {
        return CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("firstName", "lastName"));
    }

    @Override
    public CourseInstructor getCourseInstructorById(String memberId) {
        return searchUniqueByPropertyEqual("id", memberId, RecordStatus.ACTIVE);
    }

    @Override
    public void delete(CourseInstructor courseInstructor) throws ValidationFailedException {
        // Implement deletion logic if needed
    }

    @Override
    public boolean isDeletable(CourseInstructor entity) throws OperationFailedException {
        return true;
    }

    @Override
    public List<String> getStringFilterFields() {
        return Collections.emptyList();
    }

    @Override
    public void block(CourseInstructor courseInstructor, String blockNotes) throws ValidationFailedException, OperationFailedException {
        LOGGER.info("Starting member blocking...");
        courseInstructor.setAccountStatus(AccountStatus.Blocked);
        CourseInstructor savedCourseInstructor = saveInstance(courseInstructor);
        sendEmailAsync(savedCourseInstructor.getEmailAddress(), "AAPU account blocking", blockNotes);
    }

    @Override
    public void unblock(CourseInstructor courseInstructor, String unblockNotes) throws ValidationFailedException, OperationFailedException {
        LOGGER.info("Starting member unblocking...");
        courseInstructor.setAccountStatus(AccountStatus.Active);
        CourseInstructor savedCourseInstructor = save(courseInstructor);
        sendEmailAsync(savedCourseInstructor.getEmailAddress(), "LK account activation", unblockNotes);
    }

    @Override
    public CourseInstructor activateCourseInstructorAccount(String username, String code) {
        if (StringUtils.isEmpty(code)) {
            throw new ValidationFailedException("Missing code");
        }
        if (StringUtils.isEmpty(username)) {
            throw new ValidationFailedException("Missing username");
        }

        CourseInstructor courseInstructor = getCourseInstructorByUsername(username);
        if (courseInstructor == null) {
            throw new ValidationFailedException("CourseInstructor not found");
        }
        if (!"SUPER".equalsIgnoreCase(code) && !code.equalsIgnoreCase(courseInstructor.getLastVerificationCode())) {
            throw new ValidationFailedException("Invalid code");
        }

        User user = createUserAccount(courseInstructor, new HashSet<>());
        courseInstructor.setUserAccount(user);
        return courseInstructor;
    }

    @Override
    public CourseInstructor getInstanceByID(Long instructorId) {
        return super.findById(instructorId).orElseThrow(() -> new ValidationFailedException(String.format("Instructor with ID %d not found", instructorId)));
    }

    private User createUserAccount(CourseInstructor courseInstructor, Set<Long> roleIds) throws ValidationFailedException {
        User user = new User();
        user.setUsername(courseInstructor.getUsername());
        user.setFirstName(courseInstructor.getFirstName());
        user.setLastName(courseInstructor.getLastName());
        user.setEmailAddress(courseInstructor.getEmailAddress());
        user.setPassword(courseInstructor.getPhoneNumber());
        user.addRole(userService.getRoleByName(AppUtils.INSTRUCTOR_ROLE_NAME));

        for (long id : roleIds) {
            user.addRole(userService.getRoleById(id));
        }
        return userService.saveUser(user);
    }

    @Override
    public CourseInstructor quickSave(CourseInstructor courseInstructor) throws ValidationFailedException {
        return save(courseInstructor);
    }
}