package com.zenith.payment_gateway.dto;

import lombok.Data;

@Data
public class CreatePaymentLinkRequest {
    private String customerName;
    private String customerEmail;
    private Double amount;
    private String description;
}
