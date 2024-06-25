package com.bank.ne.repository;



import com.bank.ne.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {

    Recipient findByAccountNumber(String accountNumber);

    // Add other custom query methods if needed
}
