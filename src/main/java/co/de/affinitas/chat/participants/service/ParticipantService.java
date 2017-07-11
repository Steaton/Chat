package co.de.affinitas.chat.participants.service;

import co.de.affinitas.chat.participants.exception.NotAlphaNumericException;
import co.de.affinitas.chat.participants.exception.UserAlreadyConnectedException;
import co.de.affinitas.chat.participants.model.ConnectedUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Arrays;
import java.util.List;

@Controller
public class ParticipantService {

    @Autowired
    private SimpMessagingTemplate template;

    private ConnectedUsers connectedUsers = new ConnectedUsers();

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) throws NotAlphaNumericException, UserAlreadyConnectedException {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		String username = headers.getNativeHeader("username").get(0);
        connectedUsers.joinChat(username);
    }

	@EventListener
    @SendTo("/topic/chat.participants")
	public List<String> handleSessionDisconnect(SessionDisconnectEvent event) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
//		String username = headers.getNativeHeader("username").get(0);
//		connectedUsers.leaveChat(username);
        return sendConnectedParticipants();
	}

	@SubscribeMapping("/chat.participants")
    @SendTo("/topic/chat.participants")
    public List<String> sendConnectedParticipants() {
        return Arrays.asList("Steaton");
    }
}