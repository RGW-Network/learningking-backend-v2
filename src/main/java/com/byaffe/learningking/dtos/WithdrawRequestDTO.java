package com.byaffe.learningking.dtos;

import lombok.Data;

@Data
public class WithdrawRequestDTO {
    private Double amount;
    private String phoneNumber;
    private String password;
    private String names;

}
