package com.byaffe.learningking.daos;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.shared.dao.BaseDAOImpl;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDaoImpl extends BaseDAOImpl<Student> implements StudentDao{}
