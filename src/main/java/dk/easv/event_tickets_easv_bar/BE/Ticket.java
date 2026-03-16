package dk.easv.event_tickets_easv_bar.BE;

public class Ticket {

    private int id;
    private int eventId;
    private int customerId;
    private int amount;

    public Ticket(int id, int eventId, int customerId, int amount) {
        this.id = id;
        this.eventId = eventId;
        this.customerId = customerId;
        this.amount = amount;
    }

    public int getId() { return id; }

    public int getEventId() {
        return eventId;
    }

    public int getCustomerId() {
    return customerId;
    }

    public int getAmount() {
        return amount;
    }
}