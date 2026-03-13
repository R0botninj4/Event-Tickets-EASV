package dk.easv.event_tickets_easv_bar.DAL;

import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.DAL.DB.DBConnector;

import java.io.IOException;
import java.sql.*;

public class UserDAO {

    private final DBConnector dbConnector;

    public UserDAO() {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new RuntimeException("Could not connect to database", e);
        }
    }

    // ===================== LOGIN =====================
    public User login(String username, String password) {
        String sql = "SELECT UserID, Username, RoleInt FROM Users WHERE Username = ? AND PasswordHash = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("UserID"),
                            rs.getString("Username"),
                            rs.getInt("RoleInt")  // <-- bruger nu RoleInt
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // login fejlede
    }

    // ===================== GET USER BY ID =====================
    public User getUserById(int id) {
        String sql = "SELECT UserID, Username, RoleInt FROM Users WHERE UserID = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("UserID"),
                            rs.getString("Username"),
                            rs.getInt("RoleInt")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ===================== ADD NEW USER =====================
    public int addUser(String username, String password, int role) {
        String sql = "INSERT INTO Users (Username, Password, RoleInt) VALUES (?, ?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setInt(3, role);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}