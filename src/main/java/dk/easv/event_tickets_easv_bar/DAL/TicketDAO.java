package dk.easv.event_tickets_easv_bar.DAL;

import dk.easv.event_tickets_easv_bar.BE.Ticket;
import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.DAL.DB.DBConnector;
import dk.easv.event_tickets_easv_bar.DAL.Interface.ITicketDAO;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO implements ITicketDAO {

    private DBConnector dbConnector;

    public TicketDAO() {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 🔥 BUY TICKET + update TicketsSold
    @Override
    public void buyTicket(Ticket ticket) {

        String insertSql = """
        INSERT INTO Tickets 
        (EventID, CustomerID, TicketType, TicketAmount, Email, Status, Barcode)
        VALUES (?,?,?,?,?, 'Active', ?)
    """;

        String updateEventSql = """
        UPDATE Events
        SET TicketsSold = TicketsSold + 1
        WHERE EventID = ?
    """;

        try (Connection conn = dbConnector.getConnection()) {

            conn.setAutoCommit(false);

            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserByEmail(ticket.getEmail());
            int customerId = (user != null) ? user.getId() : 0;

            // 🔹 INSERT ticket
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setInt(1, ticket.getEventId());
                stmt.setInt(2, customerId);
                stmt.setString(3, ticket.getTicketType());
                stmt.setInt(4, ticket.getAmount()); // = 1 nu
                stmt.setString(5, ticket.getEmail());
                stmt.setString(6, ticket.getBarcode());

                stmt.executeUpdate();
            }

            // 🔥 UPDATE TicketsSold
            try (PreparedStatement stmt = conn.prepareStatement(updateEventSql)) {
                stmt.setInt(1, ticket.getEventId());
                stmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  GET ALL
    @Override
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();

        String sql = "SELECT * FROM Tickets WHERE Status = 'Active'";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tickets.add(createTicketFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    // 🔍 GET BY EVENT
    @Override
    public List<Ticket> getTicketsByEvent(int eventId) {
        List<Ticket> tickets = new ArrayList<>();

        String sql = "SELECT * FROM Tickets WHERE EventID = ? AND Status = 'Active'";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tickets.add(createTicketFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    // 🔍 GET BY USER
    @Override
    public List<Ticket> getTicketsByUser(int userId) {
        List<Ticket> tickets = new ArrayList<>();

        String sql = "SELECT * FROM Tickets WHERE CustomerID = ? AND Status = 'Active'";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tickets.add(createTicketFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    // 🔧 Helper
    private Ticket createTicketFromResultSet(ResultSet rs) throws SQLException {
        return new Ticket(
                rs.getInt("TicketID"),
                rs.getInt("EventID"),
                rs.getInt("CustomerID"),
                rs.getString("TicketType"),
                rs.getInt("TicketAmount"),
                rs.getTimestamp("PurchaseDate") != null ? rs.getTimestamp("PurchaseDate").toLocalDateTime() : null,
                rs.getString("Email"),
                rs.getString("status"),
                rs.getString("Barcode")

        );
    }

    public void cancelTicket(int ticketId) {

        String getTicketSql = "SELECT EventID, TicketAmount FROM Tickets WHERE TicketID = ?";
        String cancelSql = "UPDATE Tickets SET Status = 'Cancelled' WHERE TicketID = ?";
        String updateEventSql = "UPDATE Events SET TicketsSold = TicketsSold - ? WHERE EventID = ?";

        try (Connection conn = dbConnector.getConnection()) {

            conn.setAutoCommit(false);

            int eventId = 0;
            int amount = 0;

            // 🔍 Get ticket info
            try (PreparedStatement stmt = conn.prepareStatement(getTicketSql)) {
                stmt.setInt(1, ticketId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    eventId = rs.getInt("EventID");
                    amount = rs.getInt("TicketAmount");
                }
            }

            // ❌ Cancel ticket
            try (PreparedStatement stmt = conn.prepareStatement(cancelSql)) {
                stmt.setInt(1, ticketId);
                stmt.executeUpdate();
            }

            // 🔄 Update TicketsSold
            try (PreparedStatement stmt = conn.prepareStatement(updateEventSql)) {
                stmt.setInt(1, amount);
                stmt.setInt(2, eventId);
                stmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}