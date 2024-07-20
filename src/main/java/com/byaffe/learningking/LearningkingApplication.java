package com.byaffe.learningking;

import com.byaffe.learningking.services.LookupValueService;
import com.byaffe.learningking.services.TaskExecutionService;
import com.byaffe.learningking.services.TaskService;
import com.byaffe.learningking.services.UserService;
import com.byaffe.learningking.shared.constants.SecurityConstants;
import com.byaffe.learningking.shared.models.Country;
import com.byaffe.learningking.shared.models.Role;
import com.byaffe.learningking.shared.models.User;
import com.byaffe.learningking.shared.utils.CountryApiResponseDTO;
import com.byaffe.learningking.shared.utils.RestService;
import com.byaffe.learningking.utilities.AppUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
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
public class LearningkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningkingApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    CommandLineRunner run(TaskService taskService, TaskExecutionService taskExecutionService, UserService userService, RestService restService, LookupValueService LookupValueService) {
        return args -> {

            try {
                Role super_admin_role = userService.saveRole(new Role(SecurityConstants.SUPER_ADMIN_ROLE, "Super admin role"));
            } catch (Exception ex) {

            }
            try {
                Role shop_owner = userService.saveRole(new Role(AppUtils.INSTRUCTOR_ROLE_NAME, "Instructor"));
            } catch (Exception ex) {

            }
            try {
                Role shop_attendant = userService.saveRole(new Role(AppUtils.STUDENT_ROLE_NAME, "Student"));
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
                if (LookupValueService.countCountries(null) < 1) {
                    CountryApiResponseDTO countryApiResponseDTO = restService.getPostsPlainJSON();
                    for (CountryApiResponseDTO.CountryDTO data : countryApiResponseDTO.data) {
                        Country country = LookupValueService.save(new Country(data.name, data.Iso3));
                        Logger.getLogger(LearningkingApplication.class.getName()).info("Saved Country>>>>" + country.getName());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        };
    }
}