package co.de.affinitas.chat.participants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class WebSocketSessionEventListenerTest {

    private WebSocketSessionEventListener eventListener = new WebSocketSessionEventListener();

    @Before
    public void setup() {

    }

    @Test
    public void test_that_a_user_can_connect() {
        SessionConnectEvent connectEvent = mock(SessionConnectEvent.class);
        SimpMessagingTemplate simpMessagingTemplate = mock(SimpMessagingTemplate.class);
        eventListener.handleSessionConnected(connectEvent);
    }
}