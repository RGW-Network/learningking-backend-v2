package com.byaffe.learningking.dtos.quiz;

import com.byaffe.learningking.models.courses.PublicationStatus;
import lombok.Data;

@Data
public class LectureQuizRequestDTO {
    public Long id=0L;
    private Long lectureId;
    private Long quizId;
    private int position;
    private PublicationStatus publicationStatus= PublicationStatus.ACTIVE;

    //nana-6pm

}
