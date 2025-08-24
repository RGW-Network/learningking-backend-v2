package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.daos.OrganisationStudentDao;
import com.byaffe.learningking.dtos.student.CompanyRequestDTO;
import com.byaffe.learningking.models.LookupType;
import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.MessageTemplate;
import com.byaffe.learningking.shared.models.MessageTemplateChannel;
import com.byaffe.learningking.shared.models.MessageTemplateType;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.shared.services.MessageTemplateService;
import com.byaffe.learningking.shared.services.MessageTemplateUtils;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.byaffe.learningking.shared.utils.MailService;
import com.byaffe.learningking.shared.utils.PassEncTech4;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrganisationServiceImpl extends GenericServiceImpl<Organisation> implements OrganisationService {


    @Autowired
    ModelMapper modelMapper;
    @Autowired
    OrganisationStudentDao companyStudentDao;

    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    LookupValueService lookupValueService;
    @Autowired
    StudentService studentService;

    @Autowired
    SystemSettingService settingService;


    public Organisation saveOrganisation(CompanyRequestDTO dto) throws ValidationFailedException {
        if (dto.getAreaOfBusinessId() == null) {
            throw new ValidationFailedException("Missing category");
        }

        if (StringUtils.isBlank(dto.getName())) {
            throw new ValidationFailedException("Missing Title");
        }

        if (StringUtils.isBlank(dto.getDescription())) {
            throw new ValidationFailedException("Missing Description");
        }

        if (StringUtils.isBlank(dto.getTelephoneNumber())) {
            throw new ValidationFailedException("Missing Telephone Number");
        }

        if (StringUtils.isBlank(dto.getMobileNumber())) {
            throw new ValidationFailedException("Missing Mobile Number");
        }
        Organisation model = new Organisation();
        if (dto.getId() != null && dto.getId() > 0) {
            model = getInstanceByID(dto.getId());
        }
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setWebsite(dto.getWebsite());
        model.setMobileNumber(dto.getMobileNumber());
        model.setTelephoneNumber(dto.getTelephoneNumber());
        model.setEmailAddress(dto.getEmailAddress());

        if (model.isNew() || StringUtils.isEmpty(model.getTrainingMandate())) {
            model.setTrainingMandate(settingService.getAppSetting().getDefaultTrainingMandate());
        }
        model.setCountry(lookupValueService.getCountryById(dto.getCountryId()));
        model.setAreaOfBusiness(lookupValueService.getByType(LookupType.PROFESSIONS, dto.getAreaOfBusinessId()));
        model = save(model);

        if (dto.getCoverImage() != null) {
            String imageUrl = imageStorageService.uploadImage(dto.getCoverImage(), "companies/cover-images/" + model.getId());
            model.setCoverImageUrl(imageUrl);
            model = save(model);
        }
        if (dto.getLogoImage() != null) {
            String imageUrl = imageStorageService.uploadImage(dto.getLogoImage(), "companies/logos/" + model.getId());
            model.setLogoImageUrl(imageUrl);
            model = save(model);
        }
        {
            //add creator to company
            OrganisationStudent existsOnCompany = ApplicationContextProvider.getBean(OrganisationStudentService.class).getCompanyStudent(model, SessionContext.getLoggedInStudent());
            if (existsOnCompany == null) {
                OrganisationStudent organisationStudent = new OrganisationStudent();
                organisationStudent.setStudent(SessionContext.getLoggedInStudent());
                organisationStudent.setOrganisation(model);
                companyStudentDao.save(organisationStudent);
            }
        }
        return model;
    }

    @Override
    public Organisation saveInstance(Organisation instance) throws ValidationFailedException, OperationFailedException {

        if (StringUtils.isBlank(instance.getName())) {
            throw new ValidationFailedException("Missing Name");
        }

        if (instance.getAreaOfBusiness() == null) {
            throw new ValidationFailedException("Missing Area Of Business");
        }

        if (StringUtils.isBlank(instance.getDescription())) {
            throw new ValidationFailedException("Missing About details");
        }

        return save(instance);

    }


    public Organisation verifyEmail(String verificationCode) {
        Organisation organisation = searchUnique(new Search().addFilterEqual("lastVerificationCode", verificationCode).setMaxResults(1));
        if (organisation == null) throw new ValidationFailedException("Invalid verification code");
        organisation.setLastVerificationCode(null);
        organisation.setStatus(AccountStatus.Active);
        return super.save(organisation);
    }

    public Organisation initiateVerification(Organisation plan) throws ValidationFailedException {
        String lastVerification = UUID.randomUUID().toString();
        plan.setLastVerificationCode(lastVerification);
        MessageTemplate messageTemplate = ApplicationContextProvider.getBean(MessageTemplateService.class).getActiveTemplate(MessageTemplateChannel.EMAIL, MessageTemplateType.VERIFY_ORGANISATION);

        if (messageTemplate == null) throw new ValidationFailedException("Missing message template");
        String subject = MessageTemplateUtils.resolveOrganisationInvitation(plan, messageTemplate.getSubject());
        String body = MessageTemplateUtils.resolveOrganisationInvitation(plan, messageTemplate.getBody());
        ApplicationContextProvider.getBean(MailService.class).sendEmail(plan.getEmailAddress(), subject, body);
        plan.setStatus(AccountStatus.PendingActivation);

        return super.save(plan);
    }


    @Override
    public boolean isDeletable(Organisation entity) throws OperationFailedException {
        return true;
    }


    @Override
    public OrganisationStudent deActivate(OrganisationStudent plan) {
        plan.setRecordStatus(RecordStatus.DELETED);
        return companyStudentDao.save(plan);
    }


    public static Search generateSearchTermsForCompanies(String searchTerm) {

        return CustomSearchUtils.generateSearchTerms(searchTerm, Arrays.asList("title", "description"));
    }

}
