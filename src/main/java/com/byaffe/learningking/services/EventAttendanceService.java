package com.byaffe.learningking.services;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.dtos.articles.EventRequestDTO;
import com.byaffe.learningking.models.Event;
import com.byaffe.learningking.models.EventAttendance;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;

/**
 * Responsible for CRUD operations on {@link Event}
 *
 * @author RayGdhrt
 *
 */
public interface EventAttendanceService extends GenericService<EventAttendance> {

    EventAttendance attendFreeEvent(long eventId,long studentId);
    EventAttendance attendPaidEvent( AggregatorTransaction aggregatorTransaction);
    void bulkAttend( AggregatorTransaction aggregatorTransaction);
     EventAttendance saveInstance(EventAttendance eventAttendance) throws ValidationFailedException ;
    EventAttendance getByUser(long eventId,long studentId);

    EventAttendance cancel(long eventId,String cancellationNotes);
    void validateEventAttendance(Event event,Student student);
    public int countAttendances(long eventId);

}
