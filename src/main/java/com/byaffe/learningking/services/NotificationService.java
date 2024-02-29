package com.byaffe.learningking.services;

import java.util.List;
import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.Notification;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import org.sers.webutils.model.exception.OperationFailedException;

public interface NotificationService extends GenericService<Notification> {

    /**
     *
     * @param notification
     * @return
     * @throws ValidationFailedException
     * @throws OperationFailedException
     */
    Notification saveNotification(Notification notification)
            throws ValidationFailedException, OperationFailedException;

    /**
     * Sends scheduled notifications to app users.They will be stored in the
     * queue
     *
     * @param notification
     * @param topicId
     * @param members
     * @throws ValidationFailedException
     * @throws OperationFailedException
     */
    void sendFCMNotificationToCustomMembers(Notification notification, String topicId, List<Member> members) throws ValidationFailedException, OperationFailedException;

    /**
     *
     * @param notification
     * @throws ValidationFailedException
     * @throws OperationFailedException
     */
    void sendNotificationsToAllMembers(Notification notification) throws ValidationFailedException, OperationFailedException;

    /**
     *
     * @param notification
     * @throws ValidationFailedException
     * @throws OperationFailedException
     */
    void sendNotificationsToChurchMembers(Notification notification) throws ValidationFailedException, OperationFailedException;

    /**
     *
     */
    void deleteAll();

    /**
     *
     * @param notification
     * @param member
     * @param fireNow
     * @throws ValidationFailedException
     * @throws OperationFailedException
     */
    void sendNotificationsToMember(Notification notification, Member member, boolean fireNow) throws ValidationFailedException, OperationFailedException;

}
