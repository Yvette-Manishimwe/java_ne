package com.bank.ne.entity;

import com.bank.ne.entity.Banking;
import com.bank.ne.validation.UniqueEmail;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be up to 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be up to 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be up to 100 characters")

    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobile;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Account number is required")
    @Size(max = 20, message = "Account number must be up to 20 characters")
    private String account;

    @PositiveOrZero(message = "Balance must be a positive number")
    private double balance;

    private LocalDateTime lastUpdatedTime;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Banking> bankings = new ArrayList<>();

    // Getters and Setters
}
