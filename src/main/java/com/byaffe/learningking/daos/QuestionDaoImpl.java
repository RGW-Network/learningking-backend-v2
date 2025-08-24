package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.quizes.Question;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDaoImpl extends BaseDAOImpl<Question> implements QuestionDao{}
