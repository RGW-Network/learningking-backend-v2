package com.byaffe.learningking.services;

import java.util.List;
import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.MemberPendingNotification;
import com.byaffe.learningking.models.Notification;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

public interface PendingNotificationService extends GenericService<MemberPendingNotification> {

    /**
     *
     * @param member
     * @param notification
     * @return
     */
    MemberPendingNotification addNotification(Member member, Notification notification)
            throws ValidationFailedException, OperationFailedException;

    /**
     * 
     * @param notification
     * @return
     * @throws ValidationFailedException
     * @throws OperationFailedException 
     */
    void removeNotification(MemberPendingNotification notification)
            throws ValidationFailedException, OperationFailedException;
    
    /**
     * 
     * @param notifications
     * @return
     * @throws ValidationFailedException
     * @throws OperationFailedException 
     */
    void removeNotifications(List<MemberPendingNotification> notifications)
            throws ValidationFailedException, OperationFailedException;
    
    /**
     * 
     * @param member
     * @return
     * @throws ValidationFailedException
     * @throws OperationFailedException 
     */
      void removeAllNotifications(Member member)
            throws ValidationFailedException, OperationFailedException;
}
