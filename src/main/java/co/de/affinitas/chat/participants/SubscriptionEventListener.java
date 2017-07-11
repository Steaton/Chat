package co.de.affinitas.chat.participants;

import co.de.affinitas.chat.participants.model.ConnectedUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

//@Component
public class SubscriptionEventListener implements ApplicationListener<SessionSubscribeEvent> {

    @Autowired
    private SimpMessagingTemplate template;

    private ConnectedUsers connectedUsers = new ConnectedUsers();

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        Object source = sessionSubscribeEvent.getSource();
        template.convertAndSend("/topic/chat.participants", connectedUsers.connectedUserList());
    }
}
