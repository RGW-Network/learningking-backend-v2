package com.byaffe.learningking.shared.services;

import com.byaffe.learningking.shared.models.MessageTemplate;
import com.byaffe.learningking.shared.models.MessageTemplateChannel;
import com.byaffe.learningking.shared.models.MessageTemplateRequestDto;
import com.byaffe.learningking.shared.models.MessageTemplateType;
import com.googlecode.genericdao.search.Search;

import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * Handles CRUD operations on the {@link  MessageTemplate}
 */
public interface MessageTemplateService {
    /**
     * Saves a Damage to the database
     * @param dto
     * @return
     */
    MessageTemplate saveInstance(MessageTemplateRequestDto dto) throws ValidationException;
    MessageTemplate getActiveTemplate(MessageTemplateChannel channel, MessageTemplateType templateType);

    /**
     *
     * @param id
     * @throws ValidationException
     */
    void deleteInstance(long id) throws ValidationException;

    /**
     * Gets a list of Damages following a supplied search term, offset and limit
     * @param search
     * @return
     */
    List<MessageTemplate>getInstances(Search search, int offset, int limit);

    long countInstances(Search search);


    MessageTemplate getInstanceById(long id);


}
