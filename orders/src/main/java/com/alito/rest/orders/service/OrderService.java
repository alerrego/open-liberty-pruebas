package com.alito.rest.orders.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.alito.rest.eventsApi.EventsClient;
import com.alito.rest.orders.dto.EventDTO;
import com.alito.rest.orders.dto.OrderRequestDTO;
import com.alito.rest.orders.dto.TicketDTO;
import com.alito.rest.orders.dto.TicketTypesDTO;
import com.alito.rest.orders.dto.TicketsResponseDTO;
import com.alito.rest.orders.model.Order;
import com.alito.rest.orders.model.Ticket;
import com.alito.rest.orders.repository.OrderRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class OrderService {

    @Inject
    private OrderRepository orderRepository;

    @Inject
    JsonWebToken jwt;

    @Inject
    @RestClient
    EventsClient eventsClient;

    @Transactional
    public Order createOrder(OrderRequestDTO orderRequestDTO){
        List<String> dnis = orderRequestDTO.getTickets().stream()
            .map(TicketDTO::getDni)
            .collect(Collectors.toList());

        if(orderRepository.ifExistDniInTickets(orderRequestDTO.getEventId(), dnis)){
            throw new WebApplicationException(
                Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: one or more dnis alredy have tickets associated with the event")
                    .build()
            );
        }

        EventDTO event = eventsClient.getEventById(orderRequestDTO.getEventId());
        Double totalAmount = 0.0;

        Long buyerId = Long.valueOf(jwt.getSubject());

        Order order = new Order(event.getId(),buyerId,LocalDateTime.now(),"PENDING");
        List<Ticket> tickets = new ArrayList<>();

        for (TicketDTO tDTO : orderRequestDTO.getTickets()){
            Ticket ticket = new Ticket(orderRequestDTO.getEventId(), tDTO.getDni(), tDTO.getGuestName(), tDTO.getTicketType(), order);
            tickets.add(ticket);

            //ENCUENTRO EL TIPO DE ENTRADA QUE ES, SI NO EXISTE RETORNO EL ERROR
            TicketTypesDTO ticketType = event.getTicketTypes().stream()
                .filter(t -> t.getName().equals(ticket.getTicketType()))
                .findFirst()
                .orElseThrow(() -> new WebApplicationException("Invalid ticket type", 400));
            
            //SI NO TENGO STOCK RETORNO CON ERROR
            if(ticketType.getStock() <= 0){
                throw new WebApplicationException("THERE IS NOT ENOUGH STOCK FOR THE TYPE OF TICKET WITH ID: "+ticketType.getId(),400);
            }

            totalAmount += ticketType.getPrice();
        }

        order.setTickets(tickets);
        order.setTotalAmount(totalAmount);

        //ANTES DE GUARDARLO EN LA BD LE HAGO EL PUT A EVENTOS PARA DECREMENTAR EL STOCK Y YA DEJAR RESERVADAS LOS TICKETS
        eventsClient.decreaseStock(event.getId(), new TicketsResponseDTO(orderRequestDTO.getTickets()));
        
        orderRepository.save(order);
        return order;
    }

    public List<Order> getMyOrders(){
        Long buyerId = Long.valueOf(jwt.getSubject());

        return orderRepository.findByBuyerId(buyerId);
    }

    public Order getOrderById(Long id){
        Order order = orderRepository.findById(id);

        if(order == null){
            throw new WebApplicationException(
                Response.status(Response.Status.NOT_FOUND).entity("Order not found.").build()
            );
        }

        return order;
    }

    @Transactional
    public void confirmPurchase(Long id){
        Order order = orderRepository.findById(id);

        if(order == null){
            throw new WebApplicationException(
                Response.status(Response.Status.NOT_FOUND).entity("Order not found.").build()
            );
        }

        order.setStatus("CONFIRMED"); //AL USAR @TRANSACTIONAL CUANDO HACEMOS EL SET HIBERNATE EJECUTA UN UPDATE AUTOMATICAMENTE EN LA BD
    }
}
