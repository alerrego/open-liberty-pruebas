package com.alito.rest.orders.resource;

import java.util.List;

import com.alito.rest.orders.dto.OrderRequestDTO;
import com.alito.rest.orders.model.Order;
import com.alito.rest.orders.service.OrderService;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    @Inject
    OrderService orderService;

    @POST
    @RolesAllowed({"USER"})
    public Response createOrder(OrderRequestDTO orderRequestDTO){
        try {
            Order newOrder = orderService.createOrder(orderRequestDTO);
            return Response.status(Response.Status.CREATED)
                .entity("Order created succesfully with ID: "+newOrder.getId())
                .build();
        } catch (WebApplicationException e) {
            return e.getResponse();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR in system to process the order : "+e.getMessage())
                .build();
        }
    }

    @GET
    @RolesAllowed({"ADMIN","USER"})
    @Path("/me")
    public Response getMyOrders(){
        try {
            List<Order> myOrders = orderService.getMyOrders();
            return Response.ok(myOrders).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR in system to process the order : "+e.getMessage())
                .build();
        }
    }

    @GET
    @RolesAllowed({"ADMIN","USER"})
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") Long id){
        try {
            Order order = orderService.getOrderById(id);
            return Response.ok(order).build();
        } catch (WebApplicationException e) {
            return e.getResponse();
        }
    }

    @PUT
    @Path("/{id}")
    @PermitAll
    public Response confirmPurchase(@PathParam("id") Long id) {
        try {
            orderService.confirmPurchase(id);
            // Devolvemos un 200 OK genérico (void en tu interfaz del cliente)
            return Response.ok("ORDER " + id + " CONFIRMED AND PAID SUCCESSFUL.").build();
        } catch (WebApplicationException e) {
            return e.getResponse();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("ERROR TO CONFIRM ORDER: " + e.getMessage())
                           .build();
        }
    }
}
