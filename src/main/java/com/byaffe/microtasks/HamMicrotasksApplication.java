package com.byaffe.microtasks;

import com.byaffe.microtasks.daos.BigDetailsDao;
import com.byaffe.microtasks.daos.TaskDao;
import com.byaffe.microtasks.daos.TaskExecutionDao;
import com.byaffe.microtasks.dtos.LookupValueDTO;
import com.byaffe.microtasks.models.BigDetails;
import com.byaffe.microtasks.models.LookupType;
import com.byaffe.microtasks.models.Task;
import com.byaffe.microtasks.models.TaskExecution;
import com.byaffe.microtasks.services.LookupValueService;
import com.byaffe.microtasks.services.TaskExecutionService;
import com.byaffe.microtasks.services.TaskService;
import com.byaffe.microtasks.services.UserService;
import com.byaffe.microtasks.shared.constants.SecurityConstants;
import com.byaffe.microtasks.shared.models.Country;
import com.byaffe.microtasks.shared.models.Role;
import com.byaffe.microtasks.shared.models.User;
import com.byaffe.microtasks.shared.utils.CountryApiResponseDTO;
import com.byaffe.microtasks.shared.utils.RestService;
import com.googlecode.genericdao.search.Search;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Logger;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "MicroTasks", version = "1.0", description = "MicroTasks MIS"))
public class HamMicrotasksApplication {

    public static void main(String[] args) {
        SpringApplication.run(HamMicrotasksApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    CommandLineRunner run(TaskService taskService, TaskExecutionService taskExecutionService, UserService userService, RestService restService, LookupValueService lookupValueService) {
        return args -> {
//            try {
//               for(Task task:taskService.getInstances(new Search().addFilterNull("bigDetails"),0,0)){
//                   if(StringUtils.isNotEmpty(task.getDetails())) {
//                       Logger.getAnonymousLogger().info("Task Update: on Task: " + task.getName());
//                       task.setBigDetails(new BigDetails(task.getDetails()));
//                       task.setDetails(null);
//                       taskService.directSave(task);
//                   }
//               }
//
//
//            } catch (Exception ex) {
//                Logger.getAnonymousLogger().severe("Task Update: " + ex.getMessage());
//            }
//            try {
//                for(TaskExecution task:taskExecutionService.getInstances(new Search().addFilterNull("bigDetails"),0,0)){
//                    if(StringUtils.isNotEmpty(task.getDetails())) {
//                        Logger.getAnonymousLogger().info("Task Execution Update: on Execution: " + task.getTask().getName());
//                        task.setBigDetails(new BigDetails(task.getDetails()));
//                        task.setDetails(null);
//                        taskExecutionService.directSave(task);
//                    }
//                }
//
//
//            } catch (Exception ex) {
//                Logger.getAnonymousLogger().severe("Task Update: " + ex.getMessage());
//            }


            try {
                Role super_admin_role = userService.saveRole(new Role(SecurityConstants.SUPER_ADMIN_ROLE, "Super admin role"));
            } catch (Exception ex) {

            }
            try {
                Role shop_owner = userService.saveRole(new Role(SecurityConstants.TASK_CREATOR_ROLE, "Task Creator"));
            } catch (Exception ex) {

            }
            try {
                Role shop_attendant = userService.saveRole(new Role(SecurityConstants.TASK_DOER, "Task Runner"));
            } catch (Exception ex) {

            }

            try {
                Role super_admin_role = userService.getRoleByName(SecurityConstants.SUPER_ADMIN_ROLE);
                User user = new User(SecurityConstants.DEFAULT_ADMIN_USERNAME, SecurityConstants.DEFAULT_ADMIN_PASSWORD, new HashSet<>(Arrays.asList(super_admin_role)));
                user.setFirstName("System");
                user.setLastName("Administrator");
                userService.saveUser(user);
            } catch (Exception ex) {

            }

            try {
                if (lookupValueService.countCountries(null) < 1) {
                    CountryApiResponseDTO countryApiResponseDTO = restService.getPostsPlainJSON();
                    for (CountryApiResponseDTO.CountryDTO data : countryApiResponseDTO.data) {
                        Country country = lookupValueService.save(new Country(data.name, data.Iso3));
                        Logger.getLogger(HamMicrotasksApplication.class.getName()).info("Saved Country>>>>" + country.getName());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        };
    }
}