package dk.easv.event_tickets_easv_bar.DAL;

import dk.easv.event_tickets_easv_bar.BE.Ticket;
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

        String insertSql = "INSERT INTO Tickets (EventID, CustomerID, TicketType, TicketAmount, Email) VALUES (?,?,?,?,?)";
        String updateEventSql = "UPDATE Events SET TicketsSold = TicketsSold + ? WHERE EventID = ?";

        try (Connection conn = dbConnector.getConnection()) {

            conn.setAutoCommit(false); // transaction

            // INSERT ticket
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setInt(1, ticket.getEventId());
                stmt.setInt(2, ticket.getCustomerId());
                stmt.setString(3, ticket.getTicketType());
                stmt.setInt(4, ticket.getAmount());
                stmt.setString(5, ticket.getEmail());
                stmt.executeUpdate();
            }

            // UPDATE TicketsSold
            try (PreparedStatement stmt = conn.prepareStatement(updateEventSql)) {
                stmt.setInt(1, ticket.getAmount());
                stmt.setInt(2, ticket.getEventId());
                stmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔍 GET ALL
    @Override
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();

        String sql = "SELECT * FROM Tickets";

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

        String sql = "SELECT * FROM Tickets WHERE EventID = ?";

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

        String sql = "SELECT * FROM Tickets WHERE CustomerID = ?";

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
                rs.getString("Email")
        );
    }
}