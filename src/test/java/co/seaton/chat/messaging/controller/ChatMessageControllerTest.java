package co.seaton.chat.messaging.controller;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ChatMessageControllerTest {

    @Test
    public void test_that_controller_sends_message_as_string() {
        // Given
        ChatMessageController controller = new ChatMessageController();

        // When
        String message = controller.sendMessage("Hello!", "Stephen");

        // Then
        assertNotNull(message);
    }
}