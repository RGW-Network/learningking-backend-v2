package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.courses.OrganisationGroupRequestDTO;
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
 */
public interface OrganisationGroupService {
    OrganisationGroup createGroup(OrganisationGroupRequestDTO dto);

    List<OrganisationGroup> getGroups(Search search, int offset, int limit);
    OrganisationGroup getInstanceById(Long id);

    void deleteGroup(Long id);

    OrganisationGroupStudent addGroupStudent(long groupId, long organisationStudentId) throws ValidationFailedException;
    OrganisationGroup addAllStudentsToGroup(long groupId) throws ValidationFailedException;

    List<OrganisationGroupStudent> getGroupStudents(Search search, int offset, int limit);

    void deleteGroupStudent(Long id);

    OrganisationGroupCourse addGroupCourse(long groupId, long courseId) throws ValidationFailedException;

    List<OrganisationGroupCourse> getGroupCourses(Search search, int offset, int limit);

    void deleteGroupCourse(Long id);


}
