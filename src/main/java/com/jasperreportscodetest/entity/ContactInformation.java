package com.jasperreportscodetest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Embeddable
public class ContactInformation {
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;
}
