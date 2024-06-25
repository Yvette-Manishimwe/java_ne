package com.bank.ne.service;

import com.bank.ne.dto.BankingDTO;
import com.bank.ne.entity.Banking;
import com.bank.ne.entity.Customer;
import com.bank.ne.entity.Recipient;
import com.bank.ne.exception.InsufficientFundsException;
import com.bank.ne.exception.RecipientNotFoundException;
import com.bank.ne.repository.BankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class BankingService {
    private final BankingRepository bankingRepository;
    private final CustomerService customerService;
    public final RecipientService recipientService;


    @Autowired
    public BankingService(BankingRepository bankingRepository, CustomerService customerService, RecipientService recipientService) {
        this.bankingRepository = bankingRepository;
        this.customerService = customerService;
        this.recipientService = recipientService;
    }

    public void saveTransaction(Banking banking) {
        Customer customer = customerService.getCustomer(banking.getCustomer().getId());

        customer.getBankings().add(banking); // Add banking transaction to customer's bankings list

        customer.setBalance(customer.getBalance() + banking.getAmount());
        customerService.updateCustomer(customer);

        banking.setId(banking.getId());
        banking.setBankingDateTime(LocalDateTime.now());
        banking.setType("saving");
        bankingRepository.save(banking);
    }

    public void withdrawTransaction(BankingDTO bankingDTO) {
        Customer customer = customerService.getCustomer(bankingDTO.getCustomerId());
        if (customer.getBalance() >= bankingDTO.getAmount()) {
            customer.setBalance(customer.getBalance() - bankingDTO.getAmount());
            customerService.updateCustomer(customer);

            Banking banking = new Banking();
            banking.setId(banking.getId());
            banking.setCustomer(customer);
            banking.setAccount(bankingDTO.getAccount());
            banking.setAmount(bankingDTO.getAmount());
            banking.setType("withdraw");
            banking.setBankingDateTime(LocalDateTime.now());
            bankingRepository.save(banking);
        } else {
            throw new InsufficientFundsException("Insufficient funds for this withdrawal");
        }
    }

    public void transferTransaction(Long fromCustomerId, String toAccount, double amount) {
        Customer fromCustomer = customerService.getCustomer(fromCustomerId);
        if (fromCustomer.getBalance() >= amount) {
            // Check if recipient account exists

            Recipient toRecipient = recipientService.getRecipientByAccount(toAccount);
            if (toRecipient == null) {
                throw new RecipientNotFoundException("Recipient account does not exist");
            }

            fromCustomer.setBalance(fromCustomer.getBalance() - amount);
            customerService.updateCustomer(fromCustomer);

            Banking fromBanking = new Banking();
            fromBanking.setCustomer(fromCustomer);
            fromBanking.setAccount(fromCustomer.getAccount());
            fromBanking.setAmount(amount);
            fromBanking.setType("transfer");
            fromBanking.setBankingDateTime(LocalDateTime.now());
            bankingRepository.save(fromBanking);

            // Update recipient's balance
            // Assuming recipient's balance update is handled in recipientService or similar
            // Example: toRecipient.setBalance(toRecipient.getBalance() + amount);
            // recipientService.updateRecipient(toRecipient);
        } else {
            throw new InsufficientFundsException("Insufficient funds for this transfer");
        }
    }
}
