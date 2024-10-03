package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.quizes.*;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;

@Repository
public class QuizDaoImpl extends BaseDAOImpl<Quiz> implements QuizDao{}

