package co.de.affinitas.chat;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/chat")
public class ChatEndpoint {

    private static Set<Session> connectedUsers = Collections.synchronizedSet(new HashSet<Session>());

    public ChatEndpoint() {
        connectedUsers = Collections.synchronizedSet(new HashSet<Session>());
    }

    @OnOpen
    public void connect(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("A user session must be provided in order to connect.");
        }
        connectedUsers.add(session);
    }

    public Set<String> getConnectedUserUsernames() {
        Set<String> usernames = new HashSet<>();
        for (Session connectedUser : connectedUsers) {
            String username = (String) connectedUser.getUserProperties().get("username");
            usernames.add(username);
        }
        return usernames;
    }
}
