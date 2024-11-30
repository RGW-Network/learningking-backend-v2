package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.courses.OrganisationGroupCourseRequestDTO;
import com.byaffe.learningking.dtos.courses.OrganisationGroupRequestDTO;
import com.byaffe.learningking.dtos.courses.OrganisationGroupStudentRequestDTO;
import com.byaffe.learningking.dtos.student.CompanyRequestDTO;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.OrganisationGroupService;
import com.byaffe.learningking.services.OrganisationService;
import com.byaffe.learningking.services.impl.OrganisationGroupServiceImpl;
import com.byaffe.learningking.services.impl.OrganisationServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.security.UserDetailsContext;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
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


    @GetMapping("/mine")
    public ResponseEntity<ResponseList<OrganisationStudent>> getMyCompanies(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                            @RequestParam(value = "offset", required = true) Integer offset,
                                                                            @RequestParam(value = "limit", required = true) Integer limit,
                                                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                            @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                                            @RequestParam(value = "featured", required = false) Boolean featured) throws JSONException {

        Search search = OrganisationServiceImpl.generateSearchTermsForCompanyStudent(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addFilterEqual("student", UserDetailsContext.getLoggedInStudent());
        if (sortBy != null) {
            search.addSort(sortBy, sortDescending);
        }
        List<OrganisationStudent> Articles = organisationService.getCompanyStudents(search, offset, limit);
        long count = organisationService.countCompanyStudentInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(Articles, (int) count, offset, limit));

    }

    @GetMapping("")
    public ResponseEntity<ResponseList<Organisation>> getCompanies(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                   @RequestParam(value = "offset", required = true) Integer offset,
                                                                   @RequestParam(value = "limit", required = true) Integer limit,
                                                                   @RequestParam(value = "sortBy", required = false) String sortBy) throws JSONException {
        long count = 0;
        Search search = OrganisationServiceImpl.generateSearchTermsForCompanyStudent(searchTerm).addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        List<Organisation> organisations = new ArrayList<>();
        if (!Objects.requireNonNull(UserDetailsContext.getLoggedInUser()).hasAdministrativePrivileges()) {
            search.addFilterEqual("student", UserDetailsContext.getLoggedInStudent());
            organisations = organisationService.getCompanyStudents(search, offset, limit).stream().map(OrganisationStudent::getOrganisation).collect(Collectors.toList());
            count = organisationService.countCompanyStudentInstances(search);
        } else {
            organisations = organisationService.getInstances(search, offset, limit);
            count = organisationService.countInstances(search);
        }

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
            organisationService.addStudentToCompany(organisationId, studentEmail);
        }
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    @PostMapping(path = "/groups")
    public ResponseEntity<ResponseObject<OrganisationGroup>> createGroup(@PathVariable Long organisationId, @RequestBody OrganisationGroupRequestDTO  dto) {
        dto.setOrganisationId(organisationId);
        return ResponseEntity.ok().body(new ResponseObject<>(organisationGroupService.createGroup(dto)));
    }
    @GetMapping("/groups")
    public ResponseEntity<ResponseList<OrganisationGroup>> getGroups(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                   @RequestParam(value = "offset", required = true) Integer offset,
                                                                   @RequestParam(value = "limit", required = true) Integer limit,
                                                                   @RequestParam(value = "sortBy", required = false) String sortBy) throws JSONException {
        long count = 0;
        Search search = OrganisationGroupServiceImpl.generateSearchTermsForGroup(searchTerm).addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        List<OrganisationGroup> organisations = organisationGroupService.getGroups(search, offset, limit);

        return ResponseEntity.ok().body(new ResponseList<>(organisations, (int) count, offset, limit));
    }

    @PostMapping(path = "/groups/students")
    public ResponseEntity<ResponseObject<OrganisationGroupStudent>> createGroupStudents( @RequestBody OrganisationGroupStudentRequestDTO dto) {

        return ResponseEntity.ok().body(new ResponseObject<>(organisationGroupService.addGroupStudent(dto.getOrganisationGroupId(),dto.getOrganisationStudentId())));
    }
    @PostMapping(path = "/groups/{groupId}/add-all-students")
    public ResponseEntity<ResponseObject<OrganisationGroup>> addAllStudentsToGroup( @RequestParam(value = "groupId") Long groupId) {

        return ResponseEntity.ok().body(new ResponseObject<>(organisationGroupService.addAllStudentsToGroup(groupId)));
    }
    @GetMapping("/groups/students")
    public ResponseEntity<ResponseList<OrganisationGroupStudent>> getGroupStudents(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                     @RequestParam(value = "offset", required = true) Integer offset,
                                                                     @RequestParam(value = "limit", required = true) Integer limit,
                                                                     @RequestParam(value = "sortBy", required = false) String sortBy) throws JSONException {
        long count = 0;
        Search search = OrganisationGroupServiceImpl.generateSearchTermsForStudent(searchTerm).addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        List<OrganisationGroupStudent> organisations = organisationGroupService.getGroupStudents(search, offset, limit);
        return ResponseEntity.ok().body(new ResponseList<>(organisations, (int) count, offset, limit));
    }

    @PostMapping(path = "/groups/courses")
    public ResponseEntity<ResponseObject<OrganisationGroupCourse>> createGroupCourses(@RequestBody OrganisationGroupCourseRequestDTO dto) {
        return ResponseEntity.ok().body(new ResponseObject<>(organisationGroupService.addGroupCourse(dto.getOrganisationGroupId() ,dto.getCourseId())));
    }
    @GetMapping("/groups/courses")
    public ResponseEntity<ResponseList<OrganisationGroupCourse>> getGroupCourses(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                                   @RequestParam(value = "offset", required = true) Integer offset,
                                                                                   @RequestParam(value = "limit", required = true) Integer limit,
                                                                                   @RequestParam(value = "sortBy", required = false) String sortBy) throws JSONException {
        long count = 0;
        Search search = OrganisationGroupServiceImpl.generateSearchTermsForCourse(searchTerm).addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        List<OrganisationGroupCourse> organisations = organisationGroupService.getGroupCourses(search, offset, limit);
        return ResponseEntity.ok().body(new ResponseList<>(organisations, (int) count, offset, limit));
    }
}
