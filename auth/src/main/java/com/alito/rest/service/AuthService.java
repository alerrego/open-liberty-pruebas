package com.alito.rest.service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.alito.rest.dto.ForgotPasswordDTO;
import com.alito.rest.dto.RegisterRequestDTO;
import com.alito.rest.model.User;
import com.alito.rest.repository.UserRepository;
import com.alito.rest.security.JwtProvider;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {
    @Inject
    UserRepository userRepository;

    @Inject
    JwtProvider jwtProvider;


    @Inject
    @ConfigProperty(name = "rabbitmq.queue", defaultValue = "recovery-mail-queue")
    String queueName;


    public String login(String email, String password) throws Exception {
        Optional<User> optUser = userRepository.findByEmail(email);

        if(optUser.isEmpty() || !optUser.get().getPassword().equals(password)){
            throw new Exception("Credentials are invalid"); //OJO LA SEGUNDA COND EN CASO DE USAR UNA BCRYPT HABRIA QUE USAR UN DESENCRIPTADOR
        }

        return jwtProvider.generateToken(optUser.get());
    }

    public User register(RegisterRequestDTO registerRequestDTO) throws Exception {
        if(userRepository.findByEmail(registerRequestDTO.email).isPresent()){
            throw new Exception("Email is alredy in use");
        }

        User newUser = new User();
        newUser.setEmail(registerRequestDTO.email);
        newUser.setPassword(registerRequestDTO.password); //ACA SE DEBERIA HACER UN HASH DE PASSWORD
        newUser.setRol("USER"); // ASI NO PUEDEN REGISTRAR ADMIN, LA IDEA DEL ADMIN SERIA DARLO DE ALTA MANUALMENTE EN LA BD


        userRepository.save(newUser);
        return newUser;

    }

    public void forgotPassword(ForgotPasswordDTO forgotPasswordDTO){
        Optional<User> user = userRepository.findByEmail(forgotPasswordDTO.getEmail());

        if(user.isEmpty()){
            return;
        }

        //ACA DEBERIA HABER UNA LOGICA O UN BOOLEAN PARA MANTENER UN TOKEN DE USO UNICO O ALGO POR EL ESTILO PARA EL CAMBIO DE CONTRASEÑA
        //POR AHORA COMO ES A MODO DE PRACTICA SOLAMENTE ME QUEDO CON ESTABLECER LA CONEXION Y ENVIAR EL MENSAJE

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicPublish("", queueName, null, forgotPasswordDTO.getEmail().getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + forgotPasswordDTO.getEmail() + "'");
        }catch(Exception e){
            throw new RuntimeException("No se pudo encolar el mensaje de recuperación", e);
        }
    }
}
