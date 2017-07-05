package co.de.affinitas.chat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.websocket.Session;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChatEndpointTest {

    private static final String EXAMPLE_USERNAME_1 = "Stephen";
    private static final String EXAMPLE_USERNAME_2 = "David";

    private ChatEndpoint underTest;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setup() {
        underTest = new ChatEndpoint();
    }

    @Test
    public void test_that_a_user_can_connect() {
        // Assign
        Session mockSession = mockSession(EXAMPLE_USERNAME_1);

        // Act
        underTest.connect(mockSession);

        // Assert
        System.out.println(underTest.getConnectedUserUsernames());
        assertTrue(underTest.getConnectedUserUsernames().size() == 1);
        assertTrue(underTest.getConnectedUserUsernames().contains(EXAMPLE_USERNAME_1));
    }

    @Test
    public void test_that_multiple_users_can_connect() {
        // Assign
        Session mockSession1 = mockSession(EXAMPLE_USERNAME_1);
        Session mockSession2 = mockSession(EXAMPLE_USERNAME_2);

        // Act
        underTest.connect(mockSession1);
        underTest.connect(mockSession2);

        // Assert
        assertTrue(underTest.getConnectedUserUsernames().size() == 2);
        assertTrue(underTest.getConnectedUserUsernames().contains(EXAMPLE_USERNAME_1));
        assertTrue(underTest.getConnectedUserUsernames().contains(EXAMPLE_USERNAME_2));
    }

    @Test
    public void test_that_a_user_is_assigned_a_guest_username_if_none_is_provided() {
        // Not implemented yet
    }

    @Test
    public void test_that_an_exception_is_thrown_if_no_websocket_session_is_provided() {
        // Assign
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("A user session must be provided in order to connect.");

        // Act
        underTest.connect(null);
    }

    private Session mockSession(String username) {
        Session mockSession = mock(Session.class);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        when(mockSession.getUserProperties()).thenReturn(properties);
        return mockSession;
    }
}