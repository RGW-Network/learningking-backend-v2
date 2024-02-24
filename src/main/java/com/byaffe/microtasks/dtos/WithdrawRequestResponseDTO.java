package com.byaffe.microtasks.dtos;

import com.byaffe.microtasks.models.Task;
import com.byaffe.microtasks.models.WithdrawRequest;
import com.byaffe.microtasks.models.WithdrawRequestStatus;
import com.byaffe.microtasks.shared.api.BaseDTO;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
public class WithdrawRequestResponseDTO extends BaseDTO {
    private Long id;
    private Double amount;
    private LocalDateTime dateCompleted;
    private String statusName;
    private Integer statusId;
    private Double systemTax;
    private Double netAmountPayable;
    public static WithdrawRequestResponseDTO fromModel(WithdrawRequest dbModel){
        WithdrawRequestResponseDTO dto=new WithdrawRequestResponseDTO();
        dto.setId(dbModel.getId());
dto.setSystemTax(dbModel.getSystemTax());
dto.setNetAmountPayable(dbModel.getNetAmountPayable());
        dto.setAmount(dbModel.getAmountRequested());
        dto.setDateCompleted(dbModel.getDateCompleted());
        dto.setStatusId(dbModel.getStatus().getId());
        dto.setStatusName(dbModel.getStatus().getUiName());
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
