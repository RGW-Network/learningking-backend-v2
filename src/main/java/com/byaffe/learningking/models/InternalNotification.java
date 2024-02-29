package com.byaffe.learningking.models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.models.User;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "internal_notifications")
public class InternalNotification extends BaseEntity {

    private String name;
    private String description;
    private Boolean isSeen = false;
    private User designatedUser;
    private Date scheduledDate;
    private String link;

    public InternalNotification() {
    }

    public InternalNotification(String name, String description, User designatedUser, String link) {
        this.name = name;
        this.description = description;
        this.designatedUser = designatedUser;
        this.link = link;
    }

    @Column(name = "name", nullable = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "is_seen", nullable = true)
    public Boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(Boolean isSeen) {
        this.isSeen = isSeen;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "designated_user_id", nullable = true)
    public User getDesignatedUser() {
        return designatedUser;
    }

    public void setDesignatedUser(User designatedUser) {
        this.designatedUser = designatedUser;
    }

    @Column(name = "scheduled_date", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    @Column(name = "link", nullable = true, length = 200)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof InternalNotification && (super.getId() != null) ? super.getId().equals(((InternalNotification) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}
