package com.byaffe.microtasks.shared.models;

import com.byaffe.microtasks.shared.constants.RecordStatus;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

/**
 *
 * @author RayGdhrt
 */
@MappedSuperclass
public class BaseEntity implements Auditable {
    private static final long serialVersionUID = 6095671201979163425L;

    @Id
    @GeneratedValue
    public Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "record_status", nullable = false)
    private RecordStatus recordStatus=RecordStatus.ACTIVE;

    @Column(name = "created_by_id", nullable = true)
    private long createdById;
    @Column(name = "changed_by_id", nullable = true)
    private long changedById;

    @Column(name = "created_by_user_name", nullable = true)
    private String createdByUsername;

    @Column(name = "created_by_full_name", nullable = true)
    private String createdByFullName;

    @Column(name = "changed_by_full_name", nullable = true)
    private String changedByFullName;
    @Column(name = "changed_by_username", nullable = true)
    private String changedByUsername;

    @Column(name = "date_created", nullable = true)
    private LocalDateTime dateCreated;

    @Column(name = "date_changed", nullable = true)
    private LocalDateTime dateChanged;

    @Column(name = "custom_prop_one")
    private String customPropOne;


    @Column(name = "serial_number")
    private String serialNumber;

    public BaseEntity() {
        this.id = 0L;
        this.recordStatus = RecordStatus.ACTIVE;
    }

    @PrePersist
    public void prePersist() {
        this.dateCreated = LocalDateTime.now();
    }


    @PreUpdate
    public void preUpdate() {
        this.dateChanged = LocalDateTime.now();
    }

    public void addAuditTrail(User loggedInUser) {
        if (loggedInUser == null)
            return;

        if (this.getCreatedById() == 0) {
            this.setCreatedById(loggedInUser.getId());
            this.setCreatedByUsername(loggedInUser.getUsername());
            this.setCreatedByFullName(loggedInUser.getFullName());
            this.setDateCreated(LocalDateTime.now());
        }
        this.setChangedByFullName(loggedInUser.getFullName());
        this.setChangedById(loggedInUser.getId());
        this.setChangedByUsername(loggedInUser.getUsername());
        this.setDateChanged(LocalDateTime.now());
    }
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public String generateSerialNumber(){
        DecimalFormat myFormatter = new DecimalFormat("REC000000");
        return myFormatter.format(this.getId());
    }
    public String getCreatedByFullName() {
        return createdByFullName;
    }

    public void setCreatedByFullName(String createdByFullName) {
        this.createdByFullName = createdByFullName;
    }

    public String getChangedByFullName() {
        return changedByFullName;
    }

    public void setChangedByFullName(String changedByFullName) {
        this.changedByFullName = changedByFullName;
    }

    public RecordStatus getRecordStatus() {
        return this.recordStatus;
    }

    public void setRecordStatus(final RecordStatus recordStatus) {
        this.recordStatus = recordStatus;
    }

    public long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(long createdById) {
        this.createdById = createdById;
    }

    public long getChangedById() {
        return changedById;
    }

    public void setChangedById(long changedById) {
        this.changedById = changedById;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }



    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public String getChangedByUsername() {
        return changedByUsername;
    }

    public void setChangedByUsername(String changedByUsername) {
        this.changedByUsername = changedByUsername;
    }

    @Override
    public LocalDateTime getDateCreated() {
        return this.dateCreated;
    }

    @Override
    public void setDateCreated(final LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public LocalDateTime getDateChanged() {
        return this.dateChanged;
    }

    @Override
    public void setDateChanged(final LocalDateTime dateChanged) {
        this.dateChanged = dateChanged;
    }


    public String getCustomPropOne() {
        return this.customPropOne;
    }

    public void setCustomPropOne(final String customPropOne) {
        this.customPropOne = customPropOne;
    }

    @Override
    public String toString() {
        return "Equals not implemented";
    }

    @Transient
    public boolean isNew() {
        return this.id == 0;
    }

    @Transient
    public boolean isSaved() {
        return !this.isNew();
    }
}
