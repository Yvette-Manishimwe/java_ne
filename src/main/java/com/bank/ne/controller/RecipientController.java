package com.bank.ne.controller;

import com.bank.ne.entity.Recipient;
import com.bank.ne.service.RecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipients")
public class RecipientController {

    private final RecipientService recipientService;

    @Autowired
    public RecipientController(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

    @PostMapping("/register")
    public ResponseEntity<Recipient> registerRecipient(@RequestBody Recipient recipient) {
        Recipient registeredRecipient = recipientService.registerRecipient(recipient);
        return new ResponseEntity<>(registeredRecipient, HttpStatus.CREATED);
    }

    @GetMapping("/{recipientId}")
    public ResponseEntity<Recipient> getRecipientById(@PathVariable Long recipientId) {
        Recipient recipient = recipientService.getRecipientById(recipientId);
        return new ResponseEntity<>(recipient, HttpStatus.OK);
    }

    // Other endpoints as needed for recipient management
}
