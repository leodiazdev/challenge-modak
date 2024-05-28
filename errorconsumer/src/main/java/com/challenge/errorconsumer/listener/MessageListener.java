package com.challenge.errorconsumer.listener;

import com.challenge.errorconsumer.domain.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @RabbitListener(queues = "messages", messageConverter = "consumerJackson2MessageConverter")
    public void receiveMessage(Message message) {
        System.out.println("Received message from queue: ");
        System.out.println("Type: " + message.getType());
        System.out.println("Content: " + message.getContent());
        System.out.println("UserID: " + message.getUserId());
    }
}
