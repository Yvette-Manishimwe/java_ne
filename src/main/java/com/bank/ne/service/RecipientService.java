package com.bank.ne.service;

import com.bank.ne.entity.Recipient;
import com.bank.ne.exception.CustomerNotFoundException;
import com.bank.ne.exception.RecipientNotFoundException;
import com.bank.ne.repository.RecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipientService {

    private final RecipientRepository recipientRepository;

    @Autowired
    public RecipientService(RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }

    public Recipient registerRecipient(Recipient recipient) {
        // Implement validation logic if needed
        return recipientRepository.save(recipient);
    }

    public Recipient getRecipientById(Long recipientId) {
        return recipientRepository.findById(recipientId)
                .orElseThrow(() -> new CustomerNotFoundException("Recipient not found"));
    }
    public Recipient getRecipientByAccount(String accountNumber) {
        Recipient recipient = recipientRepository.findByAccountNumber(accountNumber);
        if (recipient == null) {
            throw new RecipientNotFoundException("Recipient with account number " + accountNumber + " not found");
        }
        return recipient;
    }

    // Other methods as needed
}
