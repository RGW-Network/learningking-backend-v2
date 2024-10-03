package com.byaffe.learningking.dtos;

import com.byaffe.learningking.constants.SubscriptionPlanPaymentType;
import com.byaffe.learningking.models.courses.PublicationStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubscriptionPaymentRequestDTO {
private Long organisationGroupId;
    private SubscriptionPlanPaymentType type;
}
