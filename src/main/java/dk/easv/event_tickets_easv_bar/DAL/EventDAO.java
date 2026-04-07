package dk.easv.event_tickets_easv_bar.DAL;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.DAL.DB.DBConnector;
import dk.easv.event_tickets_easv_bar.DAL.Interface.IEventDAO;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
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

    // ---------------- GET ALL ----------------
    @Override
    public List<Event> getAllEvents() {

        List<Event> events = new ArrayList<>();

        String sql = """
            SELECT
                e.EventID,
                e.EventName,
                e.EventInfo,
                e.EventDate,
                e.EndDate,
                e.EndTime,
                e.Location,
                e.LocationGuidance,
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

                // EndTime
                LocalTime endTime = null;
                Time sqlTime = rs.getTime("EndTime");
                if (sqlTime != null) endTime = sqlTime.toLocalTime();

                // EndDate
                LocalDate endDate = null;
                Date sqlEndDate = rs.getDate("EndDate");
                if (sqlEndDate != null) endDate = sqlEndDate.toLocalDate();

                Event event = new Event(
                        rs.getInt("EventID"),
                        rs.getString("EventName"),
                        rs.getString("EventInfo"),
                        rs.getDate("EventDate").toLocalDate(),
                        endTime,
                        endDate,
                        rs.getString("Location"),
                        rs.getInt("TicketAmount"),
                        rs.getInt("TicketsSold"),
                        rs.getInt("CoordinatorID"),
                        rs.getString("CoordinatorName")
                );

                // ✅ NEW FIELD
                event.setLocationGuidance(rs.getString("LocationGuidance"));

                events.add(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    // ---------------- GET BY ID ----------------
    @Override
    public Event getEventById(int id) {

        String sql = """
            SELECT
                e.EventID,
                e.EventName,
                e.EventInfo,
                e.EventDate,
                e.EndDate,
                e.EndTime,
                e.Location,
                e.LocationGuidance,
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

                LocalDate endDate = null;
                Date sqlEndDate = rs.getDate("EndDate");
                if (sqlEndDate != null) endDate = sqlEndDate.toLocalDate();

                Event event = new Event(
                        rs.getInt("EventID"),
                        rs.getString("EventName"),
                        rs.getString("EventInfo"),
                        rs.getDate("EventDate").toLocalDate(),
                        endTime,
                        endDate,
                        rs.getString("Location"),
                        rs.getInt("TicketAmount"),
                        rs.getInt("TicketsSold"),
                        rs.getInt("CoordinatorID"),
                        rs.getString("CoordinatorName")
                );

                // ✅ NEW FIELD
                event.setLocationGuidance(rs.getString("LocationGuidance"));

                return event;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ---------------- CREATE ----------------
    @Override
    public void createEvent(Event event) {

        String sql = """
            INSERT INTO Events 
            (EventName, EventInfo, EventDate, EndDate, EndTime, Location, LocationGuidance, TicketAmount, TicketsSold, CoordinatorID) 
            VALUES (?,?,?,?,?,?,?,?,?,?)
        """;

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getName());
            stmt.setString(2, event.getInfo());
            stmt.setDate(3, Date.valueOf(event.getDate()));

            if (event.getEndDate() != null)
                stmt.setDate(4, Date.valueOf(event.getEndDate()));
            else
                stmt.setNull(4, Types.DATE);

            if (event.getEndTime() != null)
                stmt.setTime(5, Time.valueOf(event.getEndTime()));
            else
                stmt.setNull(5, Types.TIME);

            stmt.setString(6, event.getLocation());
            stmt.setString(7, event.getLocationGuidance());
            stmt.setInt(8, event.getTicketAmount());
            stmt.setInt(9, event.getTicketsSold());
            stmt.setInt(10, event.getCoordinatorID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- UPDATE ----------------
    @Override
    public void updateEvent(Event event) {

        String sql = """
            UPDATE Events SET
                EventName = ?,
                EventInfo = ?,
                EventDate = ?,
                EndDate = ?,
                EndTime = ?,
                Location = ?,
                LocationGuidance = ?,
                TicketAmount = ?,
                TicketsSold = ?,
                CoordinatorID = ?
            WHERE EventID = ?
        """;

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getName());
            stmt.setString(2, event.getInfo());
            stmt.setDate(3, Date.valueOf(event.getDate()));

            if (event.getEndDate() != null)
                stmt.setDate(4, Date.valueOf(event.getEndDate()));
            else
                stmt.setNull(4, Types.DATE);

            if (event.getEndTime() != null)
                stmt.setTime(5, Time.valueOf(event.getEndTime()));
            else
                stmt.setNull(5, Types.TIME);

            stmt.setString(6, event.getLocation());
            stmt.setString(7, event.getLocationGuidance());
            stmt.setInt(8, event.getTicketAmount());
            stmt.setInt(9, event.getTicketsSold());
            stmt.setInt(10, event.getCoordinatorID());

            stmt.setInt(11, event.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- DELETE ----------------
    @Override
    public void deleteEvent(int id) {

        String sql = "UPDATE Events SET DeletedAt = GETDATE() WHERE EventID = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}