package com.byaffe.learningking.controllers;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.dtos.courses.OrganisationGroupCourseRequestDTO;
import com.byaffe.learningking.dtos.courses.OrganisationGroupRequestDTO;
import com.byaffe.learningking.dtos.courses.OrganisationGroupStudentRequestDTO;
import com.byaffe.learningking.dtos.student.CompanyRequestDTO;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.OrganisationGroupService;
import com.byaffe.learningking.services.OrganisationService;
import com.byaffe.learningking.services.OrganisationStudentService;
import com.byaffe.learningking.services.impl.OrganisationGroupServiceImpl;
import com.byaffe.learningking.services.impl.OrganisationServiceImpl;
import com.byaffe.learningking.services.impl.OrganisationStudentServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.security.SessionContext;
import com.google.gson.Gson;
import com.googlecode.genericdao.search.Search;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/organisations")
public class CompanyController {
    @Autowired
    OrganisationService organisationService;
    @Autowired
    OrganisationGroupService organisationGroupService;

    @Autowired
    OrganisationStudentService organisationStudentService;

    @GetMapping("/mine")
    public ResponseEntity<ResponseList<OrganisationStudent>> getMyCompanies(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                            @RequestParam(value = "offset", required = true) Integer offset,
                                                                            @RequestParam(value = "limit", required = true) Integer limit,
                                                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                            @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                                            @RequestParam(value = "featured", required = false) Boolean featured) throws JSONException {

        Search search = OrganisationStudentServiceImpl.generateSearchTerms(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addFilterEqual("student", SessionContext.getLoggedInStudent());
        if (sortBy != null) {
            search.addSort(sortBy, sortDescending);
        }
        List<OrganisationStudent> records = organisationStudentService.getInstances(search, offset, limit);
        long count = organisationStudentService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(records, (int) count, offset, limit));

    }

    @GetMapping("")
    public ResponseEntity<ResponseList<Organisation>> getCompanies(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                   @RequestParam(value = "offset", required = true) Integer offset,
                                                                   @RequestParam(value = "limit", required = true) Integer limit,
                                                                   @RequestParam(value = "sortBy", required = false) String sortBy) throws JSONException {
        long count = 0;
        Search search = OrganisationServiceImpl.generateSearchTermsForCompanies(searchTerm).addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        List<Organisation> organisations = new ArrayList<>();
        if (!Objects.requireNonNull(SessionContext.getLoggedInUser()).hasAdministrativePrivileges()) {
            search.addFilterEqual("createdById", SessionContext.getLoggedInUser().getId());
        }
        organisations = organisationService.getInstances(search, offset, limit);
        count = organisationService.countInstances(search);


        return ResponseEntity.ok().body(new ResponseList<>(organisations, (int) count, offset, limit));
    }

    @PostMapping(path = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> uploadCSV(@RequestPart CompanyRequestDTO dto, @RequestPart(value = "logoImage", required = false) MultipartFile logoImage, @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        dto.setCoverImage(coverImage);
        dto.setLogoImage(logoImage);
        organisationService.saveOrganisation(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    @PostMapping(path = "/{organisationId}/add-students")
    public ResponseEntity<BaseResponse> addStudent(@PathVariable Long organisationId, @RequestBody List<String> studentEmails) {
        for (String studentEmail : studentEmails) {
            organisationStudentService.addStudentToCompany(organisationId, studentEmail);
        }
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    @PostMapping(path = "/{organisationId}/trigger-verification")
    public ResponseEntity<BaseResponse> verify(@PathVariable Long organisationId) {
        Organisation organisation = organisationService.getInstanceByID(organisationId);
        if (StringUtils.isEmpty(organisation.getEmailAddress())) {
            throw new ValidationFailedException("Missing Email Address");
        }
        if (StringUtils.isEmpty(organisation.getTelephoneNumber())) {
            throw new ValidationFailedException("Missing Telephone number");
        }
        organisationService.initiateVerification(organisation);
        //To-Do
        return ResponseEntity.ok().body(new BaseResponse("Verification link has been sent successfully to your organisation email", true));
    }


    @DeleteMapping(path = "/{organisationId}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long organisationId) {
        Organisation organisation = organisationService.getInstanceByID(organisationId);
        organisationService.deleteInstance(organisation);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    @PostMapping(path = "/groups")
    public ResponseEntity<ResponseObject<OrganisationGroup>> createGroup(@RequestBody OrganisationGroupRequestDTO dto) {
        return ResponseEntity.ok().body(new ResponseObject<>(organisationGroupService.createGroup(dto)));
    }

    @GetMapping("/groups")
    public ResponseEntity<ResponseList<OrganisationGroup>> getGroups(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                     @RequestParam(value = "offset", required = true) Integer offset,
                                                                     @RequestParam(value = "limit", required = true) Integer limit,
                                                                     @RequestParam(value = "organisationId", required = true) Long organisationId,
                                                                     @RequestParam(value = "sortBy", required = false) String sortBy) throws JSONException {
        long count = 0;
        Search search = OrganisationGroupServiceImpl.generateSearchTermsForGroup(searchTerm).addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        if (organisationId != null) search.addFilterEqual("organisation.id", organisationId);
        if (sortBy != null) search.addSortDesc(sortBy);

        List<OrganisationGroup> organisations = organisationGroupService.getGroups(search, offset, limit);

        return ResponseEntity.ok().body(new ResponseList<>(organisations, (int) count, offset, limit));
    }

    @PostMapping(path = "/groups/students")
    public ResponseEntity<ResponseObject<OrganisationGroupStudent>> createGroupStudents(@RequestBody OrganisationGroupStudentRequestDTO dto) {

        return ResponseEntity.ok().body(new ResponseObject<>(organisationGroupService.addGroupStudent(dto.getOrganisationGroupId(), dto.getOrganisationStudentId())));
    }

    @PostMapping(path = "/groups/{groupId}/add-all-students")
    public ResponseEntity<ResponseObject<OrganisationGroup>> addAllStudentsToGroup(@RequestParam(value = "groupId") Long groupId) {

        return ResponseEntity.ok().body(new ResponseObject<>(organisationGroupService.addAllStudentsToGroup(groupId)));
    }

    @GetMapping("/students")
    public ResponseEntity<ResponseList<OrganisationStudent>> getOrgStudents(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                                   @RequestParam(value = "offset", required = true) Integer offset,
                                                                                   @RequestParam(value = "limit", required = true) Integer limit,
                                                                                   @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                                   @RequestParam(value = "organisationId", required = false) Long organisationId,
                                                                                   @RequestParam(value = "groupId", required = false) Long groupId) throws JSONException {
        long count = 0;
        Search search = OrganisationStudentServiceImpl.generateSearchTerms(searchTerm).addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        if (organisationId != null) search.addFilterEqual("organisation.id", organisationId);

        //if (groupId != null) search.addFilterIn("organisationGroup.id", groupId);

        List<OrganisationStudent> organisations = organisationStudentService.getInstances(search, offset, limit);
        count = organisationStudentService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(organisations, (int) count, offset, limit));
    }


    @PostMapping(path = "/student/{organisationStudentId}/accept-invitation")
    public ResponseEntity<ResponseObject<OrganisationStudent>> acceptInvitation(@PathVariable(value = "organisationStudentId") Long organisationStudentId) {
        return ResponseEntity.ok().body(new ResponseObject<>(organisationStudentService.acceptInvitation(organisationStudentId)));
    }

    @PostMapping(path = "/student/{organisationStudentId}/decline-invitation")
    public ResponseEntity<ResponseObject<OrganisationStudent>> declineInvitation(@PathVariable(value = "organisationStudentId") Long organisationStudentId) {
        return ResponseEntity.ok().body(new ResponseObject<>(organisationStudentService.declineInvitation(organisationStudentId)));
    }

    @Hidden
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        organisationService.verifyEmail(token);
        String htmlResponse = "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>LK Organisation Email Verified</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; text-align: center; padding: 50px; background-color: #f4f4f4; }" +
                ".container { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 500px; margin: auto; }" +
                "h1 { color: #4CAF50; }" +
                "p { font-size: 18px; }" +
                ".button { display: inline-block; margin-top: 20px; padding: 10px 20px; color: white; background-color: #4CAF50; text-decoration: none; border-radius: 5px; font-size: 16px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>✅ Organisation Email Verified Successfully!</h1>" +
                "<p>Your organisation email has been verified. You can now add students and purchase courses with it.</p>" +
                "<a href='http://learningking.academy/student/manage-organization' class='button'>Take me back to learningking</a>" +
                "</div>" +
                "</body>" +
                "</html>";
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlResponse);
    }

}
