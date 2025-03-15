package com.byaffe.learningking.dtos;

import com.byaffe.learningking.models.SubmissionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserSubmissionRequestDto {
    private String subject;
    private String emailAddress;
    private String description;
    private String attachmentUrl;
    private SubmissionType type;
    private String fullName;

    @JsonIgnore
    private String requestDetails;
}
