/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.models;

import java.util.Date;

/**
 * 
 * @author RayGdhrt
 */
public class NotificationBuilder {

    private String title;
    private NotificationDestinationActivity destinationActivity;
    private String description;
    private String imageUrl;
    private Date scheduleDate;
    private Date scheduleTime;
    private String destinationInstanceId;
    private String fmsTopicName;
    private OwnershipType type = OwnershipType.SYSTEM;

    public NotificationBuilder() {
    }

    public NotificationBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public NotificationBuilder setDestinationActivity(NotificationDestinationActivity destinationActivity) {
        this.destinationActivity = destinationActivity;
        return this;
    }

    public NotificationBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public NotificationBuilder setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

  
    public NotificationBuilder setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
        return this;
    }

    public NotificationBuilder setType(OwnershipType ownershipType) {
        this.type = ownershipType;
        return this;
    }

    public NotificationBuilder setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
        return this;
    }

    public NotificationBuilder setDestinationInstanceId(String destinationInstanceId) {
        this.destinationInstanceId = destinationInstanceId;
        return this;
    }

    public NotificationBuilder setFmsTopicName(String customChannelName) {
        this.fmsTopicName = customChannelName;
        return this;
    }

  

    public Notification build() {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setDescription(description);
        notification.setType(type);
        notification.setScheduleDate(scheduleDate);
        notification.setScheduleTime(scheduleTime);
        notification.setFmsTopicId(fmsTopicName);
       
        notification.setImageUrl(imageUrl);
        notification.setDestinationActivity(destinationActivity);
        notification.setDestinationInstanceId(destinationInstanceId);

        return notification;

    }

}
