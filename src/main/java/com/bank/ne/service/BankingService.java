package com.bank.ne.service;

import com.bank.ne.dto.BankingDTO;
import com.bank.ne.entity.Banking;
import com.bank.ne.entity.Customer;
import com.bank.ne.entity.Recipient;
import com.bank.ne.enums.ETransactionType;
import com.bank.ne.exception.InsufficientFundsException;
import com.bank.ne.exception.RecipientNotFoundException;
import com.bank.ne.mailHandling.MailServiceImpl;
import com.bank.ne.repository.BankingRepository;
import com.bank.ne.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class BankingService {
    private final BankingRepository bankingRepository;
    private final CustomerService customerService;
    public final RecipientService recipientService;
    private final CustomerRepository customerRepository;
    private final MailServiceImpl mailService;


    @Autowired
    public BankingService(BankingRepository bankingRepository, CustomerService customerService, RecipientService recipientService, MailServiceImpl mailService, MailServiceImpl mailService1, CustomerRepository customerRepository) {
        this.bankingRepository = bankingRepository;
        this.customerService = customerService;
        this.recipientService = recipientService;
        this.mailService = mailService1;
        this.customerRepository=customerRepository;
    }

    @Transactional
    public Banking saveTransaction(BankingDTO bankingDTO) {
        Customer customer = customerRepository.findById(bankingDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer ID"));

        Banking banking = new Banking();
        banking.setCustomer(customer);
        banking.setAccount(bankingDTO.getAccount());
        banking.setAmount(bankingDTO.getAmount());
        banking.setType(bankingDTO.getType());
        banking.setBankingDateTime(bankingDTO.getBankingDateTime());

        Banking savedBanking = bankingRepository.save(banking);

        mailService.sendMail(customer, banking.getType(), banking.getAmount());

        return savedBanking;
    }
    //mailService.sendMail(customer,ETransactionType.SAVING,banking.getAmount());

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
            banking.setType(ETransactionType.WITHDRAW);
            banking.setBankingDateTime(LocalDateTime.now());
            bankingRepository.save(banking);
            mailService.sendMail(customer,ETransactionType.WITHDRAW,bankingDTO.getAmount());
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
            fromBanking.setType(ETransactionType.TRANSFER);
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
