package com.byaffe.learningking.models.quizes;

import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "quizes")
public class Quiz extends BaseEntity {

    private String title;
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private CourseLecture courseLecture;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Question> questions;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status", nullable = true)
    private PublicationStatus publicationStatus= PublicationStatus.ACTIVE;

    @Transient
    public String getLectureName(){
        return courseLecture.getTitle();
    }
    @Transient
    public Long getLectureId(){
        return courseLecture.getId();
    }

}
