package dk.easv.event_tickets_easv_bar.BE;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {

    private int id;
    private String name;
    private String info;
    private LocalDate date;
    private LocalTime endTime;
    private String location;
    private int ticketAmount;
    private int ticketSold;
    private int coordinatorID;

    public Event(int id, String name, String info, LocalDate date, LocalTime endTime,
                 String location, int ticketAmount, int ticketSold, int coordinatorID) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.date = date;
        this.endTime = endTime;
        this.location = location;
        this.ticketAmount = ticketAmount;
        this.ticketSold = ticketSold;
        this.coordinatorID = coordinatorID;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getName() { return name; }
    public String getInfo() { return info; }
    public LocalDate getDate() { return date; }
    public String getEndTime() { return endTime.toString(); }
    public String getLocation() { return location; }
    public int getTicketAmount() { return ticketAmount; }
    public int getTicketSold() { return ticketSold; }
    public int getCoordinatorID() { return coordinatorID; }

    // --- Computed properties for TableView ---
    public int getSold() {
        return ticketSold;
    }

    public String getStatus() {
        int left = ticketAmount - ticketSold;
        if (left <= 0) return "Sold out";
        return left + " left";
    }
}