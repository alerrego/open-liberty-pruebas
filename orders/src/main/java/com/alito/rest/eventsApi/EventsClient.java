package com.alito.rest.eventsApi;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.alito.rest.orders.dto.EventDTO;
import com.alito.rest.orders.dto.TicketsResponseDTO;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(baseUri="http://localhost:9081/api")
@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EventsClient {
    @GET
    @Path("/{id}")
    EventDTO getEventById(@PathParam("id") Long id);

    @PUT
    @Path("/{id}/decrease-stock")
    void decreaseStock(@PathParam("id") Long id,TicketsResponseDTO purchasedTickets);
}
