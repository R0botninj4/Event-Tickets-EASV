package dk.easv.event_tickets_easv_bar.BE;

import java.time.LocalDateTime;

public class Ticket {

    private int id;
    private int eventId;
    private int customerId;
    private String ticketType;
    private int amount;
    private LocalDateTime purchaseDate;
    private String email;
    private String status;

    public Ticket(int id, int eventId, int customerId, String ticketType, int amount, LocalDateTime purchaseDate, String email, String status) {
        this.id = id;
        this.eventId = eventId;
        this.customerId = customerId;
        this.ticketType = ticketType;
        this.amount = amount;
        this.purchaseDate = purchaseDate;
        this.email = email;
        this.status = status;
    }

    // Constructor til oprettelse (uden id + purchaseDate)
    public Ticket(int eventId, int customerId, String ticketType, int amount, String email) {
        this.eventId = eventId;
        this.customerId = customerId;
        this.ticketType = ticketType;
        this.amount = amount;
        this.email = email;
    }

    public int getId() { return id; }

    public int getEventId() { return eventId; }

    public int getCustomerId() { return customerId; }

    public String getTicketType() { return ticketType; }

    public int getAmount() { return amount; }

    public LocalDateTime getPurchaseDate() { return purchaseDate; }

    public String getEmail() { return email; }

    public void setTicketType(String ticketType) { this.ticketType = ticketType; }

    public void setAmount(int amount) { this.amount = amount; }

    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}