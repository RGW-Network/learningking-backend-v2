package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;

import java.util.List;

;
/**
 * Responsible for CRUD operations on {@link CourseCategory}
 *
 * @author RayGdhrt
 *
 */
public interface CompanyService  extends GenericService<Company> {

    
    public Company activate(Company plan) throws ValidationFailedException;

    public Company deActivate(Company plan);
    
     public List<CompanyCourse> getCompanyCourses(Company company);

    public CompanyCourse getCompanyCourse(Company company, Course course);

    public void delete(CompanyCourse companyCourse);

    public void saveCompanyCourse(CompanyCourse companyCourse) throws ValidationFailedException;
   
    
     public List<CompanyStudent> getCompanyStudents(Search search, int offset, int limit);

    public CompanyStudent getCompanyStudent(Company company, Student student);

    public void delete(CompanyStudent companyStudent);

    public void saveCompanyStudent(CompanyStudent companyStudent) throws ValidationFailedException;
     public CompanyStudent activate(CompanyStudent plan) throws ValidationFailedException ;

    public CompanyStudent deActivate(CompanyStudent plan);
   

}
