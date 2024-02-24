package com.byaffe.microtasks.dtos;

import lombok.Data;

@Data
public class TaskExecutionRequestDTO {
    private Long taskId;
    private String details;
    private String confirmationToken;
    private String confirmationAttachmentUrl;

}
