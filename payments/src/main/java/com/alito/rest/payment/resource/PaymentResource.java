package com.alito.rest.payment.resource;

import com.alito.rest.payment.dto.PaymentRequestDTO;
import com.alito.rest.payment.service.PaymentService;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {
    @Inject
    PaymentService paymentService;

    @POST
    @Path("/create")
    @RolesAllowed({"ADMIN","USER"})
    public Response createPayment(PaymentRequestDTO paymentRequestDTO){
        try {
            String checkoutUrl = paymentService.initiatePayment(paymentRequestDTO);
            return Response.ok("{\"checkoutUrl\": \"" + checkoutUrl + "\"}").build();
        } catch (Exception e) {
           return Response.status(Response.Status.BAD_REQUEST)
                           .entity("ERROR STARTING PAYMENT: " + e.getMessage())
                           .build();
        }
    }

    @POST
    @Path("/webhook/{provider}")
    @PermitAll // ESTA ABIERTO PQ NECESITAMOS QUE LE PEGUEN
    //EL DATA ESTA COMO STRING Y NO UN DTO DEFINIDO ASI PUEDO TENER MUCHOS FORMATOS DISTINTOS EJ (MP,PAYPAL,ETC...)
    public Response handleWebhook(@PathParam("provider") String provider, String data) {
        try {
            paymentService.processWebhook(provider.toUpperCase(),data); //LO PARSEAMOS A MAYUS PARA NO TENER DRAMAS
            return Response.ok().build(); //ESTO VA A MP UN 200 LE MANDO PARA QUE NO ENVIE DEVUELTA EL POST
            
        } catch (Exception e) {
            System.err.println("ERROR TO PROCESS WEBHOOK: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); //LE RESPONDO CON ERROR 500 Y LUEGO LO ENVIA DEVUELTA
        }
    }
}
