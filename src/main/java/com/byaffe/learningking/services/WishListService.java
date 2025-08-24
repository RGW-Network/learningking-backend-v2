package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Event;
import com.byaffe.learningking.models.EventAttendance;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.WishList;
import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.shared.security.SessionContext;

/**
 * Responsible for CRUD operations on {@link Event}
 *
 * @author RayGdhrt
 *
 */
public interface WishListService extends GenericService<WishList> {
    public WishList addToWishList(Long courseId);
    public void removeFromWishList(Long wishListId);
    public void clearWishList();
   WishList getByCourse(Long courseId);
    public void removeCourseFromWishList(Long courseId);
}
