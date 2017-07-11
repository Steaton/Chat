package co.de.affinitas.chat.participants;

public class UserAlreadyConnectedException extends Exception {

    public UserAlreadyConnectedException(String message) {
        super(message);
    }
}
