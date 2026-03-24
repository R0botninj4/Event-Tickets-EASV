package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.BLL.EventManager;
import dk.easv.event_tickets_easv_bar.BLL.UserManager;
import dk.easv.event_tickets_easv_bar.GUI.Interface.ClosableWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

    private ObservableList<Event> events = FXCollections.observableArrayList();
    private Event selectedEvent;

    // ---------------- INIT ----------------
    @FXML
    public void initialize() {

        enableEscClose(closeButton);

        setupColumns();
        loadEvents();
        loadCoordinators();
        loadTimeOptions();
        setupSearch();

        // 🔥 AUTO SELECT → fill form
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                selectedEvent = newSel;
                fillForm(newSel);
            }
        });
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
        events.clear();
        events.addAll(eventManager.getAllEvents());
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

    // ---------------- SEARCH ----------------
    private void setupSearch() {

        FilteredList<Event> filtered = new FilteredList<>(events, p -> true);

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            filtered.setPredicate(event -> {

                if (newVal == null || newVal.isEmpty()) return true;

                String search = newVal.toLowerCase();

                if (event.getName().toLowerCase().contains(search)) return true;
                if (event.getLocation().toLowerCase().contains(search)) return true;
                if (event.getCoordinatorName().toLowerCase().contains(search)) return true;

                return false;
            });
        });

        SortedList<Event> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(eventTable.comparatorProperty());

        eventTable.setItems(sorted);
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

        for (User user : cbCoordinator.getItems()) {
            if (user.getId() == event.getCoordinatorID()) {
                cbCoordinator.setValue(user);
                break;
            }
        }

        if (event.getEndTime() != null) {
            cbHour.setValue(event.getEndTime().getHour());
            cbMinute.setValue(event.getEndTime().getMinute());
        } else {
            cbHour.setValue(null);
            cbMinute.setValue(null);
        }
    }

    // ---------------- SAVE (ADD + UPDATE) ----------------
    @FXML
    private void handleUpdateEvent() {

        try {

            if (txtName.getText().isEmpty()) {
                showAlert("Error", "Name is required");
                return;
            }

            if (dpDate.getValue() == null || dpEndDate.getValue() == null) {
                showAlert("Error", "Dates are required");
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

            if (selectedEvent == null) {
                // ADD
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

            } else {
                // UPDATE
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
            }

            loadEvents();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Something went wrong");
        }
    }

    // ---------------- ADD BUTTON ----------------
    @FXML
    private void handleAddEvent() {
        clearFields();
    }

    // ---------------- DELETE ----------------
    @FXML
    private void handleDeleteEvent() {

        Event selected = eventTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Error", "Select an event to delete");
            return;
        }

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

        eventTable.getSelectionModel().clearSelection();
        selectedEvent = null;
    }

    // ---------------- ALERT ----------------
    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ---------------- CLOSE ----------------
    @FXML
    private void Close(javafx.event.ActionEvent event) {
        closeWindow(event);
    }
}