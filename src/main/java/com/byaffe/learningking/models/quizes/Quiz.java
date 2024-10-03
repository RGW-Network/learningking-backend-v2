package com.byaffe.learningking.models.quizes;

import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.shared.models.BaseEntity;
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

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private CourseLecture courseLecture;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Question> questions;

    // Getters and Setters
}
