package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.auth.RoleRequestDTO;
import com.byaffe.learningking.shared.constants.PermissionConstant;
import com.byaffe.learningking.shared.constants.SecurityConstants;
import com.byaffe.learningking.shared.models.Role;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.utilities.AppUtils;
import com.googlecode.genericdao.search.Search;
import com.byaffe.learningking.dtos.auth.PermissionDTO;
import com.byaffe.learningking.dtos.auth.UserDTO;
import com.byaffe.learningking.services.UserService;
import com.byaffe.learningking.services.impl.UserServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.models.User;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@Hidden
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;


    /**
     * Endpoint to register a microservice
     *
     * @return
     */
    @PostMapping("")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) throws ValidationException {
        SessionContext.permissionProtection(PermissionConstant.Manage_Users);
        User user = userService.saveUser(userDTO);
        return ResponseEntity.ok().body(UserDTO.fromModel(user));

    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> userById(@PathVariable(value = "id") long id) throws ValidationException {
        SessionContext.permissionProtection(PermissionConstant.Manage_Users);
        User user = userService.getUserById(id);

        return ResponseEntity.ok().body(UserDTO.fromModel(user));

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable(value = "id", required = true) long id) throws ValidationException {
        SessionContext.permissionProtection(PermissionConstant.Manage_Users);
        userService.deleteUser(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    //Get Users
    @GetMapping("")
    public ResponseEntity<ResponseList<User>> getUsers(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                       @RequestParam(value = "offset", required = false) int offset,
                                                       @RequestParam(value = "limit", required = false) int limit,
                                                       @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                       @RequestParam(value = "sortBy", required = false) String sortBy

    ) {
        SessionContext.permissionProtection(PermissionConstant.Manage_Users);
        Search search = UserServiceImpl.composeSearchObjectForUser(searchTerm);

        if (StringUtils.isNotEmpty(sortBy)) {
            search.addSort(sortBy, sortDescending != null ? sortDescending : true);
        }

        List<User> users = userService.getAllUsers(search, offset, limit).stream().filter((r)->!r.hasRole(SecurityConstants.SUPER_ADMIN_ROLE) &&!r.hasRole(AppUtils.INSTRUCTOR_ROLE_NAME) &&!r.hasRole(AppUtils.STUDENT_ROLE_NAME)).collect(Collectors.toList());

        long totalRecords = userService.countAllUsers(search);
        return ResponseEntity.ok().body(new ResponseList<>(users, (int) totalRecords, offset, limit));

    }

    //Get Roles
    @GetMapping("/roles")
    public ResponseEntity<ResponseList<Role>> getRoles(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                          @RequestParam("offset") int offset,
                                                          @RequestParam("limit") int limit) {
        SessionContext.permissionProtection(PermissionConstant.Manage_Users);
        Search search = UserServiceImpl.composeSearchObjectForRole(searchTerm);
        List<Role> roles = userService.getAllRoles(search, offset, limit);
        long count = userService.countRoles(search);
        return ResponseEntity.ok().body(new ResponseList<>(roles, count, offset, limit));
    }


    @DeleteMapping("/roles/{id}")
    public ResponseEntity<BaseResponse> deleteRole(@PathVariable(value = "id", required = true) long id) throws ValidationException {
        SessionContext.permissionProtection(PermissionConstant.Manage_Users);
        userService.deleteRole(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    //Get permissions
    @GetMapping("/permissions")
    public ResponseEntity<ResponseList<PermissionDTO.PermissionLookup>> getPermissions() {
        SessionContext.permissionProtection(PermissionConstant.Manage_Users);
        List<PermissionDTO.PermissionLookup> users = PermissionDTO.getOrderedPermissions();
        return ResponseEntity.ok().body(new ResponseList<>(users,users.size(),0,0));
    }

    /**
     * Save Role
     *
     * @param role
     * @return
     */
    @PostMapping("/roles")
    public ResponseEntity<Role> saveRoles(@RequestBody RoleRequestDTO role) {
        SessionContext.permissionProtection(PermissionConstant.Manage_Users);
        return ResponseEntity.ok().body(userService.saveRole(role));
    }
}
