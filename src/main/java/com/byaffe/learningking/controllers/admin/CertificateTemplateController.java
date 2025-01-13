package com.byaffe.learningking.controllers.admin;

import com.byaffe.learningking.dtos.CertificateTemplateRequestDto;
import com.byaffe.learningking.models.CertificateTemplate;
import com.byaffe.learningking.services.CertificateTemplateService;
import com.byaffe.learningking.services.impl.CertificateTemplateServiceImpl;
import com.byaffe.learningking.shared.api.ResponseList;
import com.byaffe.learningking.shared.api.ResponseObject;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ray Gdhrt
 */
@Slf4j
@RestController
@RequestMapping("api/v1/admin/certificate-templates")
public class CertificateTemplateController {
    @Autowired
    ModelMapper modelMapper;
    @PostMapping("")
    public ResponseEntity<ResponseObject<CertificateTemplate>> addCertificateTemplate(@RequestBody CertificateTemplateRequestDto dto) throws JSONException {
        CertificateTemplate certificateTemplate=ApplicationContextProvider.getBean(CertificateTemplateService.class).save(dto);
        return ResponseEntity.ok().body(new ResponseObject<>(certificateTemplate));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<CertificateTemplate>> getById(@PathVariable(name = "id") long id) throws JSONException {
       CertificateTemplate certificateTemplate=ApplicationContextProvider.getBean(CertificateTemplateService.class).getInstanceByID(id);
        return ResponseEntity.ok().body(new ResponseObject<>(certificateTemplate));

    }
    @GetMapping("")
    public ResponseEntity<ResponseList<CertificateTemplate>> getCertificateTemplates(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                         @RequestParam(value = "offset", required = true) Integer offset,
                                                         @RequestParam(value = "limit", required = true) Integer limit) throws JSONException {

        Search search = CertificateTemplateServiceImpl.generateSearchTermsForCertificateTemplates(searchTerm);

        List<CertificateTemplate> certificateTemplates = ApplicationContextProvider.getBean(CertificateTemplateService.class).getInstances(search, offset, limit);
        long count = ApplicationContextProvider.getBean(CertificateTemplateService.class).countInstances(search);
        return ResponseEntity.ok().body(new ResponseList<>(certificateTemplates, (int) count, offset, limit));

    }




}
