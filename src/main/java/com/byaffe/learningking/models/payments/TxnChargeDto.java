package com.byaffe.learningking.models.payments;

import com.byaffe.learningking.constants.ChargeType;
import lombok.Data;

@Data
public class TxnChargeDto {
    private Double chargeAmount;
    private ChargeType chargeType;
    private Double chargeRate;
}
