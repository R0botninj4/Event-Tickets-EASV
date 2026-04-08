package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.BE.Ticket;
import dk.easv.event_tickets_easv_bar.BLL.TicketManager;
import dk.easv.event_tickets_easv_bar.DAL.EventDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintTicketsController {

    private final TicketManager ticketManager = new TicketManager();
    private final EventDAO eventDAO = new EventDAO();

    // 🔹 TABLE
    @FXML private TableView<Ticket> ticketTable;
    @FXML private TableColumn<Ticket, String> colEvent;
    @FXML private TableColumn<Ticket, String> colType;
    @FXML private TableColumn<Ticket, String> colAmount;
    @FXML private TableColumn<Ticket, String> colEmail;
    @FXML private TableColumn<Ticket, String> colDate;

    // 🔹 SEARCH
    @FXML private TextField txtSearch;

    // 🔹 DATA
    private ObservableList<Ticket> tickets = FXCollections.observableArrayList();

    // 🔹 Cache (EventID → EventName)
    private Map<Integer, String> eventMap = new HashMap<>();

    @FXML
    public void initialize() {
        loadEventNames();
        setupTable();

        tickets.addAll(ticketManager.getAllTickets()); // 🔥 load tickets

        setupSearch(); // 🔥 live search + sorting
    }

    // ---------------- LOAD EVENT NAMES ----------------
    private void loadEventNames() {
        List<Event> events = eventDAO.getAllEvents();

        for (Event e : events) {
            eventMap.put(e.getId(), e.getName());
        }
    }

    // ---------------- TABLE SETUP ----------------
    private void setupTable() {

        colEvent.setCellValueFactory(data -> {
            String name = eventMap.getOrDefault(
                    data.getValue().getEventId(),
                    "Unknown"
            );
            return new SimpleStringProperty(name);
        });

        colType.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTicketType())
        );

        colAmount.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getAmount()))
        );

        colEmail.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEmail())
        );

        colDate.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getPurchaseDate() != null
                                ? data.getValue().getPurchaseDate().toString()
                                : ""
                )
        );
    }

    // ---------------- SEARCH ----------------
    private void setupSearch() {

        FilteredList<Ticket> filtered = new FilteredList<>(tickets, p -> true);

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            filtered.setPredicate(ticket -> {

                if (newVal == null || newVal.isEmpty()) return true;

                String search = newVal.toLowerCase();

                String eventName = eventMap.getOrDefault(ticket.getEventId(), "").toLowerCase();
                String email = ticket.getEmail() != null ? ticket.getEmail().toLowerCase() : "";
                String type = ticket.getTicketType() != null ? ticket.getTicketType().toLowerCase() : "";

                if (eventName.contains(search)) return true;
                if (email.contains(search)) return true;
                if (type.contains(search)) return true;
                if (String.valueOf(ticket.getAmount()).contains(search)) return true;

                return false;
            });
        });

        SortedList<Ticket> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(ticketTable.comparatorProperty());

        ticketTable.setItems(sorted);
    }

    // ---------------- PRINT SELECTED ----------------
    @FXML
    private void handlePrintSelected() {

        Ticket ticket = ticketTable.getSelectionModel().getSelectedItem();

        if (ticket == null) {
            new Alert(Alert.AlertType.ERROR, "Select a ticket first!").showAndWait();
            return;
        }

        String eventName = eventMap.getOrDefault(ticket.getEventId(), "Unknown");

        System.out.println("===== PRINTING TICKET =====");
        System.out.println("Event: " + eventName);
        System.out.println("Customer ID: " + ticket.getCustomerId());
        System.out.println("Type: " + ticket.getTicketType());
        System.out.println("Amount: " + ticket.getAmount());
        System.out.println("Email: " + ticket.getEmail());
        System.out.println("Purchase Date: " + ticket.getPurchaseDate());
        System.out.println("BarCode: " + ticket.getBarcode());
        System.out.println("===========================");
    }

    // ---------------- CLOSE ----------------
    @FXML
    private void handleClose() {
        Stage stage = (Stage) ticketTable.getScene().getWindow();
        stage.close();
    }
}