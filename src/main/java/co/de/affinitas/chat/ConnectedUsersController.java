package co.de.affinitas.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.user.MultiServerUserRegistry;
import org.springframework.stereotype.Controller;

@Controller
public class ConnectedUsersController {

    @MessageMapping("/connect")
    public void connect(String username) {
        System.out.println(username);
        MultiServerUserRegistry multiServerUserRegistry; // Use this to keep track of all users across multiple nodes
    }

 }
