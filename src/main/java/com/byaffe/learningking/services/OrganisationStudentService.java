package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.student.CompanyRequestDTO;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.Category;
import com.byaffe.learningking.models.courses.Organisation;
import com.byaffe.learningking.models.courses.OrganisationStudent;
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
public interface OrganisationStudentService extends GenericService<OrganisationStudent> {

    
    void addStudentToCompany(long organizationId, String studentEmail) throws ValidationFailedException ;

    OrganisationStudent getCompanyStudent(Organisation organisation, Student student);

    OrganisationStudent acceptInvitation(Long plan);
    OrganisationStudent declineInvitation(Long plan);


}
