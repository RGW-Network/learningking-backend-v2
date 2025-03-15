package com.byaffe.learningking.models;

import com.byaffe.learningking.shared.models.BaseEntity;
import com.byaffe.learningking.shared.models.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class PasswordResetToken extends BaseEntity {


    private String token;

    private LocalDateTime expiryDate;

    @ManyToOne
    private User user;

    // Getters and Setters
}
