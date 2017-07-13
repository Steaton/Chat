package co.seaton.chat.participants.model;

import co.seaton.chat.participants.exception.AlreadyConnectedException;
import co.seaton.chat.participants.exception.NotAlphaNumericException;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.VisibleForTesting;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.util.Preconditions.checkNotNullOrEmpty;

public class ConnectedUsers {

    // Scalability Issues: Wouldn't work in a cluster.
    // Code Issue: Static stuff is difficult to test hence temporary @VisibleForTesting methods creep in.
    // Alternative: e.g. RabbitMQ, Database - this would then become a synchronised cache
    private static Map<String, String> connectedUsers = new ConcurrentHashMap<>();

    public void joinChat(String sessionId, String username) throws AlreadyConnectedException, NotAlphaNumericException {
        validateUsername(sessionId, username);
        connectedUsers.put(sessionId, username);
    }

    private void validateUsername(String sessionId, String username) throws AlreadyConnectedException, NotAlphaNumericException {
        checkNotNullOrEmpty(sessionId);
        checkNotNullOrEmpty(username);
        checkNotAlreadyConnected(sessionId);
        checkAlphaNumeric(username);
    }

    private void checkNotAlreadyConnected(String sessionId) throws AlreadyConnectedException {
        if (connectedUsers.containsKey(sessionId)) {
            throw new AlreadyConnectedException("Session already connected: " + sessionId);
        }
    }

    private void checkAlphaNumeric(String username) throws NotAlphaNumericException {
        if (!StringUtils.isAlphanumeric(username)) {
            throw new NotAlphaNumericException("Username must be alphanumeric but is username: " + username);
        }
    }

    public void leaveChat(String sessionId) {
        connectedUsers.remove(sessionId);
    }

    public ArrayList<String> connectedUserList() {
        return new ArrayList<String>(connectedUsers.values());
    }

    @VisibleForTesting // A side effect of using static collection demonstrating the negative impact of static stuff on testability.
    public void disconnectAll() {
        connectedUsers = new ConcurrentHashMap<String, String>();
    }
}
