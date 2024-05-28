package com.challenge.errorproducer;

import com.challenge.errorproducer.domain.Message;
import com.challenge.errorproducer.producer.MessageProducer;
import com.challenge.errorproducer.utils.RateLimiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MessageProducerTests {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RateLimiter redisRateLimiter;

    @InjectMocks
    private MessageProducer messageProducer;

    @Test
    void sendMessage_whenAllowed() {
        // Prepare system output capture
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Setup
        Message message = new Message("user1", "news", "Test message");
        when(redisRateLimiter.isAllowed(message.getUserId(), message.getType())).thenReturn(true);

        // Act
        messageProducer.sendMessage(message);

        // Assert
        verify(rabbitTemplate).convertAndSend("messages", message);
        String output = outContent.toString();
        assert(output.contains("Sent message: Test message"));
    }

    @Test
    void doNotSendMessage_whenNotAllowed() {
        // Prepare system output capture
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Setup
        Message message = new Message("user1", "news", "Test message");
        when(redisRateLimiter.isAllowed(message.getUserId(), message.getType())).thenReturn(false);

        // Act
        messageProducer.sendMessage(message);

        // Assert
        verify(rabbitTemplate, never()).convertAndSend(anyString(), Optional.ofNullable(any()));
        String output = outContent.toString();
        assert(output.contains("Limit reached, message not sent: Test message"));
    }
}
