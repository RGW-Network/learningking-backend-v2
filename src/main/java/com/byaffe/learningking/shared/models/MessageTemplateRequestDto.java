package com.byaffe.learningking.shared.models;

import lombok.Data;

@Data
public class MessageTemplateRequestDto {
    private Long id;
    private String name;
    private String subject;// For only push and emails
    private MessageTemplateChannel channel;
    private MessageTemplateType type;
    private String body;
}
