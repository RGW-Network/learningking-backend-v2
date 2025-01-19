package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.OrganisationStudentDao;
import com.byaffe.learningking.dtos.student.CompanyRequestDTO;
import com.byaffe.learningking.models.LookupType;
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
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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

        Organisation article = modelMapper.map(dto, Organisation.class);
        if (article.isNew() || StringUtils.isEmpty(article.getTrainingMandate())) {
            article.setTrainingMandate(settingService.getAppSetting().getDefaultTrainingMandate());

        }
        article.setPublicationStatus(PublicationStatus.ACTIVE);
        article.setCountry(lookupValueService.getCountryById(dto.getCountryId()));
        article.setAreaOfBusiness(lookupValueService.getByType(LookupType.PROFESSIONS, dto.getAreaOfBusinessId()));
        article = saveInstance(article);

        if (dto.getCoverImage() != null) {
            String imageUrl = imageStorageService.uploadImage(dto.getCoverImage(), "companies/cover-images/" + article.getId());
            article.setCoverImageUrl(imageUrl);
            article = super.save(article);
        }
        if (dto.getLogoImage() != null) {
            String imageUrl = imageStorageService.uploadImage(dto.getLogoImage(), "companies/logos/" + article.getId());
            article.setLogoImageUrl(imageUrl);
            article = super.save(article);
        }
        {
            //add creator to company
            OrganisationStudent existsOnCompany = getCompanyStudent(article, SessionContext.getLoggedInStudent());

            if (existsOnCompany == null) {
                OrganisationStudent organisationStudent = new OrganisationStudent();
                organisationStudent.setStudent(SessionContext.getLoggedInStudent());
                organisationStudent.setOrganisation(article);
                companyStudentDao.save(organisationStudent);
            }
        }
        return article;
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

    @Override
    public List<Organisation> getInstances(Search arg0, int arg1, int arg2) {
        return super.getInstances(arg0, arg1, arg2);
    }

    @Override
    public int countInstances(Search arg0) {
        return super.countInstances(arg0);

    }

    @Override
    public int countCompanyStudentInstances(Search arg0) {
        return companyStudentDao.count(arg0);

    }

    @Override
    public Organisation activate(Organisation plan) throws ValidationFailedException {
        plan.setPublicationStatus(PublicationStatus.ACTIVE);

        return super.save(plan);
    }

    @Override
    public Organisation deActivate(Organisation plan) {
        plan.setPublicationStatus(PublicationStatus.INACTIVE);
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

    @Override
    public OrganisationStudent getOrganisationStudentById(long organisationStudentId) {
        return companyStudentDao.findById(organisationStudentId).orElseThrow(() -> new ValidationFailedException("Group Student With Id not found"));

    }


    @Override
    public List<OrganisationStudent> getCompanyStudents(Search search, int offset, int limit) {
        search.setMaxResults(limit);
        search.setFirstResult(offset);

        return companyStudentDao.search(search);
    }

    @Override
    public OrganisationStudent getCompanyStudent(Organisation organisation, Student student) {
        Search search = new Search();
        search.addFilterEqual("organisation", organisation);
        search.addFilterEqual("student", student);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return companyStudentDao.searchUnique(search);
    }

    @Override
    public void delete(OrganisationStudent companyCourse) {

        companyCourse.setRecordStatus(RecordStatus.DELETED);

        companyStudentDao.save(companyCourse);
    }


    public void addStudentToCompany(long organizationId, String studentEmail) throws ValidationFailedException {
        if (StringUtils.isEmpty(studentEmail)) {
            throw new ValidationFailedException("Missing email");
        }
        Student student = studentService.getStudentByEmail(studentEmail);
        if (student == null) {
            //To-do send invitation email
            MessageTemplate emailTemplate = ApplicationContextProvider.getBean(MessageTemplateService.class).getActiveTemplate(MessageTemplateChannel.EMAIL, MessageTemplateType.ORGANISATION_INVITATION);
            if(emailTemplate!=null) {
                String subject = MessageTemplateUtils.resolveLkInvitationMessageTemplate(studentEmail, SessionContext.getLoggedInUser(), emailTemplate.getSubject());
                String body = MessageTemplateUtils.resolveLkInvitationMessageTemplate(studentEmail, SessionContext.getLoggedInUser(), emailTemplate.getBody());
                ApplicationContextProvider.getBean(MailService.class).sendEmail(studentEmail, subject, body);
            }
            return;
        }
        Organisation organisation = getReference(organizationId);
        if (organisation == null) {
            throw new ValidationFailedException("Organisation with Id  not found");
        }
        OrganisationStudent existsOnCompany = getCompanyStudent(organisation, student);
        if (existsOnCompany != null) {
            throw new ValidationFailedException("User already exists on this org");
        }
        OrganisationStudent organisationStudent = new OrganisationStudent();
        organisationStudent.setStudent(student);
        organisationStudent.setOrganisation(organisation);
        {//send email
            MessageTemplate emailTemplate = ApplicationContextProvider.getBean(MessageTemplateService.class).getActiveTemplate(MessageTemplateChannel.EMAIL, MessageTemplateType.ORGANISATION_INVITATION);
           if(emailTemplate!=null) {
               String subject = MessageTemplateUtils.resolveLkInvitationMessageTemplate(studentEmail, SessionContext.getLoggedInUser(), emailTemplate.getSubject());
               String body = MessageTemplateUtils.resolveLkInvitationMessageTemplate(studentEmail, SessionContext.getLoggedInUser(), emailTemplate.getBody());
               ApplicationContextProvider.getBean(MailService.class).sendEmail(studentEmail, subject, body);
           }
        }
        companyStudentDao.save(organisationStudent);
    }

    public static Search generateSearchTermsForCompanyStudent(String searchTerm) {

        return CustomSearchUtils.generateSearchTerms(searchTerm, Arrays.asList("title", "description"));
    }

}
