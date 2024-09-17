package com.byaffe.learningking.models.payments;

import com.byaffe.learningking.constants.TransactionStatus;
import com.byaffe.learningking.shared.models.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class BasePayment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 1000)
    private TransactionStatus status = TransactionStatus.NEW_CREATED;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = true)
    private PaymentType paymentType = PaymentType.MOBILE_MONEY;

    @Column(name = "transaction_id", nullable = true, length = 100)
    protected String transactionId;

    @Column(name = "rave_id")
    private String raveId;

    @Column(name = "last_rave_payment_link")
    private String lastRavePaymentLink;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "title")
    private String title = "LK payment";

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "description")
    private String description = "LK payment";

    @Column(name = "last_pgw_response", nullable = true, columnDefinition = "TEXT")
    private String lastPgwResponse;

    @Column(name = "failure_reason", nullable = true, columnDefinition = "TEXT")
    private String failureReason = "";

    @Column(name = "amount", nullable = true, length = 100)
    private Double amount = 0.0;

    public String attachTransactionId() {
        this.transactionId = String.valueOf(this.id);
        return this.transactionId;
    }
}
