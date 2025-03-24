package com.example.sebastiandavid_301241956_assignment3_frontend;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountType {
    private int accountTypeId;
    private String accountTypeName;
    private String accountTypeDescription;
    private BigDecimal minimumBalance;
    private boolean hasOverdraft;
}
