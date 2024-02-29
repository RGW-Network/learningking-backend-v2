package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.NotificationBuilder;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.courses.Certification;
import com.byaffe.learningking.models.courses.CertificationCourse;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.services.CertificationCourseDao;
import com.byaffe.learningking.services.CertificationService;
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
public class CertificationServiceImpl extends GenericServiceImpl<Certification> implements CertificationService {

    @Autowired
    CertificationCourseDao certificationCourseDao;

    @Override
    public Certification saveInstance(Certification instance) throws ValidationFailedException, OperationFailedException {

        if (StringUtils.isBlank(instance.getTitle())) {
            throw new ValidationFailedException("Missing Title");
        }

        if (StringUtils.isBlank(instance.getDescription())) {
            throw new ValidationFailedException("Missing Description");
        }

        return super.merge(instance);

    }

    @Override
    public List<Certification> getInstances(Search arg0, int arg1, int arg2) {
     return super.getInstances(arg0, arg1, arg2); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public int countInstances(Search arg0) {
        return super.countInstances(arg0); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
  
    }

    @Override
    public Certification activate(Certification plan) throws ValidationFailedException {
        plan.setPublicationStatus(PublicationStatus.ACTIVE);

        Certification savedDevotionPlan = super.save(plan);
        try {

            ApplicationContextProvider.getBean(NotificationService.class).sendNotificationsToAllMembers(
                    new NotificationBuilder()
                            .setTitle("New Certification added")
                            .setDescription(plan.getTitle())
                            .setImageUrl("")
                            .setFmsTopicName("")
                            .setDestinationActivity(NotificationDestinationActivity.DASHBOARD)
                            .setDestinationInstanceId(String.valueOf(plan.getId()))
                            .build());

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return savedDevotionPlan;
    }

    @Override
    public Certification deActivate(Certification plan) {
        plan.setPublicationStatus(PublicationStatus.INACTIVE);
        return super.save(plan);
    }

    public List<CertificationCourse> getCertificationCourses(Certification certification) {
        Search search = new Search();
        search.addFilterEqual("certification", certification);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return certificationCourseDao.search(search);
    }

    @Override
    public CertificationCourse getCertificationCourse(Certification certification, Course course) {
        Search search = new Search();
        search.addFilterEqual("certification", certification);
        search.addFilterEqual("course", course);
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        return (CertificationCourse) certificationCourseDao.searchUnique(search);
    }

    @Override
    public void delete(CertificationCourse certificationCourse) {

        certificationCourse.setRecordStatus(RecordStatus.DELETED);

        certificationCourseDao.save(certificationCourse);
    }

    @Override
    public void saveCertificationCourse(CertificationCourse certificationCourse) throws ValidationFailedException {
        if (certificationCourse.getCourse() == null) {
            throw new ValidationFailedException("Missing Course");

        }

        if (certificationCourse.getCertification() == null) {
            throw new ValidationFailedException("Missing Certification");

        }

        if (certificationCourse.getPosition() <= 0) {
            throw new ValidationFailedException("Missing Position");

        }
        CertificationCourse existsOnCertification = getCertificationCourse(certificationCourse.getCertification(), certificationCourse.getCourse());

        if (existsOnCertification != null && !existsOnCertification.getId().equals(certificationCourse.getId())) {
            throw new ValidationFailedException("Course Exists on this Certification");
        }
       
        certificationCourseDao.save(certificationCourse);
    }



    @Override
    public boolean isDeletable(Certification entity) throws OperationFailedException {
        return true;
    }

}
