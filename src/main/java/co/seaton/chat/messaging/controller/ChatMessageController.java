package co.seaton.chat.messaging.controller;

import co.seaton.chat.messaging.model.ChatMessage;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    @MessageMapping("/chat.message")
    @SendTo("/topic/chat.message")
    public String sendMessage(String message, @Header String username) {
        ChatMessage chatMessage = new ChatMessage(message, username);
        return chatMessage.toString();
    }
}
