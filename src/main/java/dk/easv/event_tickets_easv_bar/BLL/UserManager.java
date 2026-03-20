package dk.easv.event_tickets_easv_bar.BLL;

import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.DAL.Interface.IUserDAO;
import dk.easv.event_tickets_easv_bar.DAL.UserDAO;

import java.util.List;

public class UserManager {

    private final IUserDAO userDAO;

    public UserManager() {
        userDAO = new UserDAO();
    }

    public User login(String username, String password) {

        if (username == null || username.isEmpty()) return null;
        if (password == null || password.length() < 4) return null;

        return userDAO.login(username, password);
    }

    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    // ✅ ADD THIS
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public int addUser(String username, String password, int role) {

        if (username == null || username.isEmpty()) return -1;
        if (password == null || password.length() < 4) return -1;

        return userDAO.addUser(username, password, role);
    }
}