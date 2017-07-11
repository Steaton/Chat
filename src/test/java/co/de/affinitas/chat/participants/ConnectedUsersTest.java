package co.de.affinitas.chat.participants;

import co.de.affinitas.chat.participants.exception.NotAlphaNumericException;
import co.de.affinitas.chat.participants.exception.UserAlreadyConnectedException;
import co.de.affinitas.chat.participants.model.ConnectedUsers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Set;

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
    public void test_that_username_cannot_be_null() throws NotAlphaNumericException, UserAlreadyConnectedException {
        // Expect
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Argument expected not to be empty!");

        // When
        connectedUsers.joinChat(null);
    }

    @Test
    public void test_that_username_cannot_be_empty() throws NotAlphaNumericException, UserAlreadyConnectedException {
        // Expect
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Argument expected not to be empty!");

        // When
        connectedUsers.joinChat("");
    }

    @Test
    public void test_that_user_not_already_connected() throws NotAlphaNumericException, UserAlreadyConnectedException {
        // Expect
        expectedException.expect(UserAlreadyConnectedException.class);
        expectedException.expectMessage("User already connected: Stephen");

        // When
        connectedUsers.joinChat("Stephen");
        connectedUsers.joinChat("Stephen");
    }

    @Test
    public void test_that_username_must_be_alphanumeric() throws NotAlphaNumericException, UserAlreadyConnectedException {
        // Expect
        expectedException.expect(NotAlphaNumericException.class);
        expectedException.expectMessage("Username must be alphanumeric but is username: #!@");

        // When
        connectedUsers.joinChat("#!@");
    }

    @Test
    public void test_that_user_can_connect_to_chat() throws NotAlphaNumericException, UserAlreadyConnectedException {
        // When
        connectedUsers.joinChat("Stephen");

        // Then
        assertConnectedUsers("Stephen");
    }

    @Test
    public void test_that_multiple_users_can_connect_at_the_same_time() throws NotAlphaNumericException, UserAlreadyConnectedException {
        // When
        connectedUsers.joinChat("Stephen");
        connectedUsers.joinChat("Mike");

        // Then
        assertConnectedUsers("Stephen", "Mike");
    }

    @Test
    public void test_that_nothing_happens_if_user_disconnects_when_not_even_connected() throws NotAlphaNumericException, UserAlreadyConnectedException {
        // Given
        connectedUsers.joinChat("Stephen");

        // When
        connectedUsers.leaveChat("Mike");

        // Then
        assertConnectedUsers("Stephen");
    }

    @Test
    public void test_that_nothing_happens_if_user_disconnects_multiple_times() throws NotAlphaNumericException, UserAlreadyConnectedException {
        // Given
        connectedUsers.joinChat("Mike");

        // When
        connectedUsers.leaveChat("Mike");
        connectedUsers.leaveChat("Mike");

        // Then
        assertConnectedUsers();
    }

    @Test
    public void test_that_a_user_can_disconnect() throws NotAlphaNumericException, UserAlreadyConnectedException {
        // Given
        connectedUsers.joinChat("Stephen");
        connectedUsers.joinChat("Mike");

        // When
        connectedUsers.leaveChat("Mike");

        // Then
        assertConnectedUsers("Stephen");
    }

    private void assertConnectedUsers(String... users) {
        Set<String> expectedUsers = connectedUsers.connectedUserList();
        assertEquals(expectedUsers.size(), users.length);
        assertTrue(expectedUsers.containsAll(Arrays.asList(users)));
    }
}