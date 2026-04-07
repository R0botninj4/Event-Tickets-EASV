package dk.easv.event_tickets_easv_bar.BE;

import javafx.beans.property.*;

public class CustomerOverview {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty eventName = new SimpleStringProperty();
    private final IntegerProperty ticketsBought = new SimpleIntegerProperty();

    public CustomerOverview(String name, String phoneNumber, String email, String eventName, int ticketsBought) {
        this.name.set(name);
        this.phoneNumber.set(phoneNumber);
        this.email.set(email);
        this.eventName.set(eventName);
        this.ticketsBought.set(ticketsBought);
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty phoneNumberProperty() { return phoneNumber; }
    public StringProperty emailProperty() { return email; }
    public StringProperty eventNameProperty() { return eventName; }
    public IntegerProperty ticketsBoughtProperty() { return ticketsBought; }

    public String getName() { return name.get(); }
    public String getPhoneNumber() { return phoneNumber.get(); }
    public String getEmail() { return email.get(); }
    public String getEventName() { return eventName.get(); }
    public int getTicketsBought() { return ticketsBought.get(); }
}