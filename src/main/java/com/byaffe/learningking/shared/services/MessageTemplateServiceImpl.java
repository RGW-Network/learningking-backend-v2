package com.byaffe.learningking.shared.services;

import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.dao.MessageTemplateDao;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.models.MessageTemplate;
import com.byaffe.learningking.shared.models.MessageTemplateRequestDto;
import com.byaffe.learningking.shared.models.MessageTemplateChannel;
import com.byaffe.learningking.shared.utils.CustomSearchUtils;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class MessageTemplateServiceImpl implements MessageTemplateService {
    @Autowired
    MessageTemplateDao messageTemplateDao;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public MessageTemplate saveInstance(MessageTemplateRequestDto dto) {
        if ((StringUtils.isEmpty( dto.getBody()))) {
            throw new ValidationFailedException("Body is required");
        }
        if (dto.getType()==null) {
            throw new ValidationFailedException("Missing Type");
        }
        if (dto.getType().equals(MessageTemplateChannel.EMAIL) && (StringUtils.isEmpty( dto.getSubject()))) {
            throw new ValidationFailedException("Subject is required for email templates");
        }

        MessageTemplate messageTemplate= new MessageTemplate();

        if(dto.getId()!=null&&dto.getId()>0){
            messageTemplate= getInstanceById(dto.getId());
        }
        modelMapper.map(dto,messageTemplate);
        return messageTemplateDao.save(messageTemplate);
    }



    @Override
    public void deleteInstance(long id) throws ValidationException {
        if (id == 0) {
            throw new ValidationException("Missing Id");
        }
        MessageTemplate existsWithId = getInstanceById(id);
        if (existsWithId != null) {
            existsWithId.setRecordStatus(RecordStatus.DELETED);
            messageTemplateDao.save(existsWithId);
        }

    }

    @Override
    public List<MessageTemplate> getInstances(Search search, int offset, int limit) {
        search.setMaxResults(limit);
        search.setFirstResult(offset);
        return messageTemplateDao.search(search);
    }

    public long countInstances(Search search) {
        return messageTemplateDao.count(search);
    }


    @Override
    public MessageTemplate getInstanceById(long id) {
        return messageTemplateDao.findById(id).orElseThrow(() -> new OperationFailedException("MessageTemplate Owner Not found"));
    }


    public static Search composeSearchObject(String searchTerm) {
        Search search = CustomSearchUtils.generateSearchTerms(searchTerm,
                Arrays.asList("subject", "name"));

        return search;
    }


}
