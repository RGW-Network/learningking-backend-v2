package com.byaffe.learningking.services;

import java.util.List;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.StudentPendingNotification;
import com.byaffe.learningking.models.Notification;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

public interface PendingNotificationService extends GenericService<StudentPendingNotification> {

    /**
     *
     * @param student
     * @param notification
     * @return
     */
    StudentPendingNotification addNotification(Student student, Notification notification)
            throws ValidationFailedException, OperationFailedException;

    /**
     * 
     * @param notification
     * @return
     * @throws ValidationFailedException
     * @throws OperationFailedException 
     */
    void removeNotification(StudentPendingNotification notification)
            throws ValidationFailedException, OperationFailedException;
    
    /**
     * 
     * @param notifications
     * @return
     * @throws ValidationFailedException
     * @throws OperationFailedException 
     */
    void removeNotifications(List<StudentPendingNotification> notifications)
            throws ValidationFailedException, OperationFailedException;
    
    /**
     * 
     * @param student
     * @return
     * @throws ValidationFailedException
     * @throws OperationFailedException 
     */
      void removeAllNotifications(Student student)
            throws ValidationFailedException, OperationFailedException;
}
