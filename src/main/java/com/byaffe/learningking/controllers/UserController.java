package com.byaffe.learningking.controllers;

import com.googlecode.genericdao.search.Search;
import com.byaffe.learningking.dtos.auth.PermissionDTO;
import com.byaffe.learningking.dtos.auth.RoleDTO;
import com.byaffe.learningking.dtos.auth.UserDTO;
import com.byaffe.learningking.services.UserService;
import com.byaffe.learningking.services.impl.UserServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.models.User;
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

        User user = userService.saveUser(userDTO);
        return ResponseEntity.ok().body(UserDTO.fromModel(user));

    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> userById(@PathVariable(value = "id") long id) throws ValidationException {
       // creditService.updateUserCredit(id);
        User user = userService.getUserById(id);

        return ResponseEntity.ok().body(UserDTO.fromModel(user));

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable(value = "id", required = true) long id) throws ValidationException {
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

        Search search = UserServiceImpl.composeSearchObjectForUser(searchTerm);

        if (StringUtils.isNotEmpty(sortBy)) {
            search.addSort(sortBy, sortDescending != null ? sortDescending : true);
        }

        List<User> users = userService.getAllUsers(search, offset, limit);
        long totalRecords = userService.countAllUsers(search);
        return ResponseEntity.ok().body(new ResponseList<>(users, (int) totalRecords, offset, limit));

    }

    //Get Roles
    @GetMapping("/roles")
    public ResponseEntity<ResponseList<RoleDTO>> getRoles(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                          @RequestParam("offset") int offset,
                                                          @RequestParam("limit") int limit) {

        Search search = UserServiceImpl.composeSearchObjectForRole(searchTerm);

        List<RoleDTO> roles = userService.getAllRoles(search, offset, limit).stream().map(RoleDTO::fromRole).collect(Collectors.toList());
        return ResponseEntity.ok().body(new ResponseList<>(roles, 10, offset, limit));
    }


    @DeleteMapping("/roles/{id}")
    public ResponseEntity<BaseResponse> deleteRole(@PathVariable(value = "id", required = true) long id) throws ValidationException {
        userService.deleteRole(id);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    //Get permissions
    @GetMapping("/permissions")
    public ResponseEntity<List<PermissionDTO>> getPermissions() {
        List<PermissionDTO> users = PermissionDTO.getOrderedPermissions();
        return ResponseEntity.ok().body(users);
    }

    /**
     * Save Role
     *
     * @param role
     * @return
     */
    @PostMapping("/roles")
    public ResponseEntity<RoleDTO> saveRoles(@RequestBody RoleDTO role) {
        return ResponseEntity.ok().body(RoleDTO.fromRole(userService.saveRole(role)));
    }
}
