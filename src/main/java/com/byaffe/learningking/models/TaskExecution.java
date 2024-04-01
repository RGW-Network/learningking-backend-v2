package com.byaffe.learningking.models;

import com.byaffe.learningking.shared.models.BaseEntity;
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
@Table(name = "task_executions")
public class TaskExecution extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "task_doer_id")
    private TaskDoer taskDoer;

    @Column(name = "details", columnDefinition = "LONGTEXT")
    private String details;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "big_details_id")
    private BigDetails bigDetails;
    @Column(name = "confirmation_token", columnDefinition = "LONGTEXT")
    private String confirmationToken;
    @Column(name = "confirmation_attachment_url", columnDefinition = "LONGTEXT")
    private String confirmationAttachmentUrl;
    @Column(name = "amount_paid")
    private Double amountPaid;
    @Column(name = "date_completed")
    private LocalDateTime dateCompleted;
    @Column(name = "rejection_reason")
    private String rejectionReason;
    @Column(name = "status")
    private TaskExecutionStatus status= TaskExecutionStatus.DRAFT;


    public String generateSerialNumber(){
        DecimalFormat myFormatter = new DecimalFormat("EXT000000");
        return myFormatter.format(this.getId());
    }


    @Override
    public boolean equals(Object object) {
        return object instanceof TaskExecution && (super.getId() != 0) ? super.getId()==((TaskExecution) object).getId() : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != 0 ? this.getClass().hashCode() + ((Long) super.getId()).hashCode() : super.hashCode();
    }
}
