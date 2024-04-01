package com.byaffe.learningking.models;

import com.byaffe.learningking.constants.CommunicationType;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "communications")
public class Communication extends BaseEntity {

    private String emailSubject;
    @Column(length = 1000)
    private String emailBody;
    private String smsMessage;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipients")
    private List<String> recipients;
    @Enumerated(EnumType.STRING)
    private CommunicationType communicationType;
    private boolean emailsSent = false;
    private boolean smsSent = false;
    private LocalDateTime scheduleDate;
    private LocalDateTime scheduleTime;
    private int sentEmails;
    private int sentPhones;

}
