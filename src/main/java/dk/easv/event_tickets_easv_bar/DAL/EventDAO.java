package dk.easv.event_tickets_easv_bar.DAL;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.DAL.DB.DBConnector;
import dk.easv.event_tickets_easv_bar.DAL.Interface.IEventDAO;
import java.time.LocalTime;
import java.time.LocalDate;

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

        String sql = """
            SELECT 
                e.EventID,
                e.EventName,
                e.EventInfo,
                e.EventDate,
                e.EndTime,
                e.Location,
                e.TicketAmount,
                e.TicketsSold,
                e.CoordinatorID,
                u.Name AS CoordinatorName
            FROM Events e
            LEFT JOIN Users u ON e.CoordinatorID = u.UserID
        """;

        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LocalTime endTime = null;
                Time sqlTime = rs.getTime("EndTime");
                if (sqlTime != null) endTime = sqlTime.toLocalTime();

                events.add(new Event(
                        rs.getInt("EventID"),
                        rs.getString("EventName"),
                        rs.getString("EventInfo"),
                        rs.getDate("EventDate").toLocalDate(),
                        endTime,
                        rs.getString("Location"),
                        rs.getInt("TicketAmount"),
                        rs.getInt("TicketsSold"),
                        rs.getInt("CoordinatorID"),
                        rs.getString("CoordinatorName")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    @Override
    public Event getEventById(int id) {
        String sql = """
            SELECT 
                e.EventID,
                e.EventName,
                e.EventInfo,
                e.EventDate,
                e.EndTime,
                e.Location,
                e.TicketAmount,
                e.TicketsSold,
                e.CoordinatorID,
                u.Name AS CoordinatorName
            FROM Events e
            LEFT JOIN Users u ON e.CoordinatorID = u.UserID
            WHERE e.EventID = ?
        """;

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LocalTime endTime = null;
                Time sqlTime = rs.getTime("EndTime");
                if (sqlTime != null) endTime = sqlTime.toLocalTime();

                return new Event(
                        rs.getInt("EventID"),
                        rs.getString("EventName"),
                        rs.getString("EventInfo"),
                        rs.getDate("EventDate").toLocalDate(),
                        endTime,
                        rs.getString("Location"),
                        rs.getInt("TicketAmount"),
                        rs.getInt("TicketsSold"),
                        rs.getInt("CoordinatorID"),
                        rs.getString("CoordinatorName")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void createEvent(Event event) {
        String sql = """
            INSERT INTO Events 
            (EventName, EventInfo, EventDate, EndTime, Location, TicketAmount, TicketsSold, CoordinatorID) 
            VALUES (?,?,?,?,?,?,?,?)
        """;

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getName());
            stmt.setString(2, event.getInfo());
            stmt.setDate(3, Date.valueOf(event.getDate()));
            stmt.setTime(4, event.getEndTime() != null ? Time.valueOf(event.getEndTime()) : null);
            stmt.setString(5, event.getLocation());
            stmt.setInt(6, event.getTicketAmount());
            stmt.setInt(7, event.getTicketsSold());
            stmt.setInt(8, event.getCoordinatorID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}