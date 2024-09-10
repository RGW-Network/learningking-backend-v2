package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.CompanyRequestDTO;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.googlecode.genericdao.search.Search;

import java.util.List;

;
/**
 * Responsible for CRUD operations on {@link Category}
 *
 * @author RayGdhrt
 *
 */
public interface CompanyService  extends GenericService<Organisation> {

    
    public Organisation activate(Organisation plan) throws ValidationFailedException;
    public void addStudentToCompany(long organizationId, String studentEmail) throws ValidationFailedException ;

    public Organisation deActivate(Organisation plan);
    public Organisation saveOrganisation(CompanyRequestDTO dto);

    public List<CompanyCourse> getCompanyCourses(Organisation organisation);

    public CompanyCourse getCompanyCourse(Organisation organisation, Course course);

    public void delete(CompanyCourse companyCourse);

    public void saveCompanyCourse(CompanyCourse companyCourse) throws ValidationFailedException;
   
    
     public List<OrganisationStudent> getCompanyStudents(Search search, int offset, int limit);

    public OrganisationStudent getCompanyStudent(Organisation organisation, Student student);

    public void delete(OrganisationStudent organisationStudent);

    public OrganisationStudent deActivate(OrganisationStudent plan);
   

}
