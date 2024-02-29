package com.byaffe.learningking.shared.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public  class BaseDTO {

	private String status;
	private String message;
	private Long createdById;
	private String createdByUsername;
	private String createdByFullName;
	private Long changedById;
	private String changedByUserName;
	private String changedByFullName;
	private LocalDateTime dateCreated;
	private LocalDateTime dateChanged;
	private String recordStatus;
	private String serialNumber;



}
