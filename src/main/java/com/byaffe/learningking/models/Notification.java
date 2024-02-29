package com.byaffe.learningking.models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.byaffe.learningking.shared.models.BaseEntity;

/**
 * @author RayGdhrt
 *
 */
@Entity
@Table(name = "notifications")
@Inheritance(strategy = InheritanceType.JOINED)
public class Notification extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Main title which appears on a notification
     */
    private String title;

    /**
     * Type of notification/ destination page. This is for the custom sent
     * notifications by church/admin
     */
    private NotificationDestinationActivity destinationActivity;

  

    /**
     * Description on a notification
     */
    private String description;

    /**
     * Featured notification image
     */
    private String imageUrl;

    /**
     * Type/ Origin of the notification
     */
    private OwnershipType type = OwnershipType.SYSTEM;

  
    /**
     * Scheduled date
     */
    private Date scheduleDate;

    /**
     * Scheduled time
     */
    private Date scheduleTime;

    /**
     * Id of the instance to be loaded e.g Publication_id for the publication
     * that will be opened when notification is clicked
     */
    private String destinationInstanceId;

    /**
     * The Firebase notification topic  to use
     */
    private String fmsTopicId;

    public Notification() {
    }

  

    /**
     * @return
     */
    @Column(name = "title", nullable = false, length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return
     */
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "image_url", length = 255)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    public OwnershipType getType() {
        return type;
    }

    public void setType(OwnershipType type) {
        this.type = type;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "destination_app_activity")
    public NotificationDestinationActivity getDestinationActivity() {
        return destinationActivity;
    }

    public void setDestinationActivity(NotificationDestinationActivity destinationActivity) {
        this.destinationActivity = destinationActivity;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "schedule_date")
    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    @Temporal(TemporalType.TIME)
    @Column(name = "schedule_time")
    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    @Override
    public String toString() {
        return this.title;
    }

    @Column(name = "destination_instance_id")
    public String getDestinationInstanceId() {
        return destinationInstanceId;
    }

    public void setDestinationInstanceId(String destinationInstanceId) {
        this.destinationInstanceId = destinationInstanceId;
    }


     @Column(name = "fms_channel_name")
    public String getFmsTopicId() {
        return fmsTopicId;
    }

    public void setFmsTopicId(String fmsTopicId) {
        this.fmsTopicId = fmsTopicId;
    }
    
    
    
    

    @Override
    public boolean equals(Object object) {
        return object instanceof Notification && (super.getId() != null) ? super.getId().equals(((Notification) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }

}
