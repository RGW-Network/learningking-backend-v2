package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.NotificationBuilder;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.CompanyCourseDao;
import com.byaffe.learningking.services.CompanyStudentDao;
import com.byaffe.learningking.services.CompanyService;
import com.byaffe.learningking.services.NotificationService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class CompanyServiceImpl extends GenericServiceImpl<Company> implements CompanyService {

    @Autowired
    CompanyCourseDao companyCourseDao;
    
    @Autowired
    CompanyStudentDao companyStudentDao;

    @Override
    public Company saveInstance(Company instance) throws ValidationFailedException, OperationFailedException {

        if (StringUtils.isBlank(instance.getName())) {
            throw new ValidationFailedException("Missing Name");
        }
        
        if (instance.getAreaOfBusiness()==null) {
            throw new ValidationFailedException("Missing Area Of Business");
        }

        if (StringUtils.isBlank(instance.getAbountDetails())) {
            throw new ValidationFailedException("Missing About details");
        }

        return save(instance);

    }

    @Override
    public List<Company> getInstances(Search arg0, int arg1, int arg2) {
     return super.getInstances(arg0, arg1, arg2); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public int countInstances(Search arg0) {
        return super.countInstances(arg0); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
  
    }

    @Override
    public Company activate(Company plan) throws ValidationFailedException {
        plan.setPublicationStatus(PublicationStatus.ACTIVE);

        Company savedDevotionPlan = super.save(plan);
        try {

            ApplicationContextProvider.getBean(NotificationService.class).sendNotificationsToAllStudents(
                    new NotificationBuilder()
                            .setTitle("New Company added")
                            .setDescription(plan.getName())
                            .setImageUrl("")
                            .setFmsTopicName("")
                            .setDestinationActivity(NotificationDestinationActivity.DASHBOARD)
                            .setDestinationInstanceId(String.valueOf(plan.getId()))
                            .build()
                    );

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return savedDevotionPlan;
    }

    @Override
    public Company deActivate(Company plan) {
        plan.setPublicationStatus(PublicationStatus.INACTIVE);
        return super.save(plan);
    }

    public List<CompanyCourse> getCompanyCourses(Company company) {
        Search search = new Search().addSortAsc("position");
        
        search.addFilterEqual("company", company);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return companyCourseDao.search(search);
    }

    @Override
    public CompanyCourse getCompanyCourse(Company company, Course course) {
        Search search = new Search();
        search.addFilterEqual("company", company);
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
    public boolean isDeletable(Company entity) throws OperationFailedException {
        return true;
    }
    
     @Override
    public CompanyStudent activate(CompanyStudent plan) throws ValidationFailedException {
        plan.setPublicationStatus(PublicationStatus.ACTIVE);

        CompanyStudent savedDevotionPlan = companyStudentDao.save(plan);
        try {

            ApplicationContextProvider.getBean(NotificationService.class).sendNotificationsToAllStudents(
                    new NotificationBuilder()
                            .setTitle("New Company added")
                            .setDescription(plan.getStudent().getFullName())
                            .setImageUrl("")
                            .setFmsTopicName("")
                            .setDestinationActivity(NotificationDestinationActivity.DASHBOARD)
                            .setDestinationInstanceId(String.valueOf(plan.getId()))
                            .build()
                    );

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return savedDevotionPlan;
    }

    @Override
    public CompanyStudent deActivate(CompanyStudent plan) {
        plan.setPublicationStatus(PublicationStatus.INACTIVE);
        return companyStudentDao.save(plan);
    }

    
    @Override
      public List<CompanyStudent> getCompanyStudents(Search search, int offset, int limit) {
       search.setMaxResults(limit);
       search.setFirstResult(offset);

        return companyStudentDao.search(search);
    }

    @Override
    public CompanyStudent getCompanyStudent(Company company, Student student) {
        Search search = new Search();
        search.addFilterEqual("company", company);
        search.addFilterEqual("member", student);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return  companyStudentDao.searchUnique(search);
    }

    @Override
    public void delete(CompanyStudent companyCourse) {

        companyCourse.setRecordStatus(RecordStatus.DELETED);

        companyStudentDao.save(companyCourse);
    }

    @Override
    public void saveCompanyStudent(CompanyStudent companyCourse) throws ValidationFailedException {
        if (companyCourse.getStudent() == null) {
            throw new ValidationFailedException("Missing member account");

        }

        if (companyCourse.getCompany() == null) {
            throw new ValidationFailedException("Missing Company");

        }

        CompanyStudent existsOnCompany = getCompanyStudent(companyCourse.getCompany(), companyCourse.getStudent());

        if (existsOnCompany != null && !existsOnCompany.getId().equals(companyCourse.getId())) {
            throw new ValidationFailedException("member exists on this company");
        }
       
        companyStudentDao.save(companyCourse);
    }


}
