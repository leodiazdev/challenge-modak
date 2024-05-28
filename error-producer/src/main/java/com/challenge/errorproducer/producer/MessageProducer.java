package com.challenge.errorproducer.producer;

import com.challenge.errorproducer.domain.Message;
import com.challenge.errorproducer.utils.RateLimiter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
    private final RabbitTemplate rabbitTemplate;
    private final RateLimiter rateLimiter;

    @Autowired
    public MessageProducer(RabbitTemplate rabbitTemplate, RateLimiter rateLimiter) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        this.rateLimiter = rateLimiter;
    }

    public void sendMessage(Message message) {
        if (rateLimiter.isAllowed(message.getUserId(), message.getType())) {
            rabbitTemplate.convertAndSend("messages", message);
            System.out.println("Sent message: " + message.getContent());
        } else {
            System.out.println("Limit reached, message not sent: " + message.getContent());
        }
    }
}
