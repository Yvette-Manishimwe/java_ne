package com.bank.ne;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor

public class AdminApplication  {
   
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}



