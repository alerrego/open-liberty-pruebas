package com.alito.rest.resource;

import com.alito.rest.dto.ForgotPasswordDTO;
import com.alito.rest.dto.LoginRequestDTO;
import com.alito.rest.dto.RegisterRequestDTO;
import com.alito.rest.model.User;
import com.alito.rest.security.JwtProvider;
import com.alito.rest.service.AuthService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    @Inject
    AuthService authService;

    @Inject
    JwtProvider jwtProvider;

    @POST
    @Path("/register")
    public Response register(RegisterRequestDTO registerRequest){
        try {
            User u = authService.register(registerRequest);
            return Response.status(Response.Status.CREATED).entity(u).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .build();
        }
    }

    @POST
    @Path("/login")
    public Response login(LoginRequestDTO loginRequest){
        try {
            String token = authService.login(loginRequest.email, loginRequest.password);
            return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .build();
        }
    }

    //ACA SE OBTIENE LA CLAVE PUBLICA
    @GET
    @Path("/certs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCerts() {
        try {
            String jwksJson = jwtProvider.getPublicKeysJson();
            return Response.ok(jwksJson).build();
        } catch (Exception e) {
            return Response.serverError().entity("Error obteniendo las claves públicas").build();
        }
    }

    //LA IDEA ACA ES TAMBIEN AGREGAR ALGO COMO EN SPRING QUE LIMITE LA CANTIDAD DE PETICIONES REALIZADAS POR IP, ASI NO EXPLOTAN EL MAILING
    @POST
    @Path("/forgot-password")
    public Response forgotPassword(ForgotPasswordDTO forgotPasswordDTO){
        authService.forgotPassword(forgotPasswordDTO);
        return Response.ok("If you are register, recovery is send to "+forgotPasswordDTO.getEmail()).build();
    }
}
