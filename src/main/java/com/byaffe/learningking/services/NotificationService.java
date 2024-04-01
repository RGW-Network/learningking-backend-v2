package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.models.Notification;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;

import java.util.List;

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
     * @param students
     * @throws ValidationFailedException
     * @throws OperationFailedException
     */
    void sendFCMNotificationToCustomStudents(Notification notification, String topicId, List<Student> students) throws ValidationFailedException, OperationFailedException;

    /**
     *
     * @param notification
     * @throws ValidationFailedException
     * @throws OperationFailedException
     */
    void sendNotificationsToAllStudents(Notification notification) throws ValidationFailedException, OperationFailedException;

    /**
     *
     * @param notification
     * @throws ValidationFailedException
     * @throws OperationFailedException
     */
    void sendNotificationsToChurchStudents(Notification notification) throws ValidationFailedException, OperationFailedException;

    /**
     *
     */
    void deleteAll();

    /**
     *
     * @param notification
     * @param student
     * @param fireNow
     * @throws ValidationFailedException
     * @throws OperationFailedException
     */
    void sendNotificationsToStudent(Notification notification, Student student, boolean fireNow) throws ValidationFailedException, OperationFailedException;

}
