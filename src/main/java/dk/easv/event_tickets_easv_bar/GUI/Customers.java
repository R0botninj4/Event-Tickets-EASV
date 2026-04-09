package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.CustomerOverview;
import dk.easv.event_tickets_easv_bar.DAL.UserDAO;
import dk.easv.event_tickets_easv_bar.GUI.Interface.ClosableWindow;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class Customers implements ClosableWindow {

    @FXML private Button closeButton;
    @FXML private TableView<CustomerOverview> customerTable;
    @FXML private TableColumn<CustomerOverview, String> colName;
    @FXML private TableColumn<CustomerOverview, String> colPhone;
    @FXML private TableColumn<CustomerOverview, String> colEmail;
    @FXML private TableColumn<CustomerOverview, String> colEvent;
    @FXML private TableColumn<CustomerOverview, Integer> colTickets;
    @FXML private TextField searchField;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        enableEscClose(closeButton);

        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colPhone.setCellValueFactory(data -> data.getValue().phoneNumberProperty());
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());
        colEvent.setCellValueFactory(data -> data.getValue().eventNameProperty());
        colTickets.setCellValueFactory(data -> data.getValue().ticketsBoughtProperty().asObject());

        loadCustomers();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            List<CustomerOverview> all = userDAO.getCustomerOverview();
            if (newVal == null || newVal.isBlank()) {
                customerTable.setItems(FXCollections.observableArrayList(all));
            } else {
                String filter = newVal.toLowerCase();
                customerTable.setItems(FXCollections.observableArrayList(
                        all.stream()
                                .filter(c -> c.getName().toLowerCase().contains(filter)
                                        || c.getEmail().toLowerCase().contains(filter)
                                        || c.getPhoneNumber().toLowerCase().contains(filter)
                                        || c.getEventName().toLowerCase().contains(filter))
                                .toList()
                ));
            }
        });
    }

    private void loadCustomers() {
        List<CustomerOverview> customers = userDAO.getCustomerOverview();
        System.out.println(customers); //debug
        customerTable.setItems(FXCollections.observableArrayList(customers));
    }

    @FXML
    private void Close(ActionEvent event) {
        closeWindow(event);
    }
}