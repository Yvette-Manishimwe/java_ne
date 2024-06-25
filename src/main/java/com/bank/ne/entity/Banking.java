package com.bank.ne.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
public class Banking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account number is required")
    private String account;

    @Positive(message = "Amount must be positive")
    private double amount;

    @NotBlank(message = "Transaction type is required")
    @Size(min = 3, max = 20, message = "Type must be between 3 and 20 characters")
    private String type; // "saving", "withdraw", "transfer"

    private LocalDateTime bankingDateTime;

    @NotNull(message = "Customer is required")
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Getters and Setters
}
