package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.models.*;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.PermissionDeniedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.google.gson.Gson;
import com.googlecode.genericdao.search.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class WishListServiceImpl extends GenericServiceImpl<WishList> implements WishListService {

    private static final Logger log = LoggerFactory.getLogger(WishListServiceImpl.class);
    @Autowired
    CourseService courseService;

    public WishList addToWishList(Long courseId) {
        Student loggedInStudent = SessionContext.getLoggedInStudent();
        if (loggedInStudent == null) throw new PermissionDeniedException();
        WishList wishList = new WishList();
        WishList exists = getByCourse(courseId);
        if (exists != null) {
            return exists;
        }
        wishList.setStudent(SessionContext.getLoggedInStudent());
        wishList.setCourse(courseService.getInstanceByID(courseId));
        wishList = super.save(wishList);
//log.info(new Gson().toJson(wishList));
        return wishList;
    }

    public void removeFromWishList(Long wishListId) {
        removeById(wishListId);
    }

    public void removeCourseFromWishList(Long courseId) {
        WishList wishList = getByCourse(courseId);
        remove(wishList);
    }

    public void clearWishList() {
        remove(searchByPropertyEqual("student", SessionContext.getLoggedInStudent()).toArray(new WishList[0]));
    }

    @Override
    public WishList getByCourse(Long courseId) {
        Student loggedInStudent = SessionContext.getLoggedInStudent();
        if (loggedInStudent == null) return null;
        return searchUnique(new Search()
                .addFilterEqual("course.id", courseId)
                .addFilterEqual("student.id", SessionContext.getLoggedInStudent().getId())
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE).setMaxResults(1));
    }

    @Override
    public boolean isDeletable(WishList entity) throws OperationFailedException {
        return false;
    }

    @Override
    public List<String> getStringFilterFields() {
        return Collections.emptyList();
    }

    @Override
    public WishList saveInstance(WishList instance) throws ValidationFailedException, OperationFailedException {
        return save(instance);
    }
}
