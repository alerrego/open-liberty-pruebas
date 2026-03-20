package com.alito.rest.ordersApi;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.alito.rest.ordersApi.dto.OrderDTO;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@RegisterRestClient(baseUri="http://localhost:9082/api")
@RegisterClientHeaders //ESTE VA PARA QUE ENVIE EL JWT AUTOMATICAMENTE
@Path("/orders")
public interface OrdersClient {
    @GET
    @Path("/{id}")
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000) //SI DE 4 PETICIONES, FALLAN 2, ABRIMOS EL CIRCUITO POR 5 SEG
    @Fallback(fallbackMethod = "fallbackGetOrderById") //SI EL CIRCUITO ESTA ABIERTO EJECUTAMOS ESTE METODO
    OrderDTO getOrderById(@PathParam("id") Long id);

    default OrderDTO fallbackGetOrderById(Long id) {
        OrderDTO order = new OrderDTO();
        order.setId(id);
        // LE SETEAMOS ESTE ESTADO PARA PODER HACER EL MANEJO LUEGO
        order.setStatus("SERVICE_UNAVAILABLE"); 
        return order;
    }

    @PUT
    @Path("/{id}")
    void confirmPurchase(@PathParam("id") Long id);
}
