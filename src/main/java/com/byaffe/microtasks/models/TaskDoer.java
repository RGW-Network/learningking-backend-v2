package com.byaffe.microtasks.models;

import com.byaffe.microtasks.shared.constants.Gender;
import com.byaffe.microtasks.shared.models.BaseEntity;
import com.byaffe.microtasks.shared.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "task_doers")
public class TaskDoer extends BaseEntity {


    @Column(name = "national_id_number")
    private String nationalIDNumber;

    @Column(name = "digital_wallet_number")
    private String digitalWalletNumber;

    @Column(name = "total_tasks_created")
    private Integer totalTasksCreated;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    private User userAccount;

    public String generateSerialNumber(){
        DecimalFormat myFormatter = new DecimalFormat("TSDR000000");
        return myFormatter.format(this.getId());
    }
}
