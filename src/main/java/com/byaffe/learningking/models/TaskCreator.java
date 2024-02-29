package com.byaffe.learningking.models;

import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DecimalFormat;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "task_creators")
public class TaskCreator extends BaseEntity {


    @Column(name = "national_id_number")
    private String nationalIDNumber;

    @Column(name = "digital_wallet_number")
    private String digitalWalletNumber;

    @Column(name = "total_tasks_created")
    private Integer totalTasksCreated;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User userAccount;
    public String generateSerialNumber(){
        DecimalFormat myFormatter = new DecimalFormat("TSKCT000000");
        return myFormatter.format(this.getId());
    }

}
