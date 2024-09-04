package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.dtos.CompanyRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.NotificationBuilder;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class CompanyServiceImpl extends GenericServiceImpl<Organisation> implements CompanyService {

    @Autowired
    CompanyCourseDao companyCourseDao;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CompanyStudentDao companyStudentDao;

    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    LookupValueService lookupValueService;
    @Autowired
    StudentService studentService;


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
        article.setCountry(lookupValueService.getCountryById(dto.getCountryId()));
        article = saveInstance(article);

        if (dto.getCoverImage() != null) {
            String imageUrl = imageStorageService.uploadImage(dto.getCoverImage(), "companies/" + article.getId());
            article.setCoverImageUrl(imageUrl);
            article = super.save(article);
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
    public Organisation activate(Organisation plan) throws ValidationFailedException {
        plan.setPublicationStatus(PublicationStatus.ACTIVE);

        return super.save(plan);
    }

    @Override
    public Organisation deActivate(Organisation plan) {
        plan.setPublicationStatus(PublicationStatus.INACTIVE);
        return super.save(plan);
    }

    public List<CompanyCourse> getCompanyCourses(Organisation organisation) {
        Search search = new Search().addSortAsc("position");

        search.addFilterEqual("company", organisation);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return companyCourseDao.search(search);
    }

    @Override
    public CompanyCourse getCompanyCourse(Organisation organisation, Course course) {
        Search search = new Search();
        search.addFilterEqual("company", organisation);
        search.addFilterEqual("course", course);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return (CompanyCourse) companyCourseDao.searchUnique(search);
    }

    @Override
    public void delete(CompanyCourse companyCourse) {

        companyCourse.setRecordStatus(RecordStatus.DELETED);

        companyCourseDao.save(companyCourse);
    }

    @Override
    public void saveCompanyCourse(CompanyCourse companyCourse) throws ValidationFailedException {
        if (companyCourse.getCourse() == null) {
            throw new ValidationFailedException("Missing Course");

        }

        if (companyCourse.getCompany() == null) {
            throw new ValidationFailedException("Missing Company");

        }

        if (companyCourse.getPosition() <= 0) {
            throw new ValidationFailedException("Missing Position");

        }
        CompanyCourse existsOnCompany = getCompanyCourse(companyCourse.getCompany(), companyCourse.getCourse());

        if (existsOnCompany != null && !existsOnCompany.getId().equals(companyCourse.getId())) {
            throw new ValidationFailedException("Course Exists on this Company");
        }

        companyCourseDao.save(companyCourse);
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
    public List<OrganisationStudent> getCompanyStudents(Search search, int offset, int limit) {
        search.setMaxResults(limit);
        search.setFirstResult(offset);

        return companyStudentDao.search(search);
    }

    @Override
    public OrganisationStudent getCompanyStudent(Organisation organisation, Student student) {
        Search search = new Search();
        search.addFilterEqual("company", organisation);
        search.addFilterEqual("member", student);
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
            throw new ValidationFailedException("User with email not found");

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

        companyStudentDao.save(organisationStudent);
    }


}
