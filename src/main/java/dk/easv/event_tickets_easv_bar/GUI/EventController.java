package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.GUI.Interface.ClosableWindow;
import dk.easv.event_tickets_easv_bar.GUI.Login.Session;
import dk.easv.event_tickets_easv_bar.BE.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EventController implements ClosableWindow {

    @FXML private Label lblEventName;
    @FXML private Label lblCoordinator;
    @FXML private Label lblEventInfo;
    @FXML private Label lblEventDate;
    @FXML private Label lblEndDate;
    @FXML private Label lblEndTime;
    @FXML private Label lblTicketAmount;
    @FXML private Label lblTicketsSold;
    @FXML private Label lblTicketsLeft;
    @FXML private Label lblLocation;
    @FXML private Label lblLocationGuidance;
    @FXML private Label lblUserRole;
    @FXML private Button closeButton;

    @FXML
    public void initialize() {
        enableEscClose(closeButton);

        // Set role label from session
        User loggedInUser = Session.getUser();
        if (loggedInUser != null) {
            lblUserRole.setText(loggedInUser.getRole() == 1 ? "Admin" : "Coordinator");
        }
    }

    // Called from HelloController after FXML is loaded
    public void setEvent(dk.easv.event_tickets_easv_bar.BE.Event event) {
        lblEventName.setText(orDash(event.getName()));
        lblCoordinator.setText(orDash(event.getCoordinatorName()));
        lblEventInfo.setText(orDash(event.getInfo()));
        lblEventDate.setText(event.getDate() != null ? event.getDate().toString() : "-");
        lblEndDate.setText(event.getEndDate() != null ? event.getEndDate().toString() : "-");
        lblEndTime.setText(event.getEndTime() != null ? event.getEndTime().toString() : "-");
        lblTicketAmount.setText(String.valueOf(event.getTicketAmount()));
        lblTicketsSold.setText(String.valueOf(event.getTicketsSold()));
        lblTicketsLeft.setText(String.valueOf(event.getTicketAmount() - event.getTicketsSold()));
        lblLocation.setText(orDash(event.getLocation()));
        lblLocationGuidance.setText(orDash(event.getLocationGuidance()));
    }

    private String orDash(String value) {
        return (value != null && !value.isBlank()) ? value : "-";
    }

    @FXML
    private void Close(ActionEvent event) {
        closeWindow(event);
    }
}