package co.seaton.chat.messaging.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChatMessageTest {

    @Test
    public void test_that_message_is_formatted_correctly() throws Exception {
        // When
        ChatMessage chatMessage = new ChatMessage("This is the message!", "Stephen");

        // Then
        String expected = "Stephen (" + chatMessage.getDateString() + "): This is the message!";
        assertEquals(expected, chatMessage.toString());
    }
}