package com.byaffe.learningking.dtos;

import com.byaffe.learningking.models.payments.Currency;
import lombok.Data;

@Data
public class CertificateTemplateRequestDto {
    private Long id;
    private String title;
    private String template;
}
