package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.*;
import com.byaffe.learningking.dtos.courses.OrganisationGroupRequestDTO;
import com.byaffe.learningking.models.courses.*;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class OrganisationGroupServiceImpl implements OrganisationGroupService {

    @Autowired
    OrganisationGroupDao organisationGroupDao;
    @Autowired
    OrganisationGroupCourseDao organisationGroupCourseDao;

    @Autowired
    OrganisationGroupStudentDao organisationGroupStudentDao;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public OrganisationGroup createGroup(OrganisationGroupRequestDTO dto) {
        if (StringUtils.isEmpty(dto.getName())) {
            throw new ValidationFailedException("Name is required");
        }
        OrganisationGroup group = new OrganisationGroup();
        if (dto.getId() != null) {
            group = getGroupById(dto.getId());
        }
        Organisation organisation = ApplicationContextProvider.getBean(OrganisationService.class).getInstanceByID(dto.getOrganisationId());
        modelMapper.map(dto, group);
        group.setOrganisation(organisation);
        return organisationGroupDao.save(group);
    }

    @Override
    public List<OrganisationGroup> getGroups(Search search, int offset, int limit) {
        return organisationGroupDao.search(search.setFirstResult(offset).setMaxResults(limit));
    }

    public OrganisationGroup getGroupById(long id) {
        return organisationGroupDao.findById(id).orElseThrow(() -> new ValidationFailedException("Group With Id not found"));
    }

    public OrganisationGroup getInstanceById(Long id) {
        return organisationGroupDao.findById(id).orElseThrow(() -> new ValidationFailedException("Group With Id not found"));
    }

    @Override
    public void deleteGroup(Long id) {
        organisationGroupDao.removeById(id);
    }

    public OrganisationGroupStudent getGroupStudentById(long id) {
        return organisationGroupStudentDao.findById(id).orElseThrow(() -> new ValidationFailedException("Group Student With Id not found"));
    }

    @Override
    public OrganisationGroupStudent addGroupStudent(long groupId, long organisationStudentId) throws ValidationFailedException {
        OrganisationStudent organisationStudent = ApplicationContextProvider.getBean(OrganisationStudentService.class).getInstanceByID(organisationStudentId);
        OrganisationGroup organisationGroup = getGroupById(groupId);
        OrganisationGroupStudent saved = addGroupStudent(organisationGroup, organisationStudent);


        return saved;
    }

    private OrganisationGroupStudent addGroupStudent(OrganisationGroup group, OrganisationStudent organisationStudent) throws ValidationFailedException {
        OrganisationGroupStudent exists = organisationGroupStudentDao.searchUnique(new Search().addFilterEqual("organisationStudent", organisationStudent).addFilterEqual("organisationGroup", group));
        if (exists == null) {
            OrganisationGroupStudent groupStudent = new OrganisationGroupStudent();
            groupStudent.setOrganisationGroup(group);
            groupStudent.setOrganisationStudent(organisationStudent);

            exists = organisationGroupStudentDao.save(groupStudent);

            group.incrementStudentCount();
            organisationGroupDao.save(group);
        }

        return exists;
    }

    @Override
    public OrganisationGroup addAllStudentsToGroup(long groupId) throws ValidationFailedException {
        OrganisationGroup group = getGroupById(groupId);
        List<OrganisationStudent> students = ApplicationContextProvider.getBean(OrganisationStudentService.class).getInstances(new Search().addFilterEqual("organisation.id", group.getOrganisation().getId()), 0, 0);
        for (OrganisationStudent student : students) {
            addGroupStudent(group, student);
        }
        return group;
    }

    @Override
    public List<OrganisationGroupStudent> getGroupStudents(Search search, int offset, int limit) {
        return organisationGroupStudentDao.search(search.setFirstResult(offset).setMaxResults(limit));
    }

    @Override
    public int countGroupStudents(Search search) {
        return organisationGroupStudentDao.count(search);
    }

    @Override
    public void deleteGroupStudent(Long id) {
        OrganisationGroupStudent groupStudent = organisationGroupStudentDao.findById(id).orElseThrow(() -> new ValidationFailedException("Student Not Found with Supplied Id"));
        groupStudent.setRecordStatus(RecordStatus.DELETED);
        organisationGroupStudentDao.save(groupStudent);
        OrganisationGroup group=groupStudent.getOrganisationGroup();
        group.decrementStudentCount();
        organisationGroupDao.save(group);
    }

    @Override
    public OrganisationGroupCourse addGroupCourse(long groupId, long courseId) throws ValidationFailedException {
        OrganisationGroupCourse exists = organisationGroupCourseDao.searchUnique(new Search().addFilterEqual("course.id", courseId).addFilterEqual("organisationGroup.id", groupId).setMaxResults(1));
        if (exists == null) {
            OrganisationGroupCourse groupCourse = new OrganisationGroupCourse();
            groupCourse.setCourse(ApplicationContextProvider.getBean(CourseService.class).getInstanceByID(courseId));
            groupCourse.setOrganisationGroup(getGroupById(groupId));
            return organisationGroupCourseDao.save(groupCourse);
        }
        return exists;
    }

    @Override
    public List<OrganisationGroupCourse> getGroupCourses(Search search, int offset, int limit) {
        return organisationGroupCourseDao.search(search.setFirstResult(offset).setMaxResults(limit));

    }

    @Override
    public void deleteGroupCourse(Long id) {
        organisationGroupCourseDao.removeById(id);

    }


    public static Search generateSearchTermsForStudent(String searchTerm) {

        return CustomSearchUtils.generateSearchTerms(searchTerm, Arrays.asList("organisationStudent.student.firstName", "student.student.lastName"));
    }

    public static Search generateSearchTermsForGroup(String searchTerm) {

        return CustomSearchUtils.generateSearchTerms(searchTerm, Arrays.asList("name", "description"));
    }

    public static Search generateSearchTermsForCourse(String searchTerm) {

        return CustomSearchUtils.generateSearchTerms(searchTerm, Arrays.asList("course.title"));
    }
}
