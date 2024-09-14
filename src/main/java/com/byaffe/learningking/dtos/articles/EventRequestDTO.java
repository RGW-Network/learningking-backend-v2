package com.byaffe.learningking.dtos.articles;

import com.byaffe.learningking.models.EventLocationType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean featured=false;
    private boolean isPaidFor=false;
    private Double originalPrice=0.0;
    private Double discountedPrice=0.0;
    private Long maximumAttendees;



}
