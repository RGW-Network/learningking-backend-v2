package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.student.CompanyRequestDTO;
import com.byaffe.learningking.models.courses.Organisation;
import com.byaffe.learningking.models.courses.OrganisationStudent;
import com.byaffe.learningking.services.CompanyService;
import com.byaffe.learningking.services.impl.CompanyServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
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
    CompanyService companyService;


    @GetMapping("/mine")
    public ResponseEntity<ResponseList<OrganisationStudent>> getMyCompanies(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                            @RequestParam(value = "offset", required = true) Integer offset,
                                                                            @RequestParam(value = "limit", required = true) Integer limit,
                                                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                            @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                                            @RequestParam(value = "featured", required = false) Boolean featured) throws JSONException {

        Search search = CompanyServiceImpl.generateSearchTermsForCompanyStudent(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addFilterEqual("student", UserDetailsContext.getLoggedInStudent());
        if (sortBy != null) {
            search.addSort(sortBy, sortDescending);
        }
        List<OrganisationStudent> Articles = companyService.getCompanyStudents(search, offset, limit);
        long count = companyService.countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(Articles, (int) count, offset, limit));

    }

    @GetMapping("")
    public ResponseEntity<ResponseList<Organisation>> getCompanies(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                   @RequestParam(value = "offset", required = true) Integer offset,
                                                                   @RequestParam(value = "limit", required = true) Integer limit,
                                                                   @RequestParam(value = "sortBy", required = false) String sortBy) throws JSONException {
        long count = 0;
        Search search = CompanyServiceImpl.generateSearchTermsForCompanyStudent(searchTerm).addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        List<Organisation> organisations = new ArrayList<>();
        if (!Objects.requireNonNull(UserDetailsContext.getLoggedInUser()).hasAdministrativePrivileges()) {
            search.addFilterEqual("student", UserDetailsContext.getLoggedInStudent());
            organisations = companyService.getCompanyStudents(search, offset, limit).stream().map(OrganisationStudent::getOrganisation).collect(Collectors.toList());
            count = companyService.countCompanyStudentInstances(search);
        } else {
            organisations = companyService.getInstances(search, offset, limit);
            count = companyService.countInstances(search);
        }

        return ResponseEntity.ok().body(new ResponseList<>(organisations, (int) count, offset, limit));
    }

    @PostMapping(path = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> uploadCSV(@RequestPart CompanyRequestDTO dto, @RequestPart(value = "logoImage", required = false) MultipartFile logoImage, @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        dto.setCoverImage(coverImage);
        dto.setLogoImage(logoImage);
        companyService.saveOrganisation(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    @PostMapping(path = "/{organisationId}/add-student")
    public ResponseEntity<BaseResponse> addStudent(@PathVariable Long organisationId, @RequestBody List<String> studentEmails) {
        for (String studentEmail : studentEmails) {
            companyService.addStudentToCompany(organisationId, studentEmail);
        }
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

}
