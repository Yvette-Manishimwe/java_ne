package com.bank.ne.mailHandling;

import com.bank.ne.entity.Customer;
import com.bank.ne.enums.ETransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailServiceImpl implements IMailService {
    @Autowired
    private JavaMailSender javaMailSender;
    SimpleMailMessage mail = new SimpleMailMessage();


    @Override
    public void sendMail(Customer client, ETransactionType type, Double amount) {
        mail.setTo(client.getEmail());
        mail.setSubject("Transaction Notification");
        mail.setText("Dear " + client.getFirstName() +" "+ client.getLastName() + ", your " + type + " of " + amount + " frw on your account " + client.getAccount() + " has been completed successfully.");
        javaMailSender.send(mail);
    }
}