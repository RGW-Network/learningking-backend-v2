package com.byaffe.learningking.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import jdk.nashorn.internal.ir.annotations.Ignore;

import com.byaffe.learningking.shared.models.BaseEntity;

/**
 * @author RayGdhrt
 *
 */
@Entity
@Table(name = "notification_topics")
@Inheritance(strategy = InheritanceType.JOINED)
public class NotificationTopic extends BaseEntity {

    private static final long serialVersionUID = 1L;
      public NotificationTopic() {
    }

    /**
     * Main title which appears on a notification
     */
    private String name;

    /**
     * Main title which appears on a notification
     */
    private String description;

    private List<String> subscribingTokens;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ElementCollection
    @CollectionTable(name = "notification_topic_subcribers", joinColumns = @JoinColumn(name = "topic_id"))
    @Column(name = "nickname")
    public List<String> getSubscribingTokens() {
        return subscribingTokens;
    }

    public void setSubscribingTokens(List<String> subscribingTokens) {
        this.subscribingTokens = subscribingTokens;
    }

    public void addSubscriber(String token) {
        if (this.subscribingTokens == null) {
            this.subscribingTokens = new ArrayList<>();
        }
        this.subscribingTokens.add(token);
    }
    
    public void removeSubscriber(String token) {
        this.subscribingTokens.remove(token);
    }

  

    @Ignore
    public NotificationTopic(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof NotificationTopic && (super.getId() != null) ? super.getId().equals(((NotificationTopic) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
