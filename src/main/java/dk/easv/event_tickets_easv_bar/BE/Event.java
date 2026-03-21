package dk.easv.event_tickets_easv_bar.BE;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {

    private int id;
    private String name;
    private String info;
    private LocalDate date;
    private LocalTime endTime;
    private LocalDate endDate;
    private String location;
    private String locationGuidance;
    private int ticketAmount;
    private int ticketsSold;
    private int coordinatorID;
    private String coordinatorName;

    public Event(int id, String name, String info, LocalDate date, LocalTime endTime,
                 LocalDate endDate, String location, int ticketAmount, int ticketsSold,
                 int coordinatorID, String coordinatorName) {

        this.id = id;
        this.name = name;
        this.info = info;
        this.date = date;
        this.endTime = endTime;
        this.endDate = endDate;
        this.location = location;
        this.ticketAmount = ticketAmount;
        this.ticketsSold = ticketsSold;
        this.coordinatorID = coordinatorID;
        this.coordinatorName = coordinatorName;
    }

    // ---------------- GETTERS ----------------

    public int getId() { return id; }
    public String getName() { return name; }
    public String getInfo() { return info; }
    public LocalDate getDate() { return date; }
    public LocalTime getEndTime() { return endTime; }
    public LocalDate getEndDate() { return endDate; }
    public String getLocation() { return location; }
    public int getTicketAmount() { return ticketAmount; }
    public int getTicketsSold() { return ticketsSold; }
    public int getCoordinatorID() { return coordinatorID; }
    public String getCoordinatorName() { return coordinatorName; }
    public String getLocationGuidance() {return locationGuidance;}

    public void setLocationGuidance(String locationGuidance) {
        this.locationGuidance = locationGuidance;
    }

    // ---------------- STATUS ----------------

    public String getStatus() {
        int left = ticketAmount - ticketsSold;
        if (left <= 0) return "Sold out";
        return left + " left";
    }
}