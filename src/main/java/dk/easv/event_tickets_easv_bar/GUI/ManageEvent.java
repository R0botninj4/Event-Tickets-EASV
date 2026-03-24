package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.BLL.EventManager;
import dk.easv.event_tickets_easv_bar.DAL.UserDAO;
import dk.easv.event_tickets_easv_bar.GUI.Interface.ClosableWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;

public class ManageEvent implements ClosableWindow {

    // Close
    @FXML private Button closeButton;

    // Assign Coordinator
    @FXML private TextField txtSearchEvent;
    @FXML private Label lblSelectedEventAssign;
    @FXML private ComboBox<User> cmbCoordinator;  // ← replaced txtCoordinatorName
    @FXML private Button btnAssign;

    // Table
    @FXML private TableView<Event> tblEvents;
    @FXML private TableColumn<Event, String> colName;
    @FXML private TableColumn<Event, String> colDate;
    @FXML private TableColumn<Event, String> colLocation;
    @FXML private TableColumn<Event, String> colCoordinator;
    @FXML private TableColumn<Event, String> colStatus;
    @FXML private TableColumn<Event, Integer> colSold;
    @FXML private TableColumn<Event, String> colInfo;

    // Remove & Edit
    @FXML private Label lblSelectedEventRemove;
    @FXML private Button btnRemove;
    @FXML private Label lblSelectedEventEdit;
    @FXML private Button btnEdit;

    private final EventManager eventManager = new EventManager();
    private final UserDAO userDAO = new UserDAO();
    private ObservableList<Event> events;
    private FilteredList<Event> filteredEvents;

    @FXML
    public void initialize() {
        enableEscClose(closeButton);
        setupTable();
        loadData();
        setupSearch();
        setupSelectionListener();
        setupCoordinatorDropdown();
    }

    private void setupCoordinatorDropdown() {
        List<User> coordinators = userDAO.getAllUsers();

        cmbCoordinator.setItems(FXCollections.observableArrayList(coordinators));

        cmbCoordinator.setConverter(new StringConverter<>() {
            @Override
            public String toString(User u) { return u != null ? u.getName() : ""; }
            @Override
            public User fromString(String s) { return null; }
        });
    }

    private void setupTable() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colCoordinator.setCellValueFactory(new PropertyValueFactory<>("coordinatorName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colSold.setCellValueFactory(new PropertyValueFactory<>("ticketsSold"));
        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
    }

    private void loadData() {
        events = FXCollections.observableArrayList(eventManager.getAllEvents());
        filteredEvents = new FilteredList<>(events, p -> true);

        SortedList<Event> sortedData = new SortedList<>(filteredEvents);
        sortedData.comparatorProperty().bind(tblEvents.comparatorProperty());
        tblEvents.setItems(sortedData);
    }

    private void setupSearch() {
        txtSearchEvent.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredEvents.setPredicate(event -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String filter = newVal.toLowerCase();
                return event.getName().toLowerCase().contains(filter)
                        || event.getLocation().toLowerCase().contains(filter)
                        || event.getInfo().toLowerCase().contains(filter);
            });
        });
    }

    private void setupSelectionListener() {
        tblEvents.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                lblSelectedEventAssign.setText(selected.getName());

                // Auto-select the current coordinator in the dropdown
                cmbCoordinator.getItems().stream()
                        .filter(u -> u.getId() == selected.getCoordinatorID())
                        .findFirst()
                        .ifPresent(u -> cmbCoordinator.setValue(u));
            } else {
                lblSelectedEventAssign.setText("Selected Event");
                cmbCoordinator.setValue(null);
            }
        });
    }

    @FXML
    private void handleAssign(ActionEvent actionEvent) {
        Event selected = tblEvents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No event selected", "Please select an event first.");
            return;
        }

        User selectedCoordinator = cmbCoordinator.getValue();
        if (selectedCoordinator == null) {
            showWarning("No coordinator selected", "Please select a coordinator from the dropdown.");
            return;
        }

        selected.setCoordinatorID(selectedCoordinator.getId());
        selected.setCoordinatorName(selectedCoordinator.getName());
        eventManager.updateEvent(selected);

        tblEvents.refresh();
        showInfo("Success", "\"" + selectedCoordinator.getName() + "\" assigned to \"" + selected.getName() + "\"");
    }

    @FXML
    private void handleRemove(ActionEvent actionEvent) {
        Event selected = tblEvents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No event selected", "Please select an event first.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Event");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to remove \"" + selected.getName() + "\"?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                eventManager.deleteEvent(selected.getId());
                events.remove(selected);
            }
        });
    }

    @FXML
    private void handleEdit(ActionEvent actionEvent) {
        Event selected = tblEvents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No event selected", "Please select an event first.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/Views/EditEvent-view.fxml"));
            Scene scene = new Scene(loader.load());

            EditEventController controller = loader.getController();
            controller.setEvent(selected);

            Stage stage = new Stage();
            stage.setTitle("Edit Event");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh table after edit window closes
            tblEvents.refresh();

        } catch (IOException e) {
            e.printStackTrace();
            showWarning("Error", "Could not open the edit window.");
        }
    }

    @FXML
    private void Close(ActionEvent event) {
        closeWindow(event);
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}