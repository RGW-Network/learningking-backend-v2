package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.quizes.QuizAttempt;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;

@Repository
public class QuizAttemptDaoImpl extends BaseDAOImpl<QuizAttempt> implements QuizAttemptDao{}
