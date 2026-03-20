package com.alito.rest.event.resource;

import com.alito.rest.event.dto.EventDTO;
import com.alito.rest.event.dto.TicketsRequestDTO;
import com.alito.rest.event.model.Event;
import com.alito.rest.event.service.EventService;

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

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {
    @Inject
    EventService eventService;

    @POST
    @RolesAllowed("ADMIN")
    public Response create(EventDTO eventDTO){
        Event newEvent = eventService.createEvent(eventDTO);
        return Response.status(Response.Status.CREATED).entity(newEvent).build();
    }

    @GET
    @PermitAll
    public Response getAll(){
        return Response.ok(eventService.getAllEvents()).build();
    }

    @GET
    @Path("/{id}")
    @PermitAll //SOLO PARA PRUEBAS ESTO
    public Response getEvent(@PathParam("id") Long id){
        Event e = eventService.getEventById(id);
        return Response.ok(e).build();
    }

    @PUT
    @Path("/{id}/decrease-stock")
    @PermitAll //SOLO PARA PRUEBAS ESTO
    public Response decreaseStock(@PathParam("id") Long id,TicketsRequestDTO requestDTO){
        try {
            eventService.decreaseStock(id, requestDTO);
            return Response.ok("Stock decrease succesful").build();
            
        } catch (WebApplicationException e) {
            return e.getResponse();
        }
    }
}
