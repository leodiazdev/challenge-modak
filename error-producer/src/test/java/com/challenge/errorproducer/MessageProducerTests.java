package com.challenge.errorproducer;

import com.challenge.errorproducer.domain.Message;
import com.challenge.errorproducer.exceptions.RateLimitExceededException;
import com.challenge.errorproducer.producer.MessageProducer;
import com.challenge.errorproducer.utils.RateLimiter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@ExtendWith(MockitoExtension.class)
public class MessageProducerTests {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RateLimiter rateLimiter;

    @InjectMocks
    private MessageProducer messageProducer;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void sendMessage_whenAllowed() {
        Message message = new Message("user1", "news", "Test message");
        when(rateLimiter.isAllowed(message.getUserId(), message.getType())).thenReturn(true);

        messageProducer.sendMessage(message);

        verify(rabbitTemplate).convertAndSend("messages", message);
        assertTrue(outContent.toString().contains("Sent message: Test message"));
    }

    @Test
    void doNotSendMessage_whenNotAllowed() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Message message = new Message("user1", "news", "Test message");
        when(rateLimiter.isAllowed(message.getUserId(), message.getType())).thenReturn(false);

        // Expect the RateLimitExceededException to be thrown
        assertThrows(RateLimitExceededException.class, () -> {
            messageProducer.sendMessage(message);
        });

        // Verify that no message was sent
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(Message.class));
        String output = outContent.toString();
        assertTrue(output.contains("Limit reached, message not sent: Test message"));
    }
}
