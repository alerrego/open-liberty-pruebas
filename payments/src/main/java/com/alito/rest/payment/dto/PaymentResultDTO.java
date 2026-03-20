package com.alito.rest.payment.dto;

public class PaymentResultDTO {
    private String checkoutUrl;
    private String providerTransactionId;
    
    public PaymentResultDTO(String checkoutUrl, String providerTransactionId) {
        this.checkoutUrl = checkoutUrl;
        this.providerTransactionId = providerTransactionId;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public String getProviderTransactionId() {
        return providerTransactionId;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public void setProviderTransactionId(String providerTransactionId) {
        this.providerTransactionId = providerTransactionId;
    }

    
}
