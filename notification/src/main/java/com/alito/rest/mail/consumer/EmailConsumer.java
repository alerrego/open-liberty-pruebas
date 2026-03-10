package com.alito.rest.mail.consumer;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class EmailConsumer {
    // Inyectamos las variables desde el .properties
    @Inject
    @ConfigProperty(name = "rabbitmq.host", defaultValue = "localhost")
    String rabbitHost;

    @Inject
    @ConfigProperty(name = "rabbitmq.queue", defaultValue = "recovery-mail-queue")
    String queueName;

    public void iniciarEscucha(@Observes @Initialized(ApplicationScoped.class) Object init) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitHost);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(queueName, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
        };
        
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
