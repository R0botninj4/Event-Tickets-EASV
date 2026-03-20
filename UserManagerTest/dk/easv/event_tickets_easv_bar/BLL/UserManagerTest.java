package dk.easv.event_tickets_easv_bar.BLL;

import dk.easv.event_tickets_easv_bar.BE.User;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {

    @Test
    public void login_ReturnNull_whenUsernameIsNull() {
        // Arrange
        UserManager userManager = new UserManager();

        // Act
        User result = userManager.login(null, "Password");

        // Assert
        assertNull(result);
    }

    @Test
    public void login_ReturnNull_whenPasswordIsNull() {
        // Arrange
        UserManager userManager = new UserManager();

        // Act
        User result = userManager.login("Username", null);

        // Assert
        assertNull(result);
    }

    @Test
    public void login_ReturnNull_whenPasswordBelowMinLength() {
        // Arrange
        UserManager userManager = new UserManager();

        // Act
        User result = userManager.login("U", "A");

        // Assert
        assertNotNull(result);
    }
}