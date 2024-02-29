package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.UserRegistrationRequestDTO;
import com.googlecode.genericdao.search.Search;
import com.byaffe.learningking.dtos.AuthDTO;
import com.byaffe.learningking.dtos.RoleDTO;
import com.byaffe.learningking.dtos.UserDTO;
import com.byaffe.learningking.shared.models.Role;
import com.byaffe.learningking.shared.models.User;

import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * Handles CRUD operations on the {@link  User}
 */
public interface UserService {

        /**
         *
         * @param user
         * @return
         */
        User saveUser(User user);

        User saveUser(UserDTO userDTO) throws ValidationException;

        User updateUser(UserDTO userDTO) throws ValidationException;
        void deleteUser(long userId) throws ValidationException;

        void deleteRole(long roleId) throws ValidationException;

        /**
         *
         * @return
         */
        List<User> getAllUsers(Search search, int offset, int limit);

        long countAllUsers(Search search);

        /**
         *
         * @param id
         * @return
         */
        User getUserById(long id);

        UserDTO authenticateUser(AuthDTO authDTO) throws ValidationException;

        User registerUser(UserRegistrationRequestDTO authDTO) throws ValidationException;


        /**
         *
         * @param username
         * @return
         */
        User getUserByUsername(String username);


        /**
         *
         * @param role
         * @return
         */
        Role saveRole(Role role);
        Role saveRole(RoleDTO role);
         User verifyOTP(String email, String otp) ;
        /**
         *
         * @return
         */
        List<Role> getAllRoles(Search search, int offset, int limit);

        long countRoles(Search search);



        /**
         *
         * @param roleName
         * @return
         */
        Role getRoleByName(String roleName);

        /**
         *
         * @param username
         * @param roleName
         */
        void addRoleToUser(String username, String roleName);
         User sendOTP(String email) ;
}
