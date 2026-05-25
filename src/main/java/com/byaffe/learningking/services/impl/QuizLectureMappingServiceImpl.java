package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.NotificationBuilder;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.quizes.QuizLectureMapping;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.byaffe.learningking.utilities.ImageStorageService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class QuizLectureMappingServiceImpl extends GenericServiceImpl<QuizLectureMapping> implements QuizLectureMappingService {

    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    QuizService quizService;

    @Autowired
    CourseLectureService courseLectureService;

    public static Search generateSearchObjectForQuizLectureMappings(String searchTerm) {

   return  new Search();
    }

    @Override
    public QuizLectureMapping saveInstance(QuizLectureMapping plan) throws ValidationFailedException {


        return super.merge(plan);

    }

    @Override
    public int countInstances(Search search) {
        return super.count(search);
    }

    @Override
    public void deleteInstance(QuizLectureMapping plan) {
        plan.setRecordStatus(RecordStatus.DELETED);
        super.save(plan);

    }
 @Override
    public List<QuizLectureMapping> getInstances(Search search, int offset, int limit) {
        if (search == null) {
            search = new Search();
        }
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return super.search(search);
    }


    @Override
    public QuizLectureMapping save(QuizLectureMappingRequestDTO dto) throws ValidationFailedException {
        if (dto.getQuizId() == null) {
            throw new ValidationFailedException("Missing quiz");
        }
        if (dto.getLectureId() == null) {
            throw new ValidationFailedException("Missing quiz");
        }


        QuizLectureMapping model = new QuizLectureMapping();
        if(dto.getId()!=null&&dto.getId()>0){
            model=getInstanceByID(dto.getId());
        }
        QuizLectureMapping existsOnLecture = getMappingByLectureAndQuizIds(dto.getQuizId(),dto.getLectureId());
        if (existsOnLecture != null && !existsOnLecture.getId().equals(dto.getId())) {
            throw new ValidationFailedException("Same quiz already exists on this course!");
        }

        modelMapper.map(dto,model);
        model.setLecture(courseLectureService.getInstanceByID(dto.getLectureId()));
        model.setQuiz(quizService.getById(dto.getQuizId()));
        model= saveInstance(model);

        return model;
    }

    private QuizLectureMapping getMappingByLectureAndQuizIds(Long quizId, Long lectureId) {
     return  searchUnique(new Search().addFilterEqual("quiz.id",quizId).addFilterEqual("lecture.id",lectureId).addFilterEqual("recordStatus", RecordStatus.ACTIVE));
    }

    public QuizLectureMapping getInstanceByID(Long id){
        return  super.findById(id).orElseThrow(()->new ValidationFailedException("Not found"));
    }
    @Override
    public QuizLectureMapping activate(long plan) throws ValidationFailedException {
        QuizLectureMapping quizLectureMapping=getInstanceByID(plan);
        quizLectureMapping.setRecordStatus(RecordStatus.ACTIVE);


        return super.save(quizLectureMapping);
    }

    @Override
    public QuizLectureMapping deActivate(long id) {
        QuizLectureMapping plan=getInstanceByID(id);
        plan.setRecordStatus(RecordStatus.DELETED);
        return super.save(plan);
    }


    public static Search generateSearchTermsForQuizLectureMappings(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("title", "description"));
        return search;
    }


    @Override
    public boolean isDeletable(QuizLectureMapping entity) throws OperationFailedException {
        return true; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
