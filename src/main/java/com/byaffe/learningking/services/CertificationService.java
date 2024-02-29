package com.byaffe.learningking.services;

import com.byaffe.learningking.models.courses.Certification;
import com.byaffe.learningking.models.courses.CertificationCourse;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseCategory;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

import java.util.List;

;
/**
 * Responsible for CRUD operations on {@link CourseCategory}
 *
 * @author RayGdhrt
 *
 */
public interface CertificationService  extends GenericService<Certification> {

    
    public Certification activate(Certification plan) throws ValidationFailedException;

    public Certification deActivate(Certification plan);
    
     public List<CertificationCourse> getCertificationCourses(Certification certification);

    public CertificationCourse getCertificationCourse(Certification certification, Course course);

    public void delete(CertificationCourse certificationCourse);

    public void saveCertificationCourse(CertificationCourse certificationCourse) throws ValidationFailedException;
   

}
