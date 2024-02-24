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
@Table(name = "big_details")
public class BigDetails extends BaseEntity {


    @Column(name = "data", columnDefinition = "LONGTEXT")
    private String data;




}
