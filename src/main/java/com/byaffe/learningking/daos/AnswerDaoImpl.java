package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.quizes.AnswerOption;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;

@Repository
public class AnswerDaoImpl extends BaseDAOImpl<AnswerOption> implements AnswerDao{}
