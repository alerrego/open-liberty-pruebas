package com.alito.rest.payment.service;

import java.time.LocalDateTime;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.alito.rest.ordersApi.OrdersClient;
import com.alito.rest.ordersApi.dto.OrderDTO;
import com.alito.rest.payment.dto.PaymentRequestDTO;
import com.alito.rest.payment.dto.PaymentResultDTO;
import com.alito.rest.payment.dto.WebhookResultDTO;
import com.alito.rest.payment.model.Payment;
import com.alito.rest.payment.repository.PaymentRepository;
import com.alito.rest.payment.stategy.PaymentFactory;
import com.alito.rest.payment.stategy.PaymentStrategy;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class PaymentService {
    @Inject
    @RestClient
    OrdersClient ordersClient;

    @Inject
    PaymentRepository paymentRepository;

    @Inject
    PaymentFactory paymentFactory;

    @Inject
    Instance<JsonWebToken> jwt;

    @Transactional
    public String initiatePayment(PaymentRequestDTO paymentRequestDTO) throws Exception{
        OrderDTO order = ordersClient.getOrderById(paymentRequestDTO.getOrderId());

        if ("SERVICE_UNAVAILABLE".equals(order.getStatus())) {
            throw new WebApplicationException(
                Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("The ordering system is temporarily unavailable. Please try again in a few minutes.")
                        .build()
            );
        }

        String orderUserIdParsedString = String.valueOf(order.getBuyerUserId());

        if(!orderUserIdParsedString.equals(jwt.get().getSubject())){
            throw new WebApplicationException(
                Response.status(Response.Status.FORBIDDEN)
                        .entity("Access denied: This order is not yours.")
                        .build()
            );
        }

        Payment payment = new Payment(order.getId(), paymentRequestDTO.getProvider(), null, order.getTotalAmount(), "INITIATED",LocalDateTime.now());

        paymentRepository.save(payment);

        PaymentStrategy strategy = paymentFactory.getStrategy(paymentRequestDTO.getProvider());

        PaymentResultDTO result = strategy.processPayment(order, payment);

        payment.setProviderTransactionId(result.getProviderTransactionId());
        paymentRepository.update(payment);

        return result.getCheckoutUrl();
    }

    @Transactional
    public void processWebhook(String provider,String data) throws Exception{
        
        PaymentStrategy strategy = paymentFactory.getStrategy(provider);

        //ACA CADA ESTRATEGIA HACE SU TRABAJO
        WebhookResultDTO result = strategy.processWebhook(data);

        if (result == null || "IGNORED".equals(result.getStatus()) || result.getLocalPaymentId() == null) {
            return;
        }

        //BUSCAMOS EL PAGO
        Payment localPayment = paymentRepository.findById(result.getLocalPaymentId());
        if (localPayment == null) return;

        //ACTUALIZAMOS NUESTRO PAGO
        if ("APPROVED".equals(result.getStatus())) {
            
            if (!"APPROVED".equals(localPayment.getStatus())) {
                localPayment.setStatus("APPROVED");
                paymentRepository.update(localPayment);

                //LE AVISO A ORDENES QUE ESTA PAGA
                ordersClient.confirmPurchase(localPayment.getOrderId());
            }
            
        } else if ("REJECTED".equals(result.getStatus())) {
            localPayment.setStatus("REJECTED");
            paymentRepository.update(localPayment);
        }
    }
}
