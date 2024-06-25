package com.bank.ne.repository;

import com.bank.ne.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByAccount(String account);
    boolean existsByEmail(String email);
}


