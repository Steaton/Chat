package co.seaton.chat.participants.controller;

import co.seaton.chat.participants.exception.AlreadyConnectedException;
import co.seaton.chat.participants.exception.NotAlphaNumericException;
import co.seaton.chat.participants.model.ConnectedUsers;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;

@Controller
public class ParticipantController {

    @Autowired
    private SimpMessagingTemplate template;

    protected ConnectedUsers connectedUsers = new ConnectedUsers();

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) throws NotAlphaNumericException, AlreadyConnectedException {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String sessionId = headers.getSessionId();
        String username = headers.getNativeHeader("username").get(0);
        connectedUsers.joinChat(sessionId, username);
    }

	@EventListener
	public void handleSessionDisconnect(SessionDisconnectEvent event) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		String sessionId = headers.getSessionId();
		connectedUsers.leaveChat(sessionId);
        template.convertAndSend("/topic/chat.participants", connectedUsers.connectedUserList());
	}

	@SubscribeMapping("/chat.participants")
    @SendTo("/topic/chat.participants")
    public ArrayList<String> sendConnectedParticipants() {
        return connectedUsers.connectedUserList();
    }

    @VisibleForTesting // This would not exist if I removed the static user store so I've exposed it in this example.
    public void disconnectAll() {
        connectedUsers.disconnectAll();
    }
}