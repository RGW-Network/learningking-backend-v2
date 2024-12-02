package com.byaffe.learningking.shared.models;

import com.byaffe.learningking.constants.TemplateType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "message_templates")
public class MessageTemplate extends BaseEntity {

    private String name;
    private String subject;// For only push and emails
    @Enumerated(EnumType.STRING)
    private MessageTemplateChannel channel;

    @Enumerated(EnumType.STRING)
    private TemplateType type;
    @Column(name = "body", columnDefinition = "TEXT")
    private String body;



}
