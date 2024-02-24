package com.byaffe.microtasks.models;

import com.byaffe.microtasks.shared.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "withdraw_requests")
public class WithdrawRequest extends BaseEntity {


    @Column(name = "amount_requested")
    private Double amountRequested;
    @Column(name = "system_tax")
    private Double systemTax;
    @Column(name = "net_amount_payable")
    private Double netAmountPayable;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "names")
    private String names;
    @Column(name = "date_completed")
    private LocalDateTime dateCompleted;
    @Column(name = "status")
    private WithdrawRequestStatus status= WithdrawRequestStatus.SUBMITTED;


    public String generateSerialNumber(){
        DecimalFormat myFormatter = new DecimalFormat("WDR000000");
        return myFormatter.format(this.getId());
    }


    @Override
    public boolean equals(Object object) {
        return object instanceof WithdrawRequest && (super.getId() != 0) ? super.getId()==((WithdrawRequest) object).getId() : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != 0 ? this.getClass().hashCode() + ((Long) super.getId()).hashCode() : super.hashCode();
    }
}
