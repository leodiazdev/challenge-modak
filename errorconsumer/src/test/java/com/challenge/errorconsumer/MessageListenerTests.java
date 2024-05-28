package com.challenge.errorconsumer;

import com.challenge.errorconsumer.domain.Message;
import com.challenge.errorconsumer.listener.MessageListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class MessageListenerTests {

    @InjectMocks
    private MessageListener messageListener;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testReceiveMessage() {
        Message mockMessage = new Message("user1", "news", "Hello World");

        // Act: simula la recepción del mensaje
        messageListener.receiveMessage(mockMessage);

        // Assert: verifica que los datos correctos se imprimen en la consola
        String output = outContent.toString();
        assertTrue(output.contains("Received message from queue:"));
        assertTrue(output.contains("Type: news"));
        assertTrue(output.contains("Content: Hello World"));
        assertTrue(output.contains("UserID: user1"));
    }
}
