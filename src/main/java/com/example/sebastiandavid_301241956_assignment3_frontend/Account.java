package com.example.sebastiandavid_301241956_assignment3_frontend;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {
    private int accountNumber;
    private AccountType accountType;
    private Customer customer;
    private BigDecimal balance;
    private BigDecimal overDraftLimit;
}
