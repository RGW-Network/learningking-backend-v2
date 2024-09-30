package com.byaffe.learningking.models.payments;

import com.byaffe.learningking.constants.ChargeType;
import com.byaffe.learningking.constants.TransactionMode;
import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.constants.TransactionType;
import com.byaffe.learningking.models.CurrencyEnum;
import com.byaffe.learningking.models.Student;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "aggregator_transactions")
public class AggregatorTransaction extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    private TransactionMode mode;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency=CurrencyEnum.USD;
    private Double amountInitiated;
    private Double amountChargedFromUser;
    private Double chargeAmount;
    private Double chargeRate;
    private ChargeType chargeType;
    private String externalReference;
    private LocalDateTime timestamp;
    private String description;
    private String phoneNumber;
    private String redirectUrl;
    private Long referenceRecordId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    //Json data fields
    @Column(name = "last_aggregator_response", columnDefinition = "JSON")
    private String lastAggregatorResponse;
    @Column(name = "card_data", columnDefinition = "JSON")
    private String cardData;
    @Column(name = "bank_data", columnDefinition = "JSON")
    private String bankData;

    public void generateInternalReference() {
        generateSerialNumber();
    }
    @Transient
    public String getStudentName() {
        if(student==null){
            return  null;
        }
        return student.getFullName();
    }
    @Transient
    public String getStudentEmail() {
        if(student==null){
            return  null;
        }
        return student.getEmailAddress();
    }
    @Transient
    public Long getStudentId() {
        if(student==null){
            return  null;
        }
        return student.getId();
    }

    @Transient
    public String getInternalReference() {
        return this.getSerialNumber();
    }

    @Override
    public boolean equals(final Object other) {
        return (other instanceof AggregatorTransaction && this.getId() != 0) ? this.getId() == (((AggregatorTransaction) other).getId()) : (other == this);
    }

    public String generateSerialNumber() {
        DecimalFormat myFormatter = new DecimalFormat("LKTXN000000");
        return myFormatter.format(this.getId());
    }

    @Override
    public int hashCode() {
        return (this.getId() != 0) ? (int) (this.getClass().hashCode() + this.getId()) : super.hashCode();
    }

    @Transient
    public void attachCharge(TxnChargeDto charge) {
        this.chargeAmount = charge.getChargeAmount();
        this.chargeRate = charge.getChargeRate();
        this.chargeType = charge.getChargeType();
        this.amountChargedFromUser = this.amountInitiated + this.chargeAmount;


    }
}
