package com.byaffe.learningking.services;

import com.byaffe.learningking.models.NotificationTopic;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

public interface NotificationTopicService extends GenericService<NotificationTopic> {

    /**
     *
     * @param notification
     * @return
     * @throws ValidationFailedException
     * @throws OperationFailedException
     */
    NotificationTopic saveNotificationTopic(NotificationTopic notification)
            throws ValidationFailedException, OperationFailedException;

 void subscribeToTopic(String deviceId,String topicName) throws ValidationFailedException, OperationFailedException;
  void unsubscribeFromTopic(String deviceId,String topicName) throws ValidationFailedException, OperationFailedException;

  NotificationTopic getTopicByName(String topicName) ;
}
