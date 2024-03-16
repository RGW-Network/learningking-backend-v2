package com.byaffe.learningking.models;

import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "notification_topics")
@Inheritance(strategy = InheritanceType.JOINED)
public class NotificationTopic extends BaseEntity {

    private String name;
    private String description;

    @ElementCollection
    @CollectionTable(name = "notification_topic_subscribers", joinColumns = @JoinColumn(name = "topic_id"))
    @Column(name = "subscriber_token")
    private List<String> subscribingTokens;

    public NotificationTopic(String name) {
        this.name = name;
    }

    public void addSubscriber(String token) {
        if (subscribingTokens == null) {
            subscribingTokens = new ArrayList<>();
        }
        subscribingTokens.add(token);
    }

    public void removeSubscriber(String token) {
        if (subscribingTokens != null) {
            subscribingTokens.remove(token);
        }
    }

}
