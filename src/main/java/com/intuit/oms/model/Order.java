package com.intuit.oms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long orderId;
    private Long customerId;
    private Long productId;
    private Long productQuantity;
    private Double productPrice;
}
