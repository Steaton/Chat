package co.de.affinitas.chat.participants.model;

import co.de.affinitas.chat.participants.exception.NotAlphaNumericException;
import co.de.affinitas.chat.participants.exception.UserAlreadyConnectedException;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.VisibleForTesting;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.util.Preconditions.checkNotNullOrEmpty;

public class ConnectedUsers {

    // Using a static collection goes against the grain somewhat but in the interests of keeping things simple I've opted
    // to do this with a view to being able to upgrade it to use a backend such as RabbitMQ or a database.
    private static Set<String> connectedUsers = Collections.synchronizedSet(new HashSet<>());

    public void joinChat(String username) throws UserAlreadyConnectedException, NotAlphaNumericException {
        validateUsername(username);
        connectedUsers.add(username);
    }

    private void validateUsername(String username) throws UserAlreadyConnectedException, NotAlphaNumericException {
        checkNotNullOrEmpty(username);
        checkNotAlreadyConnected(username);
        checkAlphaNumeric(username);
    }

    private void checkNotAlreadyConnected(String username) throws UserAlreadyConnectedException {
        if (connectedUsers.contains(username)) {
            throw new UserAlreadyConnectedException("User already connected: " + username);
        }
    }

    private void checkAlphaNumeric(String username) throws NotAlphaNumericException {
        if (!StringUtils.isAlphanumeric(username)) {
            throw new NotAlphaNumericException("Username must be alphanumeric but is username: " + username);
        }
    }

    public void leaveChat(String username) {
        connectedUsers.remove(username);
    }

    public Set<String> connectedUserList() {
        return connectedUsers;
    }

    @VisibleForTesting // A side effect of using static collection demonstrating the negative impact of static stuff on testability.
    public void disconnectAll() {
        connectedUsers = Collections.synchronizedSet(new HashSet<>());
    }
}
