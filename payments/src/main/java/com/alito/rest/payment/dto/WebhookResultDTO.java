package com.alito.rest.payment.dto;

public class WebhookResultDTO {
    private Long localPaymentId; //TIENE ESE LOCAL PQ ESTO ES PARA NUESTRO PAGO ALMACENADO EN LA BD NUESTRA
    private String status;
    
    public WebhookResultDTO(Long localPaymentId, String status) {
        this.localPaymentId = localPaymentId;
        this.status = status;
    }

    public Long getLocalPaymentId() {
        return localPaymentId;
    }

    public void setLocalPaymentId(Long localPaymentId) {
        this.localPaymentId = localPaymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
