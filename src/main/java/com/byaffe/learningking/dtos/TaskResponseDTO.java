package com.byaffe.learningking.dtos;

import com.byaffe.learningking.models.Task;
import com.byaffe.learningking.shared.api.BaseDTO;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TaskResponseDTO extends BaseDTO {
    private Long id;
    private String name;
    private String details;
    private Double costPerExecution;

    private Integer statusId;
    private String statusName;

    private Integer localityId;
    private String localityName;

    private Integer complexityId;
    private String complexityName;
    private Boolean alreadyAttempted=false;
    private String attemptResponse;
    private String attemptStatus;
    private Integer verificationTypeId;
    private String verificationTypeName;
    private Boolean featured;
    private Long requiredExecutions;
    private Long currentExecutions;
    private String autoApprovalRegex;
    private String attachmentUrl;
    private Set<String> autoApprovalTokenList = new HashSet<>();
    public static TaskResponseDTO fromModel(Task model, boolean fullModel) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        if (fullModel&&model.getBigDetails() != null){
            dto.setDetails(model.getBigDetails().getData());
            System.err.println(">>>>>>>> bigdata id"+model.getBigDetails().getId());
    }
        dto.setAttachmentUrl(model.getAttachmentUrl());
        dto.setAutoApprovalRegex(model.getAutoApprovalRegex());
        dto.setAutoApprovalTokenList(model.getAutoApprovalTokenList());
        dto.setRequiredExecutions(model.getRequiredExecutions());
        dto.setCostPerExecution(model.getCostPerExecution());
        dto.setCurrentExecutions(model.getCurrentExecutions());

        if(model.getVerificationType()!=null) {
            dto.setVerificationTypeId(model.getVerificationType().getId());
            dto.setVerificationTypeName(model.getVerificationType().getUiName());
        }
        if(model.getComplexity()!=null) {
            dto.setComplexityId(model.getComplexity().getId());
            dto.setComplexityName(model.getComplexity().getUiName());
        }

        if(model.getLocality()!=null) {
            dto.setLocalityId(model.getLocality().getId());
            dto.setLocalityName(model.getLocality().getUiName());
        }
        if(model.getStatus()!=null) {
            dto.setStatusId(model.getStatus().getId());
            dto.setStatusName(model.getStatus().getUiName());
        }


        dto.setId(model.getId());
        dto.setSerialNumber(model.getSerialNumber());
        dto.setRecordStatus(model.getRecordStatus().name());
        dto.setCreatedById(model.getCreatedById());
        dto.setCreatedByUsername(model.getCreatedByUsername());
        dto.setChangedById(model.getChangedById());
        dto.setChangedByUserName(model.getChangedByUsername());
        dto.setDateCreated(model.getDateCreated());
        dto.setDateChanged(model.getDateChanged());
        dto.setChangedByFullName(model.getChangedByFullName());
        dto.setCreatedByFullName(model.getCreatedByFullName());
        return dto;
    }

}
