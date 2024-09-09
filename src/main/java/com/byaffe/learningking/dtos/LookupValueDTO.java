package com.byaffe.learningking.dtos;

import com.byaffe.learningking.models.LookupType;
import com.byaffe.learningking.models.LookupValue;
import com.byaffe.learningking.shared.api.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LookupValueDTO extends BaseDTO {
private long id;

    private String typeName;
    private LookupType type;
    private String value;
    private String description;
    private String imageUrl;

    public LookupValueDTO(LookupType type, String value) {
        this.typeName = type.name();
        this.type=type;
        this.value = value;
    }

    public LookupValue toDBModel(){
       return new ModelMapper().map(this,LookupValue.class);
    }
    public static LookupValueDTO fromDBModel(LookupValue dbModel){
        LookupValueDTO dto=new LookupValueDTO();
        dto.setType(dbModel.getType());
        dto.setTypeName(dbModel.getType().name());
        dto.setValue(dbModel.getValue());
        dto.setImageUrl(dbModel.getImageUrl());
        dto.setDescription(dbModel.getDescription());

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
