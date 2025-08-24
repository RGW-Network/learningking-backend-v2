package com.byaffe.learningking.models.courses;

import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.shared.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "organisation_purchases")
public class OrganisationPurchase extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private AggregatorTransaction transaction;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private OrganisationGroup group;

    @Column(name = "size", length = 10)
    private Long size = 1L;

    @Column(name = "record_Id")
    private Long recordId;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_type")
    private CategoryType recordType;

    @Column(name = "comma_separated_bulk_entry_ids", columnDefinition = "TEXT")
    private String commaSeparatedBulkEntryIds;//organisation student ids
    private String bulkExceptions;

    public List<Long> getEntryIds() {
        if (StringUtils.isNotEmpty(this.commaSeparatedBulkEntryIds)) {
            return Arrays.stream(this.commaSeparatedBulkEntryIds.split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList(); // More efficient than new ArrayList<>()
        }
    }
    @Override
    public boolean equals(Object object) {
        return object instanceof OrganisationPurchase && (super.getId() != null) ? super.getId().equals(((OrganisationPurchase) object).getId())
                : (object == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? this.getClass().hashCode() + super.getId().hashCode() : super.hashCode();
    }
}