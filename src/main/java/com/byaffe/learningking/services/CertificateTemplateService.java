package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.CertificateTemplateRequestDto;
import com.byaffe.learningking.dtos.articles.EventRequestDTO;
import com.byaffe.learningking.models.CertificateTemplate;
import com.byaffe.learningking.models.Event;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link Event}
 *
 * @author RayGdhrt
 *
 */
public interface CertificateTemplateService extends GenericService<CertificateTemplate> {


    CertificateTemplate save(CertificateTemplateRequestDto dto) throws ValidationFailedException;


}
