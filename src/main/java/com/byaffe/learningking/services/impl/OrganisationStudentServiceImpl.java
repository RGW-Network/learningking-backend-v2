package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.OrganisationStudentDao;
import com.byaffe.learningking.models.InvitationStatus;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.Organisation;
import com.byaffe.learningking.models.courses.OrganisationStudent;
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
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional
public class OrganisationStudentServiceImpl extends GenericServiceImpl<OrganisationStudent> implements OrganisationStudentService {


    @Autowired
    OrganisationStudentDao companyStudentDao;

    @Autowired
    StudentService studentService;



    @Override
    public OrganisationStudent getCompanyStudent(Organisation organisation, Student student) {
        Search search = new Search();
        search.addFilterEqual("organisation", organisation);
        search.addFilterEqual("student", student);
        search.setMaxResults(1);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return companyStudentDao.searchUnique(search);
    }

    public OrganisationStudent acceptInvitation(Long organisationStudentId) {
        OrganisationStudent student = getInstanceByID(organisationStudentId);
        if (student.getInvitationStatus() == InvitationStatus.Confirmed) {
            throw new ValidationFailedException("Invalid status");
        }
        if (student.getInvitationStatus() == InvitationStatus.Declined) {
            throw new ValidationFailedException("Invalid status");
        }

        student.setInvitationStatus(InvitationStatus.Confirmed);
        return saveInstance(student);
    }

    public OrganisationStudent declineInvitation(Long organisationStudentId) {
        OrganisationStudent student = getInstanceByID(organisationStudentId);
        if (student.getInvitationStatus() == InvitationStatus.Confirmed) {
            throw new ValidationFailedException("Invalid status");
        }
        if (student.getInvitationStatus() == InvitationStatus.Declined) {
            throw new ValidationFailedException("Invalid status");
        }

        student.setInvitationStatus(InvitationStatus.Declined);
        return saveInstance(student);
    }



    public void addStudentToCompany(long organizationId, String studentEmail) throws ValidationFailedException {
        if (StringUtils.isEmpty(studentEmail)) {
            throw new ValidationFailedException("Missing email");
        }
        Student student = studentService.getStudentByEmail(studentEmail);
        if (student == null) {
            //To-do send invitation email
            MessageTemplate emailTemplate = ApplicationContextProvider.getBean(MessageTemplateService.class).getActiveTemplate(MessageTemplateChannel.EMAIL, MessageTemplateType.NON_STUDENT_ORGANISATION_INVITATION);
            if (emailTemplate != null) {
                String subject = MessageTemplateUtils.resolveLkInvitationMessageTemplate(studentEmail, SessionContext.getLoggedInUser(), emailTemplate.getSubject());
                String body = MessageTemplateUtils.resolveLkInvitationMessageTemplate(studentEmail, SessionContext.getLoggedInUser(), emailTemplate.getBody());
                ApplicationContextProvider.getBean(MailService.class).sendEmail(studentEmail, subject, body);
            }
            return;
        }
        Organisation organisation = ApplicationContextProvider.getBean(OrganisationService.class).getInstanceByID(organizationId);
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
            MessageTemplate emailTemplate = ApplicationContextProvider.getBean(MessageTemplateService.class).getActiveTemplate(MessageTemplateChannel.EMAIL, MessageTemplateType.STUDENT_ORGANISATION_INVITATION);
            if (emailTemplate != null) {
                String subject = MessageTemplateUtils.resolveLkInvitationMessageTemplate(studentEmail, SessionContext.getLoggedInUser(), emailTemplate.getSubject());
                String body = MessageTemplateUtils.resolveLkInvitationMessageTemplate(studentEmail, SessionContext.getLoggedInUser(), emailTemplate.getBody());
                ApplicationContextProvider.getBean(MailService.class).sendEmail(studentEmail, subject, body);
            }
        }
        companyStudentDao.save(organisationStudent);
    }

    public static Search generateSearchTerms(String searchTerm) {

        return CustomSearchUtils.generateSearchTerms(searchTerm, Arrays.asList("student.firstName", "student.lastName", "organisation.title", "organisation.description"));
    }

    @Override
    public boolean isDeletable(OrganisationStudent entity) throws OperationFailedException {
        return true;
    }

    @Override
    public OrganisationStudent saveInstance(OrganisationStudent instance) throws ValidationFailedException, OperationFailedException {
        return null;
    }
}
