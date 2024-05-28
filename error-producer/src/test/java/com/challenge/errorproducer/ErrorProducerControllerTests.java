package com.challenge.errorproducer;

import com.challenge.errorproducer.controller.ErrorProducerController;
import com.challenge.errorproducer.domain.Message;
import com.challenge.errorproducer.producer.MessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ErrorProducerController.class)
public class ErrorProducerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageProducer producer;

    @Test
    public void sendMessage_ReturnsSuccessResponse_WhenMessageSent() throws Exception {
        // Prepare JSON input
        String jsonMessage = "{\"userId\":\"user1\",\"type\":\"news\",\"content\":\"Hello World\"}";

        // Mock the behavior of MessageProducer to simulate successful message sending
        doNothing().when(producer).sendMessage(any(Message.class));

        // Perform POST request
        mockMvc.perform(post("/api/messages/send-message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMessage))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Message sent successfully"));

        // Verify interactions
        verify(producer).sendMessage(any(Message.class));
    }

    @Test
    public void sendMessage_ReturnsErrorResponse_WhenExceptionThrown() throws Exception {
        String jsonMessage = "{\"userId\":\"user1\",\"type\":\"news\",\"content\":\"Hello World\"}";

        // Mock the behavior to throw an exception
        doThrow(new RuntimeException("Failed to send message")).when(producer).sendMessage(any(Message.class));

        // Perform POST request
        mockMvc.perform(post("/api/messages/send-message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMessage))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Failed to send message"));

        // Verify interactions
        verify(producer).sendMessage(any(Message.class));
    }
}
