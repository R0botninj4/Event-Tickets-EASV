package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.BE.Ticket;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TicketViewController {

    @FXML private Label lblEventName;
    @FXML private Label lblStartDate;
    @FXML private Label lblEndDate;
    @FXML private Label lblLocation;
    @FXML private Label lblTicketId;
    @FXML private Label lblBarcode;

    public void setTicket(Ticket ticket, Event event) {

        // Event navn
        lblEventName.setText(event.getName());

        // 🔹 START DATE (du bruger getDate())
        lblStartDate.setText(
                event.getDate() != null ? event.getDate().toString() : ""
        );

        // 🔹 END DATE
        lblEndDate.setText(
                event.getEndDate() != null ? event.getEndDate().toString() : ""
        );

        // 🔹 LOCATION
        lblLocation.setText(
                event.getLocation() != null ? event.getLocation() : ""
        );

        // 🔹 TICKET ID
        lblTicketId.setText(String.valueOf(ticket.getId()));

        // 🔹 BARCODE
        lblBarcode.setText(
                ticket.getBarcode() != null ? ticket.getBarcode() : ""
        );
    }
}