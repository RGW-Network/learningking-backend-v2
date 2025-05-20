package com.byaffe.learningking.dtos.student;

import lombok.Data;

@Data
public class StudentDashboardDto {

    private Integer totalEnrolledCourses=0;
    private Integer totalActiveCourses=0;
    private Integer totalCompletedCourses=0;

    private Integer enrolledOffShelfCourses=0;
    private Integer activeOffShelfCourses=0;
    private Integer completedOffShelfCourses=0;

    private Integer enrolledCorporateCourses=0;
    private Integer activeCorporateCourses=0;
    private Integer completedCorporateCourses=0;


}
