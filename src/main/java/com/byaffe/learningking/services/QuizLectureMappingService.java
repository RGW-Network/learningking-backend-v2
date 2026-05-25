package com.byaffe.learningking.services;

import com.byaffe.learningking.models.quizes.QuizLectureMapping;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

/**
 * Responsible for CRUD operations on {@link QuizLectureMapping}
 *
 * @author RayGdhrt
 *
 */
public interface QuizLectureMappingService extends GenericService<QuizLectureMapping> {


    QuizLectureMapping save(QuizLectureMappingRequestDTO dto) throws ValidationFailedException;


    QuizLectureMapping activate(long plan) throws ValidationFailedException;

    /**
     *
     * @param plan
     * @return
     */
    QuizLectureMapping deActivate(long plan);


    

}
