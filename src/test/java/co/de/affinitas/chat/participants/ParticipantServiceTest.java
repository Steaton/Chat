package co.de.affinitas.chat.participants;

import co.de.affinitas.chat.participants.exception.NotAlphaNumericException;
import co.de.affinitas.chat.participants.exception.UserAlreadyConnectedException;
import co.de.affinitas.chat.participants.service.ParticipantService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ParticipantServiceTest {

    private ParticipantService eventListener = new ParticipantService();

    @Before
    public void setup() {

    }

    @Test
    public void test_that_a_user_can_connect() throws NotAlphaNumericException, UserAlreadyConnectedException {
        SessionConnectEvent connectEvent = mock(SessionConnectEvent.class);
        SimpMessagingTemplate simpMessagingTemplate = mock(SimpMessagingTemplate.class);
        // eventListener.handleSessionConnected(connectEvent);
    }
}