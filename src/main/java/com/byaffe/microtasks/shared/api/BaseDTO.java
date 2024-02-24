package com.byaffe.microtasks.shared.api;

import com.byaffe.microtasks.shared.models.BaseEntity;
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
