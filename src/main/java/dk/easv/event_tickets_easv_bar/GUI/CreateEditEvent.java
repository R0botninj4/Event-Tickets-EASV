package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.BLL.EventManager;
import dk.easv.event_tickets_easv_bar.BLL.UserManager;
import dk.easv.event_tickets_easv_bar.GUI.Interface.ClosableWindow;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateEditEvent implements ClosableWindow {

    // ---------------- UI ----------------
    @FXML private Button closeButton;

    @FXML private TableView<Event> eventTable;
    @FXML private TextField txtSearch;
    @FXML private TableColumn<Event, String> colName;
    @FXML private TableColumn<Event, LocalDate> colDate;
    @FXML private TableColumn<Event, String> colLocation;
    @FXML private TableColumn<Event, String> colCoordinator;
    @FXML private TableColumn<Event, String> colStatus;
    @FXML private TableColumn<Event, Integer> colTicketsSold;
    @FXML private TableColumn<Event, String> colInfo;

    @FXML private TextField txtName;
    @FXML private TextField txtInfo;
    @FXML private TextField txtLocation;
    @FXML private TextField txtTicketAmount;
    @FXML private TextField txtLocationGuidance;

    @FXML private DatePicker dpDate;
    @FXML private DatePicker dpEndDate;

    @FXML private ComboBox<User> cbCoordinator;
    @FXML private ComboBox<Integer> cbHour;
    @FXML private ComboBox<Integer> cbMinute;

    // ---------------- DATA ----------------
    private EventManager eventManager = new EventManager();
    private UserManager userManager = new UserManager();

    private Event selectedEvent; // used for editing

    // ---------------- INIT ----------------
    @FXML
    public void initialize() {

        enableEscClose(closeButton);

        setupColumns();
        loadEvents();
        loadCoordinators();
        loadTimeOptions();
        setupSearch();
    }

    // ---------------- TABLE ----------------
    private void setupColumns() {

        colName.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));

        colDate.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getDate()));

        colLocation.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getLocation()));

        colCoordinator.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getCoordinatorName()));

        colStatus.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

        colTicketsSold.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getTicketsSold()));

        colInfo.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getInfo()));
    }

    // ---------------- LOAD ----------------
    private void loadEvents() {
        eventTable.setItems(FXCollections.observableArrayList(
                eventManager.getAllEvents()
        ));
    }

    private void loadCoordinators() {

        cbCoordinator.setItems(FXCollections.observableArrayList(
                userManager.getAllUsers()
        ));

        cbCoordinator.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getName());
            }
        });

        cbCoordinator.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getName());
            }
        });
    }

    private void loadTimeOptions() {

        cbHour.setItems(FXCollections.observableArrayList(
                0,1,2,3,4,5,6,7,8,9,10,11,
                12,13,14,15,16,17,18,19,20,21,22,23
        ));

        cbMinute.setItems(FXCollections.observableArrayList(
                0,15,30,45
        ));
    }

    // ---------------- FILL FORM ----------------
    private void fillForm(Event event) {

        txtName.setText(event.getName());
        txtInfo.setText(event.getInfo());
        txtLocation.setText(event.getLocation());
        txtTicketAmount.setText(String.valueOf(event.getTicketAmount()));
        txtLocationGuidance.setText(event.getLocationGuidance());

        dpDate.setValue(event.getDate());
        dpEndDate.setValue(event.getEndDate());

        // Coordinator
        for (User user : cbCoordinator.getItems()) {
            if (user.getId() == event.getCoordinatorID()) {
                cbCoordinator.setValue(user);
                break;
            }
        }

        // Time
        if (event.getEndTime() != null) {
            cbHour.setValue(event.getEndTime().getHour());
            cbMinute.setValue(event.getEndTime().getMinute());
        } else {
            cbHour.setValue(null);
            cbMinute.setValue(null);
        }
    }

    // ---------------- ALERT ----------------
    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // ---------------- ADD EVENT ----------------
    @FXML
    private void handleAddEvent() {

        try {

            if (txtName.getText().isEmpty()) {
                showAlert("Error", "Name is required");
                return;
            }

            if (dpDate.getValue() == null) {
                showAlert("Error", "Start date is required");
                return;
            }

            if (dpEndDate.getValue() == null) {
                showAlert("Error", "End date is required");
                return;
            }

            if (cbCoordinator.getValue() == null) {
                showAlert("Error", "Select a coordinator");
                return;
            }

            int tickets;
            try {
                tickets = Integer.parseInt(txtTicketAmount.getText());
            } catch (Exception e) {
                showAlert("Error", "Ticket amount must be a number");
                return;
            }

            LocalTime endTime = null;

            if (cbHour.getValue() != null && cbMinute.getValue() != null) {
                endTime = LocalTime.of(cbHour.getValue(), cbMinute.getValue());
            }

            User user = cbCoordinator.getValue();

            Event event = new Event(
                    0,
                    txtName.getText(),
                    txtInfo.getText(),
                    dpDate.getValue(),
                    endTime,
                    dpEndDate.getValue(),
                    txtLocation.getText(),
                    tickets,
                    0,
                    user.getId(),
                    user.getName()
            );

            event.setLocationGuidance(txtLocationGuidance.getText());

            eventManager.createEvent(event);

            loadEvents();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Something went wrong");
        }
    }

    // ---------------- EDIT BUTTON ----------------
    @FXML
    private void handleEditEvent() {

        Event selected = eventTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Error", "Select an event first");
            return;
        }

        selectedEvent = selected;
        fillForm(selected);
    }

    // ---------------- UPDATE EVENT ----------------
    @FXML
    private void handleUpdateEvent() {

        if (selectedEvent == null) {
            showAlert("Error", "Press Edit first");
            return;
        }

        try {

            int tickets = Integer.parseInt(txtTicketAmount.getText());

            LocalTime endTime = null;
            if (cbHour.getValue() != null && cbMinute.getValue() != null) {
                endTime = LocalTime.of(cbHour.getValue(), cbMinute.getValue());
            }

            User user = cbCoordinator.getValue();

            Event updatedEvent = new Event(
                    selectedEvent.getId(),
                    txtName.getText(),
                    txtInfo.getText(),
                    dpDate.getValue(),
                    endTime,
                    dpEndDate.getValue(),
                    txtLocation.getText(),
                    tickets,
                    selectedEvent.getTicketsSold(),
                    user.getId(),
                    user.getName()
            );

            updatedEvent.setLocationGuidance(txtLocationGuidance.getText());

            eventManager.updateEvent(updatedEvent);

            loadEvents();
            clearFields();
            selectedEvent = null;

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Update failed");
        }
    }

    // ---------------- CLEAR ----------------
    private void clearFields() {

        txtName.clear();
        txtInfo.clear();
        txtLocation.clear();
        txtTicketAmount.clear();
        txtLocationGuidance.clear();

        dpDate.setValue(null);
        dpEndDate.setValue(null);

        cbCoordinator.setValue(null);
        cbHour.setValue(null);
        cbMinute.setValue(null);

        selectedEvent = null;
    }

    @FXML
    private void handleDeleteEvent() {

        Event selected = eventTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Error", "Select an event to delete");
            return;
        }

        // Confirm dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Event");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete this event?");

        if (confirm.showAndWait().get() == ButtonType.OK) {

            eventManager.deleteEvent(selected.getId());

            loadEvents();
            clearFields();
        }
    }

    private void setupSearch() {

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {

            String search = newVal.toLowerCase();

            eventTable.setItems(FXCollections.observableArrayList(
                    eventManager.getAllEvents().stream()
                            .filter(e ->
                                    e.getName().toLowerCase().contains(search) ||
                                            e.getLocation().toLowerCase().contains(search) ||
                                            e.getCoordinatorName().toLowerCase().contains(search)
                            )
                            .toList()
            ));
        });
    }

    // ---------------- CLOSE ----------------
    @FXML
    private void Close(javafx.event.ActionEvent event) {
        closeWindow(event);
    }
}