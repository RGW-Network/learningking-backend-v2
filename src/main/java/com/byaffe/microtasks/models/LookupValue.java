package com.byaffe.microtasks.models;

import com.byaffe.microtasks.shared.models.BaseEntity;
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




}
