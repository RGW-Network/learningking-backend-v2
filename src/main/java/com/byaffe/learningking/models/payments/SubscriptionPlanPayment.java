package com.byaffe.learningking.models.payments;

import com.byaffe.learningking.models.Member;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Table(name = "subscription_plan_payments")  
@Inheritance(strategy = InheritanceType.JOINED)
public class SubscriptionPlanPayment extends BasePayment {

    private static final long serialVersionUID = 1L;
    private SubscriptionPlan subscriptionPlan;
    private Member subscriber;

  
    /**
     * @return the course
     */
    @OneToOne(optional = true)
    @JoinColumn(name = "subscription_plan_id", nullable = true)
    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    /**
     * @return the subscriber
     */
    @OneToOne
    @JoinColumn(name = "subscriber")
    public Member getSubscriber() {
        return subscriber;
    }

    /**
     * @param subscriber the subscriber to set
     */
    public void setSubscriber(Member subscriber) {
        this.subscriber = subscriber;
    }

   
 @Override
    public String attachTransactionId() {
        if (StringUtils.isBlank(transactionId)) {
            this.transactionId = PaymentPrefixes.COURSE_PAYMENT_PREFIX + String.valueOf(id).toUpperCase();
        }
        return transactionId;
    }
}
