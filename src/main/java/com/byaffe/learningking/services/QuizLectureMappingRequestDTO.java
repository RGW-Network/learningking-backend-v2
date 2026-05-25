package com.byaffe.learningking.services;

import com.byaffe.learningking.shared.constants.RecordStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuizLectureMappingRequestDTO {
    private Long id;
    private Long quizId;
    private Long lectureId;
    private int position=0;
    private int minScore=1;
    private RecordStatus recordStatus=RecordStatus.ACTIVE;

}
