package com.byaffe.learningking.dtos;

import com.byaffe.learningking.constants.SubscriptionPlanPaymentType;
import com.byaffe.learningking.models.courses.PublicationStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BulkPaymentRequestDTO {
private Long organisationGroupId;
private String callBackUrl;
}
