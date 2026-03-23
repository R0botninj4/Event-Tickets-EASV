package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.BLL.EventManager;
import dk.easv.event_tickets_easv_bar.DAL.UserDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EditEventController {

    @FXML private TextField txtEventName;
    @FXML private TextArea txtEventInfo;
    @FXML private DatePicker dpEventDate;
    @FXML private DatePicker dpEndDate;
    @FXML private TextField txtEndTime;
    @FXML private TextField txtLocation;
    @FXML private TextField txtLocationGuidance;
    @FXML private Spinner<Integer> spTicketAmount;
    @FXML private ComboBox<User> cmbCoordinator;
    @FXML private Button closeButton;
    @FXML private Button btnSave;

    private final EventManager eventManager = new EventManager();
    private final UserDAO userDAO = new UserDAO();
    private Event currentEvent;

    // Called by ManageEvent after loading the FXML
    public void setEvent(Event event) {
        this.currentEvent = event;
        prefillFields(event);
    }

    @FXML
    public void initialize() {
        // Load coordinators (role == 2) into the ComboBox
        List<User> allUsers = userDAO.getAllUsers();
        List<User> coordinators = allUsers.stream()
                .filter(u -> u.getRole() == 2)
                .toList();

        cmbCoordinator.setItems(FXCollections.observableArrayList(coordinators));

        // Show user's name in the dropdown
        cmbCoordinator.setConverter(new StringConverter<>() {
            @Override
            public String toString(User user) {
                return user != null ? user.getName() : "";
            }
            @Override
            public User fromString(String s) { return null; }
        });

        // Spinner setup
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99999, 0);
        spTicketAmount.setValueFactory(valueFactory);
    }

    private void prefillFields(Event event) {
        txtEventName.setText(event.getName());
        txtEventInfo.setText(event.getInfo());
        dpEventDate.setValue(event.getDate());
        dpEndDate.setValue(event.getEndDate());
        txtEndTime.setText(event.getEndTime() != null ? event.getEndTime().toString() : "");
        txtLocation.setText(event.getLocation());
        txtLocationGuidance.setText(event.getLocationGuidance() != null ? event.getLocationGuidance() : "");
        spTicketAmount.getValueFactory().setValue(event.getTicketAmount());

        // Pre-select the current coordinator in the dropdown
        cmbCoordinator.getItems().stream()
                .filter(u -> u.getId() == event.getCoordinatorID())
                .findFirst()
                .ifPresent(u -> cmbCoordinator.setValue(u));
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) {
        // Validate end time format
        LocalTime endTime = null;
        String endTimeText = txtEndTime.getText().trim();
        if (!endTimeText.isEmpty()) {
            try {
                endTime = LocalTime.parse(endTimeText);
            } catch (DateTimeParseException e) {
                showWarning("Invalid time", "Please enter end time in HH:mm format, e.g. 21:00");
                return;
            }
        }

        // Validate required fields
        if (txtEventName.getText().trim().isEmpty()) {
            showWarning("Missing field", "Event name cannot be empty.");
            return;
        }
        if (dpEventDate.getValue() == null) {
            showWarning("Missing field", "Event date cannot be empty.");
            return;
        }

        // Apply changes to the event object
        currentEvent.setName(txtEventName.getText().trim());
        currentEvent.setInfo(txtEventInfo.getText().trim());
        currentEvent.setDate(dpEventDate.getValue());
        currentEvent.setEndDate(dpEndDate.getValue());
        currentEvent.setEndTime(endTime);
        currentEvent.setLocation(txtLocation.getText().trim());
        currentEvent.setLocationGuidance(txtLocationGuidance.getText().trim());
        currentEvent.setTicketAmount(spTicketAmount.getValue());

        if (cmbCoordinator.getValue() != null) {
            currentEvent.setCoordinatorID(cmbCoordinator.getValue().getId());
            currentEvent.setCoordinatorName(cmbCoordinator.getValue().getName());
        }

        // Save to DB
        eventManager.updateEvent(currentEvent);

        // Close the window
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}