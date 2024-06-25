package com.bank.ne.service;

import com.bank.ne.entity.Customer;
import com.bank.ne.exception.CustomerNotFoundException;
import com.bank.ne.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerCustomer(Customer customer) {
        Boolean isCustomerEmailTaken = customerRepository.existsByEmail(customer.getEmail());
        if(isCustomerEmailTaken){
            throw new IllegalStateException("Customer already exists with email: "+customer.getEmail());
        }
        customer.setId(customer.getId());
        customer.setLastUpdatedTime(LocalDateTime.now());
        customerRepository.save(customer);
    }

    public void updateCustomer(Customer customer) {
        customer.setLastUpdatedTime(LocalDateTime.now());
        customerRepository.save(customer);
    }

    public Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    public Customer getCustomerByAccount(String account) {
        return customerRepository.findByAccount(account);
    }
}
