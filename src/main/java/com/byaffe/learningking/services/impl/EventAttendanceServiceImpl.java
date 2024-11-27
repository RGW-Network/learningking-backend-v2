package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.dtos.articles.EventRequestDTO;
import com.byaffe.learningking.models.*;
import com.byaffe.learningking.models.courses.Course;
import com.byaffe.learningking.models.courses.CourseEnrollment;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.models.payments.AggregatorTransaction;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class EventAttendanceServiceImpl extends GenericServiceImpl<EventAttendance> implements EventAttendanceService {

    @Autowired
    EventService eventService;


    @Override
    public EventAttendance saveInstance(EventAttendance eventAttendance) throws ValidationFailedException {

        if (eventAttendance.getEvent() == null) {
            throw new ValidationFailedException("Missing event");
        }

        if (eventAttendance.getStudent() == null) {
            throw new ValidationFailedException("Missing student");
        }

        int currentAttendances = countAttendances(eventAttendance.getEventId());
        if (currentAttendances >= eventAttendance.getEvent().getMaximumAttendees()) {
           // throw new ValidationFailedException("Sorry, the attendance list is full");
        }
        EventAttendance exists = getAttendance(eventAttendance.getEvent(), eventAttendance.getStudent());
        if (exists != null) {
            throw new ValidationFailedException("event attendance already exists");
        }
        eventAttendance = super.save(eventAttendance);


        return eventAttendance;

    }

    @Override
    public int countInstances(Search search) {
        return super.count(search);
    }

    @Override
    public void deleteInstance(EventAttendance event) {
        event.setRecordStatus(RecordStatus.DELETED);
        super.save(event);

    }


    @Override
    public List<EventAttendance> getInstances(Search search, int offset, int limit) {
        if (search == null) {
            search = new Search();
        }
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return super.search(search);
    }

    @Override
    public EventAttendance getInstanceByID(Long event_id) {
        return super.getInstanceByID(event_id);
    }

    public EventAttendance getAttendance(Event event, Student student) {
        return super.searchUnique(new Search().addFilterEqual("event", event).addFilterEqual("student", student).addFilterIn("status", Arrays.asList(EventAttendanceStatus.ATTENDING, EventAttendanceStatus.COMPLETED)));
    }

    public int countAttendances(long eventId) {
        return countInstances(new Search().addFilterEqual("event.id", eventId).addFilterEqual("recordStatus", RecordStatus.ACTIVE).addFilterIn("status", Arrays.asList(EventAttendanceStatus.ATTENDING, EventAttendanceStatus.COMPLETED)));
    }

    @Override
    public EventAttendance attendFreeEvent(long eventId, long studentId) {
        Event event = eventService.getById(eventId);
        if (event.getIsPaidFor()) {
            throw new ValidationFailedException("This event requires payment");
        }
        Student student = ApplicationContextProvider.getBean(StudentService.class).getStudentById(studentId);
        EventAttendance eventAttendance = new EventAttendance();
        eventAttendance.setStatus(EventAttendanceStatus.ATTENDING);
        eventAttendance.setStudent(student);
        eventAttendance.setEvent(event);
        return saveInstance(eventAttendance);
    }

    @Override
    public EventAttendance attendPaidEvent(AggregatorTransaction aggregatorTransaction) {
        if (aggregatorTransaction == null || !aggregatorTransaction.getStatus().equals(TransactionStatus.SUCCESSFUL)) {
            return null;
        }
        Event event = eventService.getById(aggregatorTransaction.getReferenceRecordId());
        Student student = aggregatorTransaction.getStudent();
        EventAttendance eventAttendance = new EventAttendance();
        eventAttendance.setStatus(EventAttendanceStatus.ATTENDING);
        eventAttendance.setStudent(student);
        eventAttendance.setEvent(event);
        eventAttendance.setAggregatorTransaction(aggregatorTransaction);
        return saveInstance(eventAttendance);

    }

    @Override
    public EventAttendance cancel(long eventId, String notes) throws ValidationFailedException {
        EventAttendance event = getInstanceByID(eventId);
        event.setStatus(EventAttendanceStatus.CANCELLED);
        event.setNotes(notes);
        return super.save(event);

    }

    @Override
    public void validateEventAttendance(Event event, Student student) {
       // if (event.isFull()) throw new ValidationFailedException("Event Attendance List is full");
        if (!event.getStatus().equals(EventStatus.UPCOMING))
            throw new ValidationFailedException("Event is already " + event.getStatus().getUiName());
        if (!event.getPublicationStatus().equals(PublicationStatus.ACTIVE))
            throw new ValidationFailedException("Event is not published");
       /// if (!event.getIsPaidFor()) throw new ValidationFailedException("This Event isn't paid for");
        EventAttendance eventAttendance = getAttendance(event, student);
        if (eventAttendance != null) throw new ValidationFailedException("User aldready has an existing attendance");
    }


    public static Search generateSearchTerms(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("event.title",
                        "event.description"));

        return search;
    }


    @Override
    public boolean isDeletable(EventAttendance entity) throws OperationFailedException {
        return true; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
