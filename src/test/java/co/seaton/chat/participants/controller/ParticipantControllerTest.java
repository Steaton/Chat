package co.seaton.chat.participants.controller;

import co.seaton.chat.participants.exception.AlreadyConnectedException;
import co.seaton.chat.participants.exception.NotAlphaNumericException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParticipantControllerTest {

    @Mock
    Message<byte[]> message;

    @Mock
    SimpMessagingTemplate template;

    private Map<String, Object> headers;

    @InjectMocks
    private ParticipantController participantController;

    @Before
    public void setup() {
        participantController.disconnectAll();
        headers = new HashMap<String,Object>();
    }

    @Test
    public void test_that_a_user_can_connect() throws NotAlphaNumericException, AlreadyConnectedException {
        // Given
        mockMessageHeader("123", "Stephen");

        // When
        participantController.handleSessionConnect(new SessionConnectEvent(participantController, message));

        // Then
        assertConnectedUsers("Stephen");
    }

    @Test
    public void test_that_a_user_can_disconnect() throws NotAlphaNumericException, AlreadyConnectedException {
        // Given
        mockMessageHeader("123", "Stephen");
        participantController.handleSessionConnect(new SessionConnectEvent(participantController, message));

        // When
        participantController.handleSessionDisconnect(new SessionDisconnectEvent(participantController, message, "123", CloseStatus.NORMAL));

        // Then
        assertConnectedUsers();
        verify(template).convertAndSend("/topic/chat.participants", new ArrayList<String>());
    }

    @Test
    public void test_multiple_disconnects_of_same_user() throws NotAlphaNumericException, AlreadyConnectedException {
        // Given
        mockMessageHeader("XYZ", "John");
        participantController.handleSessionConnect(new SessionConnectEvent(participantController, message));
        mockMessageHeader("123", "Stephen");
        participantController.handleSessionConnect(new SessionConnectEvent(participantController, message));

        // When
        participantController.handleSessionDisconnect(new SessionDisconnectEvent(participantController, message, "123", CloseStatus.NORMAL));
        participantController.handleSessionDisconnect(new SessionDisconnectEvent(participantController, message, "123", CloseStatus.NORMAL));

        // Then
        assertConnectedUsers("John");
        verify(template, times(2)).convertAndSend("/topic/chat.participants", Arrays.asList("John"));

    }

    private void mockMessageHeader(String sessionId, String username) {
        mockSessionIdHeader(sessionId);
        mockUsernameHeader(username);
        when(message.getHeaders()).thenReturn(new MessageHeaders(headers));
    }

    private void mockSessionIdHeader(String sessionId) {
        headers.put(SimpMessageHeaderAccessor.SESSION_ID_HEADER, sessionId);
    }

    private void mockUsernameHeader(String username) {
        HashMap<String, Object> nativeHeaders = new HashMap<>();
        nativeHeaders.put("username", Arrays.asList(username));
        headers.put(SimpMessageHeaderAccessor.NATIVE_HEADERS, nativeHeaders);
    }

    private void assertConnectedUsers(String... users) {
        ArrayList<String> expectedUsers = participantController.sendConnectedParticipants();
        assertEquals(expectedUsers.size(), users.length);
        assertTrue(expectedUsers.containsAll(Arrays.asList(users)));
    }
}