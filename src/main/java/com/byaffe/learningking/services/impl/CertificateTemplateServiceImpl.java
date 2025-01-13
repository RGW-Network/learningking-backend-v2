package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.dtos.CertificateTemplateRequestDto;
import com.byaffe.learningking.models.CertificateTemplate;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

@Service
@Transactional
public class CertificateTemplateServiceImpl extends GenericServiceImpl<CertificateTemplate> implements CertificateTemplateService {

    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    InstructorService instructorService;

    @Override
    public CertificateTemplate saveInstance(CertificateTemplate certificateTemplate) throws ValidationFailedException {
        return super.save(certificateTemplate);

    }

    @Override
    public CertificateTemplate save(CertificateTemplateRequestDto dto) throws ValidationFailedException {

        if (StringUtils.isBlank(dto.getTitle())) {
            throw new ValidationFailedException("Missing Title");
        }
        CertificateTemplate existingWithTitle = getByTitle(dto.getTitle());

        if (existingWithTitle != null && !existingWithTitle.getId().equals(dto.getId())) {
            throw new ValidationFailedException("An certificateTemplate with the same title already exists!");
        }
        CertificateTemplate certificateTemplate= new CertificateTemplate();
        if(dto.getId()!=null &&dto.getId()>0) {
             certificateTemplate = getById(dto.getId());
        }

        certificateTemplate.setTitle(dto.getTitle());
        certificateTemplate.setTemplate(dto.getTemplate());
        certificateTemplate = saveInstance(certificateTemplate);
        return certificateTemplate;
    }



    public CertificateTemplate getByTitle(String certificateTemplateTitle) {
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addFilterEqual("title", certificateTemplateTitle);

        return super.searchUnique(search);

    }

    public CertificateTemplate getById(Long id) {
        return super.findById(id).orElseThrow(() -> new ValidationFailedException("No record Found"));
    }


    public static Search generateSearchTermsForCertificateTemplates(String searchTerm) {
        return CustomSearchUtils.generateSearchTerms(searchTerm,
                Collections.singletonList("title"));
    }


    @Override
    public boolean isDeletable(CertificateTemplate entity) throws OperationFailedException {
        return true; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
