package com.byaffe.learningking.dtos.instructor;

import lombok.Data;

@Data
public class InstructorRequestDTO {
    public String emailAddress;
    public String lastName;
    public String firstName;
    public String phoneNumber;
    public Long countryId;



}
