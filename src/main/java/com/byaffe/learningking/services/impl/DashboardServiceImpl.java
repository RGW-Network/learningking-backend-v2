package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.daos.CourseDao;
import com.byaffe.learningking.daos.StudentDao;
import com.byaffe.learningking.daos.UserDao;
import com.byaffe.learningking.dtos.DashboardDto;
import com.byaffe.learningking.dtos.articles.ArticleRequestDTO;
import com.byaffe.learningking.models.Article;
import com.byaffe.learningking.models.NotificationBuilder;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.ReadStatus;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class DashboardServiceImpl  implements DashboardService {

    @Lazy
    @Autowired
    StudentDao studentDao;

    @Autowired
    CourseDao courseDao;
@Autowired
CourseEnrollmentService courseEnrollmentService;

@Autowired
DashboardRepository dashboardRepository;

    @Override
    public List<Long> findWeeklySignupCounts() {
        List<Object[]> results = dashboardRepository.findWeeklySignupCounts();
        List<Long> dailyCounts = new ArrayList<>(Collections.nCopies(7, 0L)); // Initialize with 0 for 7 days

        for (Object[] result : results) {
            int dayOfWeek = ((Number) result[0]).intValue(); // 1 (Sunday) to 7 (Saturday)
            long count = ((Number) result[1]).longValue();
            dailyCounts.set(dayOfWeek - 1, count); // Subtract 1 to map to 0-based index
        }

        return dailyCounts;
    }

    @Override
    public DashboardDto getSummaries() {
        DashboardDto dto= new DashboardDto();
        int enrollments=courseEnrollmentService.countInstances(new Search());
        int completions = courseEnrollmentService.countInstances(new Search().addFilterEqual("readStatus", ReadStatus.Completed));
        dto.setUsersSignedUp(studentDao.count(new Search()));
        dto.setActiveEnrollments(courseEnrollmentService.countInstances(new Search()));
        dto.setPublishedCourses(courseDao.count(new Search()));
        dto.setCompletionRate((double) completions / enrollments);
        return dto;
    }

    @Override
    public DashboardDto getSignupOverview() {
        return new DashboardDto();
    }

    @Override
    public DashboardDto getRecentUsers() {
        return new DashboardDto();
    }

    @Override
    public DashboardDto getRecentReviews() {
        return new DashboardDto();
    }

    @Override
    public DashboardDto getMonthlyRevenueSummary() {
        return new DashboardDto();
    }
}
