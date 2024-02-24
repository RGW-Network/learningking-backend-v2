package com.byaffe.microtasks.dtos;

import com.byaffe.microtasks.models.Task;
import com.byaffe.microtasks.models.TaskDoer;
import com.byaffe.microtasks.models.TaskExecution;
import com.byaffe.microtasks.models.TaskExecutionStatus;
import com.byaffe.microtasks.shared.api.BaseDTO;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
public class TaskExecutionResponseDTO extends BaseDTO {
    private Long id;
    private Long taskId;
    private String taskName;
    private String details;
    private String confirmationToken;
    private String confirmationAttachmentUrl;
    private Double amountPaid;
    private LocalDateTime dateCompleted;
    private String statusName;
    private Integer statusId;
    private String rejectionReason;

    public static TaskExecutionResponseDTO fromModel(TaskExecution dbModel,boolean fullModel){
        TaskExecutionResponseDTO dto=new TaskExecutionResponseDTO();
        dto.setTaskId(dbModel.getTask().getId());
        dto.setTaskName(dbModel.getTask().getName());
        dto.setStatusId(dbModel.getStatus().getId());
        dto.setStatusName(dbModel.getStatus().getUiName());
        dto.setAmountPaid(dbModel.getAmountPaid());

        dto.setConfirmationAttachmentUrl(dbModel.getConfirmationAttachmentUrl());
        dto.setConfirmationToken(dbModel.getConfirmationToken());

        if (fullModel&&dbModel.getDetails() != null&& fullModel){
            dto.setDetails(dbModel.getBigDetails().getData());
        }
dto.setRejectionReason(dbModel.getRejectionReason());

        dto.setId(dbModel.getId());
        dto.setSerialNumber(dbModel.getSerialNumber());
        dto.setRecordStatus(dbModel.getRecordStatus().name());
        dto.setCreatedById(dbModel.getCreatedById());
        dto.setCreatedByUsername(dbModel.getCreatedByUsername());
        dto.setChangedById(dbModel.getChangedById());
        dto.setChangedByUserName(dbModel.getChangedByUsername());
        dto.setDateCreated(dbModel.getDateCreated());
        dto.setDateChanged(dbModel.getDateChanged());
        dto.setChangedByFullName(dbModel.getChangedByFullName());
        dto.setCreatedByFullName(dbModel.getCreatedByFullName());
        return dto;
    }

}
