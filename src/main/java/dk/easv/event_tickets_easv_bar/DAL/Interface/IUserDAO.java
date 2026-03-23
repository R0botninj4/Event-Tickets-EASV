package dk.easv.event_tickets_easv_bar.DAL.Interface;

import dk.easv.event_tickets_easv_bar.BE.User;
import java.util.List;

public interface IUserDAO {

    User login(String username, String password);

    User getUserById(int id);

    List<User> getAllUsers();

    boolean deleteUser(int userId);
    boolean updateUser(int userId, String username, String name, String email, String phoneNumber, int role);

    // Updated to include full name
    int addUser(String username, String password, String name, String email, String phoneNumber, int role);
}