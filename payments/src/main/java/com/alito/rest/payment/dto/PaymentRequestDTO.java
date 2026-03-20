package com.alito.rest.payment.dto;

public class PaymentRequestDTO {
    private Long orderId;

    private String provider;

    public PaymentRequestDTO() {
    }

    public PaymentRequestDTO(Long orderId, String provider) {
        this.orderId = orderId;
        this.provider = provider;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    
}
