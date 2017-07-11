package co.de.affinitas.chat.messaging;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    @MessageMapping("/chat.message")
    public void sendMessage() {
        System.out.println("I am arrived!!!!");
    }
}
