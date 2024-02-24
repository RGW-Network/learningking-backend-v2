package com.byaffe.microtasks.models;

import com.byaffe.microtasks.shared.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "details", columnDefinition = "LONGTEXT")
    private String details;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "big_details_id")
    private BigDetails bigDetails;


    @Column(name = "cost_per_execution")
    private Double costPerExecution;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private TaskStatus status= TaskStatus.SUBMITTED;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "locality")
    private TaskLocality locality= TaskLocality.LOCAL;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "complexity")
    private TaskComplexity complexity= TaskComplexity.STARTER;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "task_verification_type")
    private TaskVerificationType verificationType= TaskVerificationType.MANUAL;
    @Column(name = "featured")
    private Boolean featured;
    @Column(name = "required_executions")
    private Long requiredExecutions;
    @Column(name = "current_executions")
    private Long currentExecutions;
    @Column(name = "auto_approval_regex")
    private String autoApprovalRegex;
    @Column(name = "attachment_url")
    private String attachmentUrl;
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "approval_tokens", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "auto_approval_token_list")
    private Set<String> autoApprovalTokenList = new HashSet<>();
    public String generateSerialNumber(){
        DecimalFormat myFormatter = new DecimalFormat("TSK000000");
        return myFormatter.format(this.getId());
    }


    @Override
    public boolean equals(Object object) {
        return object instanceof Task && (super.getId() != 0) ? super.getId()==((Task) object).getId() : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != 0 ? this.getClass().hashCode() + ((Long) super.getId()).hashCode() : super.hashCode();
    }
}
