package com.bank.ne.controller;


import com.bank.ne.dto.BankingDTO;
import com.bank.ne.entity.Banking;
import com.bank.ne.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/banking")
@Validated // Enables validation for this controller
public class BankingController {

    private final BankingService bankingService;

    @Autowired
    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @PostMapping("/save")
    public ResponseEntity<Banking> saveTransaction(@Valid @RequestBody BankingDTO bankingDTO) {
        bankingService.saveTransaction(bankingDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawTransaction(@Valid @RequestBody BankingDTO bankingDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getValidationErrors(result));
        }
        bankingService.withdrawTransaction(bankingDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferTransaction(@RequestParam Long fromCustomerId, @RequestParam String toAccount, @RequestParam double amount) {

        bankingService.transferTransaction(fromCustomerId, toAccount, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Utility method to format validation errors
    private Map<String, String> getValidationErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }
}
