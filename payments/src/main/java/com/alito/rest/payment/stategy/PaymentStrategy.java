package com.alito.rest.payment.stategy;

import com.alito.rest.ordersApi.dto.OrderDTO;
import com.alito.rest.payment.dto.PaymentResultDTO;
import com.alito.rest.payment.dto.WebhookResultDTO;
import com.alito.rest.payment.model.Payment;

public interface PaymentStrategy {
    PaymentResultDTO processPayment(OrderDTO order, Payment payment) throws Exception;

    WebhookResultDTO processWebhook(String payload) throws Exception;

    String getProviderName();
}
