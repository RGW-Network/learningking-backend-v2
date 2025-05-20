package com.byaffe.learningking.dtos.articles;

import com.byaffe.learningking.config.UtcLocalDateTimeDeserializer;
import com.byaffe.learningking.models.EventLocationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.TimeZone;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = UtcLocalDateTimeDeserializer.class)
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = UtcLocalDateTimeDeserializer.class)
    private LocalDateTime endDate;
    private Boolean featured=false;
    private Boolean isPaidFor=false;
    private Double originalPrice=0.0;
    private Double discountedPrice=0.0;
    private Long maximumAttendees;
private Set<Long> speakerIds;


}
