package com.byaffe.learningking.dtos;

import lombok.Data;

@Data
public class DashboardDto {
    private Integer usersSignedUp=0;
    private Integer publishedCourses=0;
    private Double totalCourses=0.0;
    private Integer totalEnrollments=0;
    private Integer completedEnrollments=0;
    private Integer activeEnrollments=0;
    private Double monthlyRevenue=0.0;

}
