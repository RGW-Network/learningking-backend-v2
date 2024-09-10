package com.byaffe.learningking.controllers;

import com.byaffe.learningking.dtos.CompanyRequestDTO;
import com.byaffe.learningking.models.courses.OrganisationStudent;
import com.byaffe.learningking.services.CompanyService;
import com.byaffe.learningking.services.impl.ArticleServiceImpl;
import com.byaffe.learningking.shared.api.BaseResponse;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("/v1/organisations")
public class ApiCompanyService {
@Autowired
    CompanyService companyService;

    @GetMapping("/mine")
    public ResponseEntity<ResponseList<OrganisationStudent>> getMyCompanies(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                                            @RequestParam(value = "offset", required = true) Integer offset,
                                                                            @RequestParam(value = "limit", required = true) Integer limit,
                                                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                                                            @RequestParam(value = "sortDescending", required = false) Boolean sortDescending,
                                                                            @RequestParam(value = "featured", required = false) Boolean featured) throws JSONException {

        Search search = ArticleServiceImpl.generateSearchTermsForArticles(searchTerm)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        if (sortBy != null) {
            search.addSort(sortBy,sortDescending);
        }
        List<OrganisationStudent> Articles = ApplicationContextProvider.getBean(CompanyService.class).getCompanyStudents(search, offset, limit);
        long count = ApplicationContextProvider.getBean(CompanyService.class).countInstances(search);

        return ResponseEntity.ok().body(new ResponseList<>(Articles, (int) count, offset, limit));


    }
    @PostMapping(path = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BaseResponse> uploadCSV(@RequestPart CompanyRequestDTO dto, @RequestPart(value = "logoImage",required = false) MultipartFile logoImage, @RequestPart(value = "coverImage",required = false) MultipartFile coverImage)  {
        dto.setCoverImage(coverImage);
        dto.setLogoImage(logoImage);
        ApplicationContextProvider.getBean(CompanyService.class).saveOrganisation(dto);
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

    @PostMapping(path = "/{organisationId}/add-student")
    public ResponseEntity<BaseResponse> addStudent(@PathVariable Long  organisationId, @RequestBody List<String> studentEmails)  {
        for(String studentEmail:studentEmails){
            ApplicationContextProvider.getBean(CompanyService.class).addStudentToCompany(organisationId,studentEmail);
        }
        return ResponseEntity.ok().body(new BaseResponse(true));
    }

}
