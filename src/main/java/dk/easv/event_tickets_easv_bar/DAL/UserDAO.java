package dk.easv.event_tickets_easv_bar.DAL;

import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.DAL.DB.DBConnector;
import dk.easv.event_tickets_easv_bar.DAL.Interface.IUserDAO;

import java.io.IOException;
import java.sql.*;

public class UserDAO implements IUserDAO {

    private final DBConnector dbConnector;

    public UserDAO() {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 🔐 LOGIN
    @Override
    public User login(String username, String password) {

        String sql = "SELECT * FROM Users WHERE Username=? AND PasswordHash=?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        rs.getInt("RoleInt")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 🔍 GET USER BY ID
    @Override
    public User getUserById(int id) {

        String sql = "SELECT * FROM Users WHERE UserID=?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        rs.getInt("RoleInt")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int addUser(String username, String password, int role) {
        return 0;
    }

    // ➕ CREATE USER (rigtige data, ikke midlertidige værdier)
    public int addUser(String username, String password, String name, String email, String phone, int role) {

        String sql = "INSERT INTO Users (Username, PasswordHash, RoleInt, Name, Email, PhoneNumber) VALUES (?,?,?,?,?,?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, role);
            stmt.setString(4, name);
            stmt.setString(5, email);
            stmt.setString(6, phone);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1); // returner ny brugerID
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}