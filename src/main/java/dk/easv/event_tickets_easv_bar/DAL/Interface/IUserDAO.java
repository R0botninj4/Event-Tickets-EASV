package dk.easv.event_tickets_easv_bar.DAL.Interface;

import dk.easv.event_tickets_easv_bar.BE.User;

public interface IUserDAO {

    User login(String username, String password);

    User getUserById(int id);

    int addUser(String username, String password, int role);
}