package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.BE.Ticket;
import dk.easv.event_tickets_easv_bar.BLL.TicketManager;
import dk.easv.event_tickets_easv_bar.DAL.EventDAO;
import dk.easv.event_tickets_easv_bar.GUI.Interface.ClosableWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreateTicket implements ClosableWindow {

    private final EventDAO eventDAO = new EventDAO();
    private final TicketManager ticketManager = new TicketManager();

    // TABLE
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> colName;
    @FXML private TableColumn<Event, String> colDate;
    @FXML private TableColumn<Event, String> colLocation;
    @FXML private TableColumn<Event, String> colCoordinator;
    @FXML private TableColumn<Event, String> colTicketsLeft;
    @FXML private TableColumn<Event, String> colTicketsSold;

    // SEARCH
    @FXML private TextField txtSearch;

    // EVENT DISPLAY
    @FXML private TextField txtEventName;
    @FXML private TextField txtDate;
    @FXML private TextField txtLocation;

    // TICKET INPUT
    @FXML private TextField txtAmount;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> comboTicketType;

    @FXML private Button closeButton;

    private ObservableList<Event> allEvents = FXCollections.observableArrayList();
    private int selectedEventId = -1;

    @FXML
    public void initialize() {

        enableEscClose(closeButton);

        setupTable();
        loadEvents();
        setupSelection();
        setupSearch();

        comboTicketType.getItems().addAll("Standard", "VIP", "Student");
        comboTicketType.getSelectionModel().selectFirst();

        txtEventName.setEditable(false);
        txtDate.setEditable(false);
        txtLocation.setEditable(false);
    }

    // ---------------- TABLE ----------------
    private void setupTable() {
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getName()));
        colDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDate().toString()));
        colLocation.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getLocation()));
        colCoordinator.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCoordinatorName()));

        colTicketsSold.setCellValueFactory(d ->
                new SimpleStringProperty(String.valueOf(d.getValue().getTicketsSold()))
        );

        colTicketsLeft.setCellValueFactory(d -> {
            int left = d.getValue().getTicketAmount() - d.getValue().getTicketsSold();
            return new SimpleStringProperty(String.valueOf(left));
        });
    }

    private void loadEvents() {
        List<Event> events = eventDAO.getAllEvents();
        allEvents.setAll(events);
        eventTable.setItems(allEvents);
    }

    private void setupSelection() {
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedEventId = newVal.getId();
                txtEventName.setText(newVal.getName());
                txtDate.setText(newVal.getDate().toString());
                txtLocation.setText(newVal.getLocation());
            }
        });
    }

    private void setupSearch() {
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            String search = newVal.toLowerCase();

            List<Event> filtered = allEvents.stream()
                    .filter(e ->
                            e.getName().toLowerCase().contains(search) ||
                                    e.getLocation().toLowerCase().contains(search) ||
                                    e.getCoordinatorName().toLowerCase().contains(search)
                    )
                    .collect(Collectors.toList());

            eventTable.setItems(FXCollections.observableArrayList(filtered));
        });
    }

    // ---------------- BUY TICKET ----------------
    @FXML
    private void handleSendTicket() {

        //Check event selected
        if (selectedEventId == -1) {
            showError("Select an event first!");
            return;
        }

        try {
            int amount = Integer.parseInt(txtAmount.getText());
            String type = comboTicketType.getValue();
            String email = txtEmail.getText();

            //Basic validation
            if (amount <= 0) {
                showError("Amount must be greater than 0!");
                return;
            }

            if (email == null || email.isEmpty()) {
                showError("Email is required!");
                return;
            }

            // CREATE MULTIPLE TICKETS
            for (int i = 0; i < amount; i++) {

                Ticket ticket = new Ticket(
                        selectedEventId,
                        1, //Placeholder
                        type,
                        1, //ONE ticket per row
                        email
                );

                // Generate unique barcode for each ticket
                ticket.setBarcode(generateBarcode());

                ticketManager.buyTicket(ticket);
            }

            // ✅ Success message
            showSuccess(amount + " tickets created successfully!");

            // 🔄 Reset UI
            clearFields();
            loadEvents();

        } catch (NumberFormatException e) {
            showError("Amount must be a number!");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Something went wrong!");
        }
    }

    // ---------------- BARCODE ----------------
    private String generateBarcode() {
        return "TCK-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    // ---------------- PRINT ----------------
    @FXML
    private void handlePrintTicket() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/Views/PrintTickets.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Print Tickets");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- CLEAR ----------------
    private void clearFields() {
        txtAmount.clear();
        txtEmail.clear();
        comboTicketType.getSelectionModel().selectFirst();
    }

    // ---------------- ALERTS ----------------
    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showSuccess(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    @FXML
    private void Close(javafx.event.ActionEvent event) {
        closeWindow(event);
    }
}