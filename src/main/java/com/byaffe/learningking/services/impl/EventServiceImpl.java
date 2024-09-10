package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.dtos.courses.EventRequestDTO;
import com.byaffe.learningking.models.Event;
import com.byaffe.learningking.models.NotificationBuilder;
import com.byaffe.learningking.models.NotificationDestinationActivity;
import com.byaffe.learningking.models.courses.PublicationStatus;
import com.byaffe.learningking.services.EventService;
import com.byaffe.learningking.services.CategoryService;
import com.byaffe.learningking.services.NotificationService;
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

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class EventServiceImpl extends GenericServiceImpl<Event> implements EventService {

    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CategoryService categoryService;

    public static Search generateSearchObjectForEvents(String searchTerm) {
    return new Search();
    }

    @Override
    public Event saveInstance(Event event) throws ValidationFailedException {

        if (event.getCategory() == null) {
            throw new ValidationFailedException("Mising category");
        }

        if (StringUtils.isBlank(event.getTitle())) {
            throw new ValidationFailedException("Missing Title");
        }

        if (StringUtils.isBlank(event.getDescription())) {
            throw new ValidationFailedException("Missing Description");
        }

        Event existingWithTitle = getByTitle(event.getTitle());

        if (existingWithTitle != null && !existingWithTitle.getId().equals(event.getId())) {
            throw new ValidationFailedException("An event with the same title already exists!");
        }
        event.setPublicationStatus(PublicationStatus.INACTIVE);

        return super.merge(event);

    }

    @Override
    public int countInstances(Search search) {
        return super.count(search);
    }

    @Override
    public void deleteInstance(Event event) {
        event.setRecordStatus(RecordStatus.DELETED);
        super.save(event);

    }
 @Override
    public List<Event> getInstances(Search search, int offset, int limit) {
        if (search == null) {
            search = new Search();
        }
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return super.search(search);
    }
    @Override
    public Event getInstanceByID(Long event_id) {
        return super.getInstanceByID(event_id);
    }


    @Override
    public Event save(EventRequestDTO dto) throws ValidationFailedException {
        if (dto.getCategoryId() == null) {
            throw new ValidationFailedException("Missing category");
        }

        if (StringUtils.isBlank(dto.getTitle())) {
            throw new ValidationFailedException("Missing Title");
        }

        if (StringUtils.isBlank(dto.getDescription())) {
            throw new ValidationFailedException("Missing Description");
        }

        Event existingWithTitle = getByTitle(dto.getTitle());

        if (existingWithTitle != null && !existingWithTitle.getId().equals(dto.getId())) {
            throw new ValidationFailedException("An event with the same title already exists!");
        }

        Event event=modelMapper.map(dto,Event.class);
        //event.setCategory(categoryService.getInstanceByID(dto.getCategoryId()));
        event= saveInstance(event);

        if(dto.getCoverImage()!=null) {
            String imageUrl=   imageStorageService.uploadImage(dto.getCoverImage(), "events/" + event.getId());
            event.setCoverImageUrl(imageUrl);
            event=super.save(event);
        }

        return event;
    }

    @Override
    public Event activate(long eventId) throws ValidationFailedException {
        Event event=getInstanceByID(eventId);
        event.setPublicationStatus(PublicationStatus.ACTIVE);

        Event savedDevotionEvent = super.save(event);
        try {

            ApplicationContextProvider.getBean(NotificationService.class).sendNotificationsToAllStudents(
                    new NotificationBuilder()
                            .setTitle("New Events added")
                            .setDescription(event.getTitle())
                            .setImageUrl("")
                            .setFmsTopicName("")
                            .setDestinationActivity(NotificationDestinationActivity.DASHBOARD)
                            .setDestinationInstanceId(String.valueOf(event.getId()))
                            .build());

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return savedDevotionEvent;
    }

    @Override
    public Event deActivate(long id) {
        Event event=getInstanceByID(id);
        event.setPublicationStatus(PublicationStatus.INACTIVE);
        return super.save(event);
    }

    @Override
    public Event getByTitle(String eventTitle) {
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addFilterEqual("title", eventTitle);

        return super.searchUnique(search);

    }

    @Override
    public Event getById(Long id) {
        return super.findById(id).orElseThrow(()->new ValidationFailedException("No record Found"));
    }


    public static Search generateSearchTermsForEvents(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("title",
                        "description"));

        return search;
    }


    @Override
    public boolean isDeletable(Event entity) throws OperationFailedException {
        return true; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
