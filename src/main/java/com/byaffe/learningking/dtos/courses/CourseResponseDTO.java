package com.byaffe.learningking.dtos.courses;

import com.byaffe.learningking.models.courses.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
public class CourseResponseDTO {
    private Long id;
    private String title;
    private String description;
    private int numberOfTopics = 0;
    private String coverImageUrl;
    private String welcomeVideoUrl;
    private String whatYouWillLearn;
    private List<String> tags;
    private String guidelineVideoUrl;
    private String welcomeRemarks;
    private String certificateTemplate;
    private boolean isFeatured;
    private boolean isPaid;
    private float cost;
    private String fullDescription;
   private Set<Testimonial> testimonials;

    private PublicationStatus publicationStatus ;
    private CourseCategory category;
    private CourseOwnerShipType ownershipType;
    private Company company;
    private CourseAcademyType academy;

    private String publicationStatusName =publicationStatus!=null?publicationStatus.getDisplayName():null;
    private Integer publicationStatusId=publicationStatus!=null?publicationStatus.getId() :null;;
    private String ownershipTypeName=ownershipType!=null?ownershipType.getDisplayName():null;;
    private Integer ownershipTypeId=ownershipType!=null?ownershipType.getId():null;;
    private String academyName=academy!=null?academy.getDisplayName():null;;
    private Integer academyId=academy!=null?academy.getId():null;;

}
