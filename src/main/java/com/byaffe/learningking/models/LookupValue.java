package com.byaffe.learningking.models;

import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "lookup_values")
public class LookupValue extends BaseEntity {

@Enumerated(EnumType.ORDINAL)
@Column(name = "type")
    private LookupType type;
    @Column(name = "value")
    private String value;
    @Column(name = "description")
    private String description;
    @Column(name = "image_url")
    private String imageUrl;

@Transient
public String getTypeName(){
    if(type==null){
        return  null;
    }
    return type.getUiName();
}

}
