package dk.easv.event_tickets_easv_bar.BLL;

import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.BLL.Interface.IUserManager;
import dk.easv.event_tickets_easv_bar.DAL.Interface.IUserDAO;
import dk.easv.event_tickets_easv_bar.DAL.UserDAO;

public class UserManager implements IUserManager {

    private final IUserDAO userDAO;

    public UserManager() {
        userDAO = new UserDAO();
    }

    @Override
    public User login(String username, String password) {

        if(username == null || username.isEmpty()) return null;
        if(password == null || password.isEmpty()) return null;

        return userDAO.login(username, password);
    }

    @Override
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    @Override
    public int addUser(String username, String password, int role) {
        return userDAO.addUser(username,password,role);
    }
}