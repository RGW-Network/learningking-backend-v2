package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.dtos.UserRegistrationRequestDTO;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.MailService;
import com.googlecode.genericdao.search.Search;
import com.byaffe.learningking.daos.*;
import com.byaffe.learningking.dtos.AuthDTO;
import com.byaffe.learningking.dtos.RoleDTO;
import com.byaffe.learningking.dtos.UserDTO;
import com.byaffe.learningking.services.UserService;
import com.byaffe.learningking.shared.constants.Gender;
import com.byaffe.learningking.shared.constants.PermissionConstant;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ResourceNotFoundException;
import com.byaffe.learningking.shared.models.Country;
import com.byaffe.learningking.shared.models.Role;
import com.byaffe.learningking.shared.models.User;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.byaffe.learningking.shared.utils.PassEncTech4;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import static com.byaffe.learningking.config.MessageLabels.USER_REGISTRATION_EMAIL_CONTENT;
import static com.byaffe.learningking.config.MessageLabels.USER_REGISTRATION_EMAIL_SUBJECT;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userRepository;

    @Autowired
    MailService mailService;
    @Autowired
    RoleDao roleRepository;

    @Autowired
    TaskCreatorDao shopOwnerDao;

    @Autowired
    TaskDoerDao shopAttendantDao;
    @Autowired
    CountryDao countryDao;

    @Override
    public User saveUser(User user) {
        User existEWithUserName=getUserByUsername(user.getUsername());
        if (existEWithUserName != null && user.isNew()) {
            throw new OperationFailedException("User with username exists");
        }
        log.info("Saving new user to DB", user.getUsername());

        if (user.isNew()) {
            user.setPassword(PassEncTech4.generateSecurePassword(user.getPassword()));
        }
        return userRepository.save(user);

    }

    @Override
    public User saveUser(UserDTO dto) throws ValidationException {

        if (dto.id!=0) {
            throw new ValidationException("Editing not allowed on this method");
        }
        if (StringUtils.isBlank(dto.username)) {
            throw new ValidationException("Missing username");
        }
        if (StringUtils.isBlank(dto.firstName)) {
            throw new ValidationException("Missing first name");
        }

        if (StringUtils.isBlank(dto.lastName)) {
            throw new ValidationException("Missing lastname");
        }

        if (dto.id==0&&StringUtils.isBlank(dto.initialPassword)) {
            throw new ValidationException("Missing initialPassword");
        }
        User existsWithUsername = getUserByUsername(dto.username);
        if (existsWithUsername != null && existsWithUsername.getId() != dto.id) {
            throw new ValidationException("User with username exists");
        }

        Gender gender= Gender.fromId((int) dto.genderId);
        if(gender==null){
            throw new ValidationException("Missing/Invalid Gender Id");
        }

        User user = new User();
        if(dto.countryId!=0){
            Country country=countryDao.getReference(dto.countryId);
            if(country==null){
                throw new ValidationException("Missing Country");
            }
            user.setCountry(country);
        }

        user.setPassword(PassEncTech4.generateSecurePassword(dto.initialPassword));
        user.setUsername(dto.username);
        user.setEmailAddress(dto.emailAddress);
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setPhoneNumber(dto.phoneNumber);
        user.setGender(gender);

        for (long roleId : dto.roleIds) {
            Role role = roleRepository.getReference(roleId);
            user.addRole(role);
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserDTO dto) throws ValidationException {

        if (dto.id==0) {
            throw new ValidationException("Missing Id");
        }

        User user = getUserById(dto.id);
        if (user== null) {
            throw new ValidationException("User with id not found");
        }


        if (StringUtils.isBlank(dto.firstName)) {
            throw new ValidationException("Missing first name");
        }

        if (StringUtils.isBlank(dto.lastName)) {
            throw new ValidationException("Missing lastname");
        }

        if(dto.countryId!=0){
            Country country=countryDao.getReference(dto.countryId);

            if(country==null){
                throw new ValidationException("Missing Country");
            }
            user.setCountry(country);
        }
        user.setEmailAddress(dto.emailAddress);
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setPhoneNumber(dto.phoneNumber);

        Gender gender= Gender.fromId((int) dto.genderId);
        if(gender==null){
            throw new ValidationException("Missing/Invalid Gender Id");
        }
        user.setGender(gender);

        for (long roleId : dto.roleIds) {
            Role role = roleRepository.getReference(roleId);
            user.addRole(role);
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(long userId) throws ValidationException {
        if (userId==0) {
            throw new ValidationException("Missing Id");
        }

        User existsWithId = getUserById(userId);
        if (existsWithId == null) {
            throw new ValidationException("User with id not found");
        }

        existsWithId.setRecordStatus(RecordStatus.DELETED);
        userRepository.save(existsWithId);

    }

    @Override
    public void deleteRole(long userId) throws ValidationException {
        if (userId==0) {
            throw new ValidationException("Missing Id");
        }

        Role existsWithId = roleRepository.getReference(userId);
        if (existsWithId == null) {
            throw new ValidationException("Role with id not found");
        }

        existsWithId.setRecordStatus(RecordStatus.DELETED);
        roleRepository.save(existsWithId);

    }


    @Override
    public List<User> getAllUsers(Search search, int offset, int limit) {
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return userRepository.search(search);
    }
    public long countAllUsers(Search search){
        return userRepository.count(search);
    }
    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public UserDTO authenticateUser(AuthDTO authDTO) throws ValidationException {
        if (authDTO == null) {
            throw new ValidationException("No data specified");
        }
        if (StringUtils.isBlank(authDTO.getUsername())) {
            throw new ValidationException("Missing Username");
        }

        if (StringUtils.isBlank(authDTO.getPassword())) {
            throw new ValidationException("Missing password");
        }

        User user = getUserByUsername(authDTO.getUsername());

        if (user == null) {
            throw new ValidationException("Unknown credentials");
        }
        if (!PassEncTech4.verifyUserPassword(authDTO.getPassword(), user.getPassword())) {
            throw new ValidationException("Invalid Username or Password");
        }

        return UserDTO.fromModel(user, user.getBalance());
    }

    @Override
    public User registerUser(UserRegistrationRequestDTO dto) throws ValidationException {
        if (StringUtils.isBlank(dto.getEmailAddress())) {
            throw new ValidationException("Missing Email Address");
        }
        if (StringUtils.isBlank(dto.firstName)) {
            throw new ValidationException("Missing first name");
        }

        if (StringUtils.isBlank(dto.lastName)) {
            throw new ValidationException("Missing lastname");
        }


        if (StringUtils.isBlank(dto.confirmPassword)||StringUtils.isBlank(dto.password)) {
            throw new ValidationException("Passwords don't match");
        }
        if (!dto.confirmPassword.equals(dto.password)) {
            throw new ValidationException("Passwords don't match");
        }

//        if (dto.countryId==null||dto.countryId>0) {
//            throw new ValidationException("Missing country");
//        }
//        Country country= countryDao.getReference(dto.countryId);
//        if (country==null) {
//            throw new ValidationException("Invalid country");
//        }
        User existEWithUserName=getUserByUsername(dto.getEmailAddress());
        if (existEWithUserName != null) {
            if(existEWithUserName.getRecordStatus().equals(RecordStatus.ACTIVE_LOCKED)) {
             return    sendOTP(dto.emailAddress);
            }else{
                throw new OperationFailedException("User with email exists");
            }
        }
        User existEWithEmail=getUserByEmail(dto.getEmailAddress());
        if (existEWithEmail != null) {
            if(existEWithEmail.getRecordStatus().equals(RecordStatus.ACTIVE_LOCKED)) {
                return    sendOTP(dto.emailAddress);
            }else{
                throw new OperationFailedException("User with email exists");
            }
        }


        User user = new User();
        user.setEmailAddress(dto.getEmailAddress());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
       // user.setCountry(country);
        user.setRecordStatus(RecordStatus.ACTIVE_LOCKED);

        user.setUsername(dto.getEmailAddress());
        user.setPassword(dto.getConfirmPassword());
        user.setLastVerificationCode(PassEncTech4.generateOTP(6));
      User  savedUser= saveUser(user);
        new Thread(() -> {
            try {
                mailService.sendEmail(
                        user.getEmailAddress(),
                        USER_REGISTRATION_EMAIL_SUBJECT,
                        MessageFormat.format(USER_REGISTRATION_EMAIL_CONTENT, user.getLastVerificationCode())
                );

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).run();

        return savedUser;
    }

    public User sendOTP(String email) {
        User user= userRepository.searchUnique(new Search()
                .addFilterEqual("recordStatus",RecordStatus.ACTIVE_LOCKED)
                .addFilterEqual("username",email)
                .setMaxResults(1) );

        if(user == null){
            throw new ValidationFailedException("User not found or already active");
        }
        try {
            user.setLastVerificationCode(PassEncTech4.generateOTP(6));
            mailService.sendEmail(
                    user.getEmailAddress(),
                    USER_REGISTRATION_EMAIL_SUBJECT,
                    MessageFormat.format(USER_REGISTRATION_EMAIL_CONTENT, user.getLastVerificationCode())
            );
return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.searchUnique(new Search().addFilterEqual("username",username).setMaxResults(1));
    }

    public User getUserByEmail(String email) {
        return userRepository.searchUnique(new Search().addFilterEqual("emailAddress",email).setMaxResults(1));
    }

    public User verifyOTP(String email, String otp) {
       User user= userRepository.searchUnique(new Search()
               .addFilterEqual("recordStatus",RecordStatus.ACTIVE_LOCKED)
               .addFilterEqual("username",email)
               .addFilterEqual("lastVerificationCode",otp)
               .setMaxResults(1) );

       if(user == null){
           throw new ValidationFailedException("Invalid OTP");
       }
       user.setRecordStatus(RecordStatus.ACTIVE);

       return  userRepository.save(user);
    }
    @Override
    public Role saveRole(Role role) {
        if (getRoleByName(role.getName()) != null) {
            throw new OperationFailedException("Role with same name exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public Role saveRole(RoleDTO dto) {

        if (StringUtils.isBlank(dto.getName())) {
            throw new OperationFailedException("Missing name");
        }

        if (StringUtils.isBlank(dto.getDescription())) {
            throw new OperationFailedException("Missing description");
        }
        Role existsWithName = getRoleByName(dto.getName());
        if (existsWithName != null && existsWithName.getId() != dto.getId()) {
            throw new OperationFailedException("Role with same name exists");
        }

        Role newRole = new Role();
        newRole.setId(dto.getId());
        newRole.setName(dto.getName());
        newRole.setDescription(dto.getDescription());

        for (long permissionId : dto.getPermissionIds()) {
            PermissionConstant permission = PermissionConstant.getById(permissionId);
            newRole.addPermission(permission);
        }

        return roleRepository.save(newRole);
    }


    @Override
    public List<Role> getAllRoles(Search search, int offset, int limit) {
        search.setFirstResult(offset);
        search.setMaxResults(limit);
        return roleRepository.search(search);
    }
    public long countRoles(Search search){
       return  roleRepository.count(search);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleRepository.searchUnique(new Search().addFilterEqual("name",roleName));
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = getUserByUsername(username);
        Role role = getRoleByName(roleName);
        user.addRole(role);
        userRepository.save(user);
    }

    public static Search composeSearchObjectForUser(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,   Arrays.asList("username","lastName", "firstName"));
        search.addSortDesc("id");
           return  search;
        }

    public static Search composeSearchObjectForRole(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,   Arrays.asList("name","description"));

            return  search;
          }

}




