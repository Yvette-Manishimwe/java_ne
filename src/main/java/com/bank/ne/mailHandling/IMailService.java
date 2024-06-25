package com.bank.ne.mailHandling;

import com.bank.ne.entity.Customer;
import com.bank.ne.enums.ETransactionType;

public interface IMailService {
    void sendMail(Customer client, ETransactionType type, Double amount);
}