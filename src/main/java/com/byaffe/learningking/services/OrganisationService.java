package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.student.CompanyRequestDTO;
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
public interface OrganisationService extends GenericService<Organisation> {

    
    Organisation activate(Organisation plan) throws ValidationFailedException;
    void addStudentToCompany(long organizationId, String studentEmail) throws ValidationFailedException ;
    int countCompanyStudentInstances(Search arg0) ;

    Organisation deActivate(Organisation plan);
    Organisation saveOrganisation(CompanyRequestDTO dto);


    
     List<OrganisationStudent> getCompanyStudents(Search search, int offset, int limit);

    OrganisationStudent getCompanyStudent(Organisation organisation, Student student);

    void delete(OrganisationStudent organisationStudent);

    OrganisationStudent deActivate(OrganisationStudent plan);


    OrganisationStudent getOrganisationStudentById(long organisationStudentId);
}
