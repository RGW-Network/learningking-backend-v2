package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.models.*;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.services.*;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.security.SessionContext;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class WishListServiceImpl extends GenericServiceImpl<WishList> implements WishListService {

    @Autowired
    CourseService courseService;

    public WishList addToWishList(Long courseId){
        WishList wishList= new WishList();
        wishList.setStudent(SessionContext.getLoggedInStudent());
        wishList.setCourse(courseService.getInstanceByID(courseId));

        return save(wishList);
    }
    public void removeFromWishList(Long wishListId){
        removeById(wishListId);
    }
    public void clearWishList(){
        remove(searchByPropertyEqual("student", SessionContext.getLoggedInStudent()).toArray(new WishList[0]));
    }

    @Override
    public boolean isDeletable(WishList entity) throws OperationFailedException {
        return false;
    }

    @Override
    public WishList saveInstance(WishList instance) throws ValidationFailedException, OperationFailedException {
        return save(instance);
    }
}
