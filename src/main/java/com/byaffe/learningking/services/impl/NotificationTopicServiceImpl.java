package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.models.NotificationTopic;
import com.byaffe.learningking.services.NotificationTopicService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationTopicServiceImpl extends GenericServiceImpl<NotificationTopic> implements NotificationTopicService {

    @Override
    public NotificationTopic saveNotificationTopic(NotificationTopic notificationTopic)
            throws ValidationFailedException, OperationFailedException {
        System.out.println("Saving created notification topic...");
        if (notificationTopic.getName() == null) {
            throw new ValidationFailedException("Missing name ");
        }
        notificationTopic.setName(notificationTopic.getName().trim());

        NotificationTopic existsWithName = getTopicByName(notificationTopic.getName());

        if (existsWithName != null && !existsWithName.equals(notificationTopic)) {
            return existsWithName;
        }

        return super.save(notificationTopic);
    }

    public NotificationTopic createTopic(NotificationTopic notificationTopic)
            throws ValidationFailedException, OperationFailedException {
        System.out.println("Saving created notification topic...");
        if (notificationTopic.getName() == null) {
            throw new ValidationFailedException("Missing name ");
        }
        notificationTopic.setName(notificationTopic.getName().trim());

        NotificationTopic existsWithName = getTopicByName(notificationTopic.getName());

        if (existsWithName != null && !existsWithName.equals(notificationTopic)) {
            throw new ValidationFailedException("Topic name Exists");
        }

        return super.save(notificationTopic);
    }

    @Override
    public boolean isDeletable(NotificationTopic entity) throws OperationFailedException {
        return true;
    }

    @Override
    public NotificationTopic saveInstance(NotificationTopic instance) throws ValidationFailedException, OperationFailedException {
        return super.save(instance);
    }

    @Override
    public void subscribeToTopic(String deviceId, String topicName) throws ValidationFailedException, OperationFailedException {
        if (StringUtils.isNotBlank(topicName) && StringUtils.isNotBlank(deviceId)) {
            NotificationTopic notificationTopic = getTopicByName(topicName);
            if (notificationTopic == null) {
                notificationTopic = saveNotificationTopic(new NotificationTopic(topicName));
            }
            notificationTopic.addSubscriber(deviceId);
            saveInstance(notificationTopic);
        }

    }

    @Override
    public void unsubscribeFromTopic(String deviceId, String topicName) throws ValidationFailedException, OperationFailedException {
        if (StringUtils.isNotBlank(topicName) && StringUtils.isNotBlank(deviceId)) {
            NotificationTopic notificationTopic = getTopicByName(topicName);
            if (notificationTopic != null) {
                notificationTopic.removeSubscriber(deviceId);
                saveInstance(notificationTopic);

            }
        }
    }

    @Override
    public NotificationTopic getTopicByName(String topicName) {
        System.err.println(">>>>>>>>>>>>>>>>>>>>Got topic>>>>>>>>>" + topicName);
        return super.searchUniqueByPropertyEqual("name", topicName, RecordStatus.ACTIVE);
    }

}
