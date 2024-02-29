package com.byaffe.learningking.services.impl;

import com.byaffe.learningking.constants.AccountStatus;
import com.byaffe.learningking.constants.NotificationTopics;
import com.byaffe.learningking.models.Member;
import com.byaffe.learningking.models.Notification;
import com.byaffe.learningking.services.MemberService;
import com.byaffe.learningking.services.NotificationService;
import com.byaffe.learningking.services.PendingNotificationService;
import com.byaffe.learningking.shared.constants.RecordStatus;
import com.byaffe.learningking.shared.exceptions.OperationFailedException;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.byaffe.learningking.shared.utils.ApplicationContextProvider;
import com.byaffe.learningking.utilities.AppUtils;
import com.googlecode.genericdao.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl extends GenericServiceImpl<Notification> implements NotificationService {

    @Autowired
    PendingNotificationService pendingNotificationService;

    @Override
    public Notification saveNotification(Notification notification)
            throws ValidationFailedException, OperationFailedException {
        System.out.println("Saving created notification...");
        if (notification.getTitle() == null) {
            throw new ValidationFailedException("Missing title ");
        }

        if (notification.getDescription() == null) {
            throw new ValidationFailedException("Missing description");
        }

        return super.save(notification);
    }

    @Transactional(propagation = Propagation.NEVER)
    @Override
    public void sendFCMNotificationToCustomMembers(Notification notification, String topicId, List<Member> members) throws ValidationFailedException, OperationFailedException {

        if (notification.isNew()) {
            notification = saveNotification(notification);
        }

        if (topicId == null && (members == null || members.isEmpty())) {
            throw new ValidationFailedException("Missing firebase topicId and Members. Pleas supply one of them");

        }

        System.out.println("Sending created notification...");

        if (topicId != null) {
            AppUtils.sendNotificationToTopic("", notification.getTitle(), notification.getDescription(), notification.getDestinationActivity(), notification.getDestinationInstanceId());
        } else {
            MemberService memberService = ApplicationContextProvider.getBean(MemberService.class);

            System.out.println("Its for RGW...");
            for (Member member : memberService.getMembers(new Search()
                    .addFilterEqual("status", AccountStatus.Active)
                    .addFilterEqual("recordStatus", RecordStatus.ACTIVE), 0, 0)) {
                pendingNotificationService.addNotification(member, notification);
            }
            AppUtils.sendDirectNotifications(notification.getTitle(), notification.getDescription(), null, members, notification.getDestinationActivity(), notification.getDestinationInstanceId());
        }

        return;

    }

    @Override
    public void sendNotificationsToAllMembers(Notification notification) throws ValidationFailedException, OperationFailedException {
        System.out.println("Sending created notification...");
        if (notification.isNew()) {
            notification = saveNotification(notification);
        }
        MemberService memberService = ApplicationContextProvider.getBean(MemberService.class);

        System.out.println("Its for RGW...");
        for (Member member : memberService.getMembers(new Search()
                .addFilterEqual("accountStatus", AccountStatus.Active)
                .addFilterEqual("recordStatus", RecordStatus.ACTIVE), 0, 0)) {
            pendingNotificationService.addNotification(member, notification);
        }

        AppUtils.sendNotificationToTopic(NotificationTopics.GENERAL_TOPIC, notification.getTitle(), notification.getDescription(), notification.getDestinationActivity(), notification.getDestinationInstanceId());
    }
 
    @Override
    public void sendNotificationsToChurchMembers(Notification notification) throws ValidationFailedException, OperationFailedException {

    }

    @Override
    public void sendNotificationsToMember(Notification notification, Member member, boolean fireNow) throws ValidationFailedException, OperationFailedException {

        if (notification.isNew()) {
            notification = saveNotification(notification);
        }

        pendingNotificationService.addNotification(member, notification);
        final Notification savedNotification = notification;
        if (fireNow) {
            new Thread(
                    new Runnable() {
                @Override
                public void run() {
                    AppUtils.sendDirectNotifications(savedNotification.getTitle(),
                            savedNotification.getDescription(),
                            null,
                            new ArrayList<>(Arrays.asList(member)),
                            savedNotification.getDestinationActivity(),
                            savedNotification.getDestinationInstanceId());

                }
            }
            ).start();

        }

    }

    @Override
    public boolean isDeletable(Notification entity) throws OperationFailedException {
        return true;
    }

    @Override
    public Notification saveInstance(Notification instance) throws ValidationFailedException, OperationFailedException {
        return super.save(instance);
    }

    public void deleteAll() {
        for (Notification notification : super.findAll()) {
            super._removeEntity(notification);
        }
    }
}
