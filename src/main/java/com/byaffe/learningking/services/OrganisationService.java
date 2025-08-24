package com.byaffe.learningking.services;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.dtos.student.CompanyRequestDTO;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.MessageTemplate;
import com.byaffe.learningking.shared.models.MessageTemplateChannel;
import com.byaffe.learningking.shared.models.MessageTemplateType;
import com.byaffe.learningking.shared.services.MessageTemplateService;
import com.byaffe.learningking.shared.services.MessageTemplateUtils;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.MailService;
import com.googlecode.genericdao.search.Search;

import java.util.List;
import java.util.UUID;

;
/**
 * Responsible for CRUD operations on {@link Category}
 *
 * @author RayGdhrt
 *
 */
public interface OrganisationService extends GenericService<Organisation> {
    
     Organisation saveOrganisation(CompanyRequestDTO dto);

    public Organisation verifyEmail(String verificationCode) ;

    public Organisation initiateVerification(Organisation plan) ;

    OrganisationStudent deActivate(OrganisationStudent plan);
}
