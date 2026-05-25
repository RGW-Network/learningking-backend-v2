package com.byaffe.learningking.models.quizes;

import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.models.courses.CourseLecture;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "quiz_lecture_mapping")
public class QuizLectureMapping extends BaseEntity {



    @JsonIncludeProperties({"id", "title"})
    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private CourseLecture lecture;

    @JsonIncludeProperties({"id", "title"})
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private int position=0;
    private int minScore=1;

    @Transient
    public Long getQuizId(){
        return this.getQuiz()!=null? this.getQuiz().getId():null;
    }



}
