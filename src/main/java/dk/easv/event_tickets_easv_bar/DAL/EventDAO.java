package dk.easv.event_tickets_easv_bar.DAL;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.DAL.DB.DBConnector;
import dk.easv.event_tickets_easv_bar.DAL.Interface.IEventDAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO implements IEventDAO {

    private DBConnector dbConnector;

    public EventDAO() {
        try {
            dbConnector = new DBConnector();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Events";

        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("EventID"),
                        rs.getString("EventName"),
                        rs.getString("EventInfo"),
                        rs.getDate("EventDate").toLocalDate(),
                        rs.getTime("EndTime").toLocalTime(),
                        rs.getString("Location"),
                        rs.getInt("TicketAmount"),
                        rs.getInt("TicketsSold"),
                        rs.getInt("CoordinatorID")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    @Override
    public Event getEventById(int id) {
        String sql = "SELECT * FROM Events WHERE EventID=?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Event(
                        rs.getInt("EventID"),
                        rs.getString("EventName"),
                        rs.getString("EventInfo"),
                        rs.getDate("EventDate").toLocalDate(),
                        rs.getTime("EndTime").toLocalTime(),
                        rs.getString("Location"),
                        rs.getInt("TicketAmount"),
                        rs.getInt("TicketSold"),
                        rs.getInt("CoordinatorID")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void createEvent(Event event) {
        String sql = "INSERT INTO Events (EventName, EventInfo, EventDate, EndTime, Location, TicketAmount, TicketSold, CoordinatorID) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getName());
            stmt.setString(2, event.getInfo());
            stmt.setDate(3, Date.valueOf(event.getDate()));
            stmt.setTime(4, Time.valueOf(event.getEndTime()));
            stmt.setString(5, event.getLocation());
            stmt.setInt(6, event.getTicketAmount());
            stmt.setInt(7, event.getTicketSold());
            stmt.setInt(8, event.getCoordinatorID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}