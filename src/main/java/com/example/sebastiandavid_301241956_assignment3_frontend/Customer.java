package com.example.sebastiandavid_301241956_assignment3_frontend;

import lombok.*;

import java.time.LocalDate;

@Data

@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Long customerId;
    private String customerName;
    private String username;
    private String password;
    private String email;
    private LocalDate dob;
    private String address;
    private String phone;

    private Boolean admin;
}
