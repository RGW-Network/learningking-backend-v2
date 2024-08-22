package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.EventLocationType;
import com.byaffe.learningking.models.EventStatus;
import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.models.courses.ArticleType;
import com.byaffe.learningking.models.courses.CourseAcademyType;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class EventRequestDTO {
    private Long id;
    private String title;
    private String description;
    private String coverImageUrl;
    private MultipartFile coverImage;
    private String location;
    private Long categoryId;
    private EventLocationType locationType = EventLocationType.PHYSICAL;
    private String whatYouWillGain;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean featured=false;
    private boolean isPaidFor=false;
    private Double originalPrice=0.0;
    private Double discountedPrice=0.0;
    private Long maximumAttendees;



}
