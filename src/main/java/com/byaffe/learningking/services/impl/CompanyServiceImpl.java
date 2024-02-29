package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.NotificationBuilder;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.CompanyCourseDao;
import com.byaffe.learningking.services.CompanyMemberDao;
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
    CompanyMemberDao companyMemberDao;

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

            ApplicationContextProvider.getBean(NotificationService.class).sendNotificationsToAllMembers(
                    new NotificationBuilder()
                            .setTitle("New Company added")
                            .setDescription(plan.getName())
                            .setImageUrl("")
                            .setFmsTopicName("")
                            .setDestinationActivity(NotificationDestinationActivity.DASHBOARD)
                            .setDestinationInstanceId(plan.getId())
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

        if (existsOnCompany != null && !existsOnCompany.getId().equalsIgnoreCase(companyCourse.getId())) {
            throw new ValidationFailedException("Course Exists on this Company");
        }
       
        companyCourseDao.save(companyCourse);
    }

   
    @Override
    public boolean isDeletable(Company entity) throws OperationFailedException {
        return true;
    }
    
     @Override
    public CompanyMember activate(CompanyMember plan) throws ValidationFailedException {
        plan.setPublicationStatus(PublicationStatus.ACTIVE);

        CompanyMember savedDevotionPlan = companyMemberDao.save(plan);
        try {

            ApplicationContextProvider.getBean(NotificationService.class).sendNotificationsToAllMembers(
                    new NotificationBuilder()
                            .setTitle("New Company added")
                            .setDescription(plan.getMember().composeFullName())
                            .setImageUrl("")
                            .setFmsTopicName("")
                            .setDestinationActivity(NotificationDestinationActivity.DASHBOARD)
                            .setDestinationInstanceId(plan.getId())
                            .build()
                    );

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return savedDevotionPlan;
    }

    @Override
    public CompanyMember deActivate(CompanyMember plan) {
        plan.setPublicationStatus(PublicationStatus.INACTIVE);
        return companyMemberDao.save(plan);
    }

    
    @Override
      public List<CompanyMember> getCompanyMembers(Search search, int offset, int limit) {
       search.setMaxResults(limit);
       search.setFirstResult(offset);

        return companyMemberDao.search(search);
    }

    @Override
    public CompanyMember getCompanyMember(Company company, Member member) {
        Search search = new Search();
        search.addFilterEqual("company", company);
        search.addFilterEqual("member", member);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return  companyMemberDao.searchUnique(search);
    }

    @Override
    public void delete(CompanyMember companyCourse) {

        companyCourse.setRecordStatus(RecordStatus.DELETED);

        companyMemberDao.save(companyCourse);
    }

    @Override
    public void saveCompanyMember(CompanyMember companyCourse) throws ValidationFailedException {
        if (companyCourse.getMember() == null) {
            throw new ValidationFailedException("Missing member account");

        }

        if (companyCourse.getCompany() == null) {
            throw new ValidationFailedException("Missing Company");

        }

        CompanyMember existsOnCompany = getCompanyMember(companyCourse.getCompany(), companyCourse.getMember());

        if (existsOnCompany != null && !existsOnCompany.getId().equalsIgnoreCase(companyCourse.getId())) {
            throw new ValidationFailedException("member exists on this company");
        }
       
        companyMemberDao.save(companyCourse);
    }


}
