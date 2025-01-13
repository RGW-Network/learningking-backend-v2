package com.byaffe.learningking.dtos;

import lombok.Data;

@Data
public class DashboardDto {
    private Integer usersSignedUp=0;
    private Integer publishedCourses=0;
    private Double completionRate=0.0;
    private Integer activeEnrollments=0;

}
