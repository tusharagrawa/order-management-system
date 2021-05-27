package com.intuit.oms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    private Long customerId;
    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private String customerPhoneNumber;

}
