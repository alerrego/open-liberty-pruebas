package com.alito.rest.payment.stategy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import com.alito.rest.ordersApi.dto.OrderDTO;
import com.alito.rest.payment.dto.PaymentResultDTO;
import com.alito.rest.payment.dto.WebhookResultDTO;
import com.alito.rest.payment.model.Payment;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@ApplicationScoped
public class MercadoPagoStrategy implements PaymentStrategy{

    public MercadoPagoStrategy() {
        MercadoPagoConfig.setAccessToken("APP_USR-4050853833205568-031912-0c39797e37d162d75ed3a86a26406440-3006513682"); //ESTO TIENE QUE IR EN VARIABLES FUERA DEL ALCANCE DEL USUARIO
    }

    @Override
    public PaymentResultDTO processPayment(OrderDTO order, Payment localPayment) throws Exception {
        // ARMAMOS EL ITEM CON EL PRECIO REAL OBTENIDO DESDE ORDENES
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
            .id(order.getId().toString())
            .title("Entradas Evento #" + order.getEventId())
            .quantity(1)
            .unitPrice(new BigDecimal(order.getTotalAmount()))
            .currencyId("ARS")
            .build();

        // ARMAMOS LA PREFERENCIA, ES DECIR EL COBRO CON MP
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
            .items(Collections.singletonList(itemRequest))
            
            //ACA LA URL DEBE SER UNA REAL, PARA DEV USAMOS NGROK
            .externalReference(localPayment.getId().toString()) 
            .notificationUrl("https://georgene-unquixotical-maida.ngrok-free.dev/api/payments/webhook/MERCADOPAGO")
            .build();

        // LLAMAMOS A MERCADOPAGO Y CREAMOS LA REFERENCIA
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        // DEVOLVEMOS LA URL DE PAGO Y EL ID DE PREFERENCIA
        return new PaymentResultDTO(preference.getInitPoint(), preference.getId());
    }

    

    @Override
    public WebhookResultDTO processWebhook(String payload) throws Exception {
        //CONVIERTO EL STRING CRUDO A UN JSON
        Jsonb jsonb = JsonbBuilder.create();
        java.util.Map<String, Object> jsonMap = jsonb.fromJson(payload, java.util.Map.class);
        
        String action = (String) jsonMap.get("action");
        
        //SI NO ES UN AVISO DE PAGO SE IGNORA
        if (!"payment.created".equals(action) && !"payment.updated".equals(action)) {
            return new WebhookResultDTO(null, "IGNORED");
        }

        //SACAMOS EL ID
        Map<String, Object> data = (Map<String, Object>) jsonMap.get("data");
        Long mpPaymentId = Long.valueOf(data.get("id").toString());

        //CONSULTAMOS CON MP SOBRE EL PAGO, PQ EL WEBHOOK PUEDE SER ALCANZADO POR CUALQUIERA
        PaymentClient client = new PaymentClient();
        com.mercadopago.resources.payment.Payment mpPayment = client.get(mpPaymentId);

        String localPaymentIdStr = mpPayment.getExternalReference();
        if (localPaymentIdStr == null) return new WebhookResultDTO(null, "IGNORED");

        //REALIZAMOS LA TRADUCCION DEL ESTADO DE MP A ALGO GENERICO QUE MANEJAMOS NOSOTROS PARA TODOS LOS TIPOS DE PAGO, ASI EL SERVICE PUEDE USARLO
        String finalStatus = "PENDING";
        if ("approved".equals(mpPayment.getStatus())) {
            finalStatus = "APPROVED";
        } else if ("rejected".equals(mpPayment.getStatus())) {
            finalStatus = "REJECTED";
        }

        return new WebhookResultDTO(Long.valueOf(localPaymentIdStr), finalStatus);
    }

    @Override
    public String getProviderName() {
        return "MERCADOPAGO";
    }
    
}
