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

    public Event(int id, String name, String info, LocalDate date, LocalTime endTime, String location) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.date = date;
        this.endTime = endTime;
        this.location = location;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public LocalDate getDate() { return date; }

    public String getInfo() {
        return info;
    }

    public String getEndTime() {
        return endTime.toString();
    }

    public String getLocation() {
        return location;
    }
}