package com.bank.ne.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BankingDTO {
    private Long customerId;
    private String account;
    private double amount;
    private String type; // "saving", "withdraw", "transfer"
    private LocalDateTime bankingDateTime;

    // Getters and Setters
}
