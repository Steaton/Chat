package co.seaton.chat.participants.model;

import co.seaton.chat.participants.exception.AlreadyConnectedException;
import co.seaton.chat.participants.exception.NotAlphaNumericException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConnectedUsersTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ConnectedUsers connectedUsers;

    @Before
    public void setup() {
        connectedUsers = new ConnectedUsers();
        connectedUsers.disconnectAll();
    }

    @Test
    public void test_that_username_cannot_be_null() throws NotAlphaNumericException, AlreadyConnectedException {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Argument expected not to be empty!");

        // When
        connectedUsers.joinChat("123", null);
    }

    @Test
    public void test_that_sessionId_cannot_be_null() throws NotAlphaNumericException, AlreadyConnectedException {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Argument expected not to be empty!");

        // When
        connectedUsers.joinChat(null, "Stephen");
    }

    @Test
    public void test_that_username_cannot_be_empty() throws NotAlphaNumericException, AlreadyConnectedException {
        // Expect
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Argument expected not to be empty!");

        // When
        connectedUsers.joinChat("123", "");
    }

    @Test
    public void test_that_sessionId_cannot_be_empty() throws NotAlphaNumericException, AlreadyConnectedException {
        // Expect
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Argument expected not to be empty!");

        // When
        connectedUsers.joinChat("", "123");
    }

    @Test
    public void test_that_session_not_already_connected() throws NotAlphaNumericException, AlreadyConnectedException {
        // Expect
        expectedException.expect(AlreadyConnectedException.class);
        expectedException.expectMessage("Session already connected: 123");

        // When
        connectedUsers.joinChat("123", "Stephen");
        connectedUsers.joinChat("123", "John");
    }

    @Test
    public void test_that_username_must_be_alphanumeric() throws NotAlphaNumericException, AlreadyConnectedException {
        // Expect
        expectedException.expect(NotAlphaNumericException.class);
        expectedException.expectMessage("Username must be alphanumeric but is username: #!@");

        // When
        connectedUsers.joinChat("122","#!@");
    }

    @Test
    public void test_that_user_can_connect_to_chat() throws NotAlphaNumericException, AlreadyConnectedException {
        // When
        connectedUsers.joinChat("123", "Stephen");

        // Then
        assertConnectedUsers("Stephen");
    }

    @Test
    public void test_that_multiple_users_can_connect_at_the_same_time() throws NotAlphaNumericException, AlreadyConnectedException {
        // When
        connectedUsers.joinChat("123", "Stephen");
        connectedUsers.joinChat("xyz", "Mike");

        // Then
        assertConnectedUsers("Stephen", "Mike");
    }

    @Test
    public void test_that_nothing_happens_if_user_disconnects_when_not_even_connected() throws NotAlphaNumericException, AlreadyConnectedException {
        // Given
        connectedUsers.joinChat("123", "Stephen");

        // When
        connectedUsers.leaveChat("xyz");

        // Then
        assertConnectedUsers("Stephen");
    }

    @Test
    public void test_that_nothing_happens_if_user_disconnects_multiple_times() throws NotAlphaNumericException, AlreadyConnectedException {
        // Given
        connectedUsers.joinChat("123", "Stephen");

        // When
        connectedUsers.leaveChat("123");
        connectedUsers.leaveChat("123");

        // Then
        assertConnectedUsers();
    }

    @Test
    public void test_that_a_user_can_disconnect() throws NotAlphaNumericException, AlreadyConnectedException {
        // Given
        connectedUsers.joinChat("123", "Stephen");
        connectedUsers.joinChat("xyz", "Mike");

        // When
        connectedUsers.leaveChat("xyz");

        // Then
        assertConnectedUsers("Stephen");
    }

    @Test
    public void test_two_users_with_same_username_can_connect() throws NotAlphaNumericException, AlreadyConnectedException {
        // When
        connectedUsers.joinChat("123", "Stephen");
        connectedUsers.joinChat("xyz", "Stephen");

        // Then
        assertConnectedUsers("Stephen", "Stephen");
    }

    private void assertConnectedUsers(String... users) {
        ArrayList<String> expectedUsers = connectedUsers.connectedUserList();
        assertEquals(expectedUsers.size(), users.length);
        assertTrue(expectedUsers.containsAll(Arrays.asList(users)));
    }
}