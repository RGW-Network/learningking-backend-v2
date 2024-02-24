package com.byaffe.microtasks.dtos;

import com.byaffe.microtasks.models.TaskDoer;
import com.byaffe.microtasks.shared.api.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TaskDoerResponseDTO extends BaseDTO {
private Long id;

    private String nationalIDNumber;

    private String digitalWalletNumber;
    private Integer totalTasksCreated;

    public static TaskDoerResponseDTO fromDBModel(TaskDoer dbModel){
        TaskDoerResponseDTO dto=new TaskDoerResponseDTO();
        dto.setId(dbModel.getId());
        dto.setNationalIDNumber(dbModel.getNationalIDNumber());

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
