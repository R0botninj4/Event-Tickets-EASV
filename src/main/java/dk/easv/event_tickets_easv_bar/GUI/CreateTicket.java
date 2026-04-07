package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.BE.Ticket;
import dk.easv.event_tickets_easv_bar.BLL.TicketManager;
import dk.easv.event_tickets_easv_bar.DAL.EventDAO;
import dk.easv.event_tickets_easv_bar.GUI.Interface.ClosableWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CreateTicket implements ClosableWindow {

    private final EventDAO eventDAO = new EventDAO();
    private final TicketManager ticketManager = new TicketManager();

    // 🔹 TABLE
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> colName;
    @FXML private TableColumn<Event, String> colDate;
    @FXML private TableColumn<Event, String> colLocation;
    @FXML private TableColumn<Event, String> colCoordinator;
    @FXML private TableColumn<Event, String> colTicketsLeft;
    @FXML private TableColumn<Event, String> colTicketsSold;

    // 🔹 SEARCH
    @FXML private TextField txtSearch;

    // 🔹 EVENT DISPLAY (READ-ONLY)
    @FXML private TextField txtEventName;
    @FXML private TextField txtDate;
    @FXML private TextField txtLocation;

    // 🔹 TICKET INPUT
    @FXML private TextField txtAmount;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> comboTicketType;

    // 🔹 BUTTON
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

        // 🔒 Make fields read-only
        txtEventName.setEditable(false);
        txtDate.setEditable(false);
        txtLocation.setEditable(false);
    }

    // ---------------- TABLE ----------------
    private void setupTable() {
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().toString()));
        colLocation.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));
        colCoordinator.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCoordinatorName()));


        colTicketsSold.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getTicketsSold()))
        );

        colTicketsLeft.setCellValueFactory(data -> {
            int left = data.getValue().getTicketAmount() - data.getValue().getTicketsSold();
            return new SimpleStringProperty(String.valueOf(left));
        });
    }

    // ---------------- LOAD EVENTS ----------------
    private void loadEvents() {
        List<Event> events = eventDAO.getAllEvents();
        allEvents.setAll(events);
        eventTable.setItems(allEvents);
    }

    // ---------------- SELECT EVENT ----------------
    private void setupSelection() {
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedEventId = newVal.getId();

                // 🔥 AUTO-FILL (READ ONLY)
                txtEventName.setText(newVal.getName());
                txtDate.setText(newVal.getDate().toString());
                txtLocation.setText(newVal.getLocation());
            }
        });
    }

    // ---------------- SEARCH ----------------
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

        if (selectedEventId == -1) {
            showError("Please select an event first!");
            return;
        }

        try {
            int amount = Integer.parseInt(txtAmount.getText());
            String type = comboTicketType.getValue();
            String email = txtEmail.getText();

            Ticket ticket = new Ticket(
                    selectedEventId,
                    1, // TODO replace with logged-in user
                    type,
                    amount,
                    email
            );

            ticketManager.buyTicket(ticket);

            showSuccess("Ticket created!");

            clearFields();
            loadEvents(); // refresh

        } catch (NumberFormatException e) {
            showError("Amount must be a number!");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Something went wrong!");
        }
    }

    //-----------------Print-----------------
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

    // ---------------- UI ----------------
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