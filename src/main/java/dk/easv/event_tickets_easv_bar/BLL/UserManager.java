package dk.easv.event_tickets_easv_bar.BLL;

import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.DAL.UserDAO;

import java.sql.SQLException;

public class UserManager {

    private UserDAO userDAO;

    public UserManager() {
        userDAO = new UserDAO();
    }

    public User login(String username, String passwordHash) throws SQLException {
        if (username.isEmpty() || passwordHash.isEmpty()) {
            return null;
        }
        return userDAO.login(username, passwordHash);
    }
}