package com.byaffe.learningking.models;

import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@NoArgsConstructor
@Entity
@Table(name = "video_records")
public class VideoRecord extends BaseEntity {

    private String vimeoId;
    @Column(name = "upload_link",columnDefinition = "TEXT")
    private String uploadLink;
    private String title;
    private String status; // uploaded, processing, ready

    private String thumbnailUrl;

    @Transient
    public String getUri(){
        return "https://vimeo.com/"+vimeoId;
    }
}
