package com.byaffe.learningking.dtos;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
public class TaskRequestDTO {
    private Long id;
    private String name;
    private String details;
    private String detailsId;
    private Double costPerExecution;
    private Integer localityId;
    private Integer complexityId;
    private Integer verificationType;
    private Long categoryId;
    private Long requiredExecutions;
    private String autoApprovalRegex;
    private String autoApprovalTokens;

    public Set<String> getAutoApprovalTokenList() {
        if (StringUtils.isNotBlank(this.autoApprovalTokens)) {
            String[] lines = this.autoApprovalTokens.split(System.lineSeparator());
            return new HashSet<>(Arrays.asList(lines));
        }
        return null;
    }


}
