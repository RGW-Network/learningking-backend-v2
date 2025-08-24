package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.courses.OrganisationGroupRequestDTO;
import com.byaffe.learningking.dtos.courses.OrganisationGroupStudentRequestDTO;
import com.byaffe.learningking.dtos.student.CompanyRequestDTO;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.services.impl.GroupPurchaseServiceImpl;
import com.byaffe.learningking.services.impl.OrganisationGroupServiceImpl;
import com.byaffe.learningking.services.impl.OrganisationServiceImpl;
import com.byaffe.learningking.services.impl.OrganisationStudentServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.security.SessionContext;
import com.googlecode.genericdao.search.Search;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Data;
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
@RequestMapping("/api/v1/organisations/groups")
public class CompanyGroupsController {
    @Autowired
    OrganisationService organisationService;
    @Autowired
    OrganisationGroupService organisationGroupService;
    @Autowired
    CourseService courseService;
    @Autowired
    OrganisationStudentService organisationStudentService;
    @Autowired
    OrganisationPurchaseService organisationPurchaseService;


    @PostMapping(path = "")
    public ResponseEntity<ResponseObject<OrganisationGroup>> createGroup(@RequestBody OrganisationGroupRequestDTO dto) {
        return ResponseEntity.ok().body(new ResponseObject<>(organisationGroupService.createGroup(dto)));
    }

    @GetMapping("")
    public ResponseEntity<ResponseList<OrganisationGroup>> getGroups(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                     @RequestParam(value = "offset", required = true) Integer offset,
                                                                     @RequestParam(value = "limit", required = true) Integer limit,
                                                                     @RequestParam(value = "organisationId", required = false) Long organisationId,
                                                                     @RequestParam(value = "sortBy", required = false) String sortBy) throws JSONException {
        long count = 0;
        Search search = OrganisationGroupServiceImpl.generateSearchTermsForGroup(searchTerm).addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        if (!Objects.requireNonNull(SessionContext.getLoggedInUser()).hasAdministrativePrivileges()) {
            search.addFilterEqual("organisation.createdById", SessionContext.getLoggedInUser().getId());
        }

        if (sortBy != null) search.addSortDesc(sortBy);

        List<OrganisationGroup> organisations = organisationGroupService.getGroups(search, offset, limit);

        return ResponseEntity.ok().body(new ResponseList<>(organisations, (int) count, offset, limit));
    }

    @PostMapping(path = "/students")
    public ResponseEntity<ResponseObject<OrganisationGroupStudent>> createGroupStudents(@RequestBody OrganisationGroupStudentRequestDTO dto) {

        return ResponseEntity.ok().body(new ResponseObject<>(organisationGroupService.addGroupStudent(dto.getOrganisationGroupId(), dto.getOrganisationStudentId())));
    }
    @DeleteMapping(path = "/students/{group-student-id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable(name = "group-student-id") Long groupStudentId) {
        organisationGroupService.deleteGroupStudent(groupStudentId);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }
    @GetMapping("/students")
    public ResponseEntity<ResponseList<OrganisationGroupStudent>> getGroupStudents(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                            @RequestParam(value = "offset", required = true) Integer offset,
                                                                            @RequestParam(value = "limit", required = true) Integer limit,
                                                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                            @RequestParam(value = "organisationId", required = false) Long organisationId,
                                                                            @RequestParam(value = "groupId", required = false) Long groupId) throws JSONException {
        long count = 0;
        Search search = OrganisationStudentServiceImpl.generateSearchTerms(searchTerm);
        if (organisationId != null) search.addFilterEqual("organisationGroup.organisation.id", organisationId);
        if (groupId != null) search.addFilterIn("organisationGroup.id", groupId);
        List<OrganisationGroupStudent> organisations = organisationGroupService.getGroupStudents(search, offset, limit);
        count = organisationGroupService.countGroupStudents(search);
        return ResponseEntity.ok().body(new ResponseList<>(organisations, (int) count, offset, limit));
    }
    @PostMapping(path = "/{groupId}/add-all-students")
    public ResponseEntity<ResponseObject<OrganisationGroup>> addAllStudentsToGroup(@PathVariable(value = "groupId") Long groupId) {
        return ResponseEntity.ok().body(new ResponseObject<>(organisationGroupService.addAllStudentsToGroup(groupId)));
    }




}
