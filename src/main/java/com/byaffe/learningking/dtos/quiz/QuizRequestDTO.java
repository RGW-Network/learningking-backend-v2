package com.byaffe.learningking.dtos.quiz;

import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.models.quizes.Question;
import com.byaffe.learningking.shared.api.BaseDTO;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Data
public class QuizRequestDTO {
    public Long id;
    private String title;
    private String description;
    private Long courseLectureId;
    private List<QuizQuestionRequestDTO> questions;

}
