package com.byaffe.learningking.models;

import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "video_records")
public class VideoRecord extends BaseEntity {

    private String vimeoId;
    private String uploadLink;
    private String title;
    private String status; // uploaded, processing, ready

    private String thumbnailUrl;
}
