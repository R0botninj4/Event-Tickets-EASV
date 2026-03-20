package dk.easv.event_tickets_easv_bar.DAL.Interface;

import dk.easv.event_tickets_easv_bar.BE.User;
import java.util.List;

public interface IUserDAO {

    User login(String username, String password);

    User getUserById(int id);

    List<User> getAllUsers();   // ✅ ADD THIS

    int addUser(String username, String password, int role);
}