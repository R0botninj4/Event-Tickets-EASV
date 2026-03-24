package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.Event;
import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.BLL.EventManager;
import dk.easv.event_tickets_easv_bar.GUI.Login.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Button btnCreateEditEvent;
    @FXML
    private Button btnManageEvent;
    @FXML
    private Button btnCreateTicket;
    @FXML
    private Button btnCustomers;
    @FXML
    private Button btnVoucher;
    // Login & User info
    @FXML
    private Label welcomeText;
    @FXML
    private Button btnManageUsers;
    @FXML
    private Label lblUserRole;
    private User loggedInUser;

    // Event TableView
    @FXML
    private TableView<Event> tblEvents;
    @FXML
    private TableColumn<Event, String> colName;
    @FXML
    private TableColumn<Event, String> colLocation;
    @FXML
    private TableColumn<Event, String> colInfo;
    @FXML
    private TableColumn<Event, Object> colDate;
    @FXML
    private TableColumn<Event, Integer> colSold;
    @FXML
    private TableColumn<Event, String> colStatus;
    @FXML
    private TableColumn<Event, String> colCoordinator;
    @FXML
    private TextField txtSearch;

    private FilteredList<Event> filteredEvents;

    @FXML
    private EventManager eventManager;
    private ObservableList<Event> events;

    public HelloController() {
        eventManager = new EventManager();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Login / Session setup
        loggedInUser = Session.getUser();
        if (loggedInUser != null) {
            if (loggedInUser.getRoleInt() == 1) {
                lblUserRole.setText("Admin");
                btnVoucher.setVisible(false);
                btnVoucher.setManaged(false);
                btnCreateEditEvent.setVisible(false);
                btnCreateEditEvent.setManaged(false);
                btnCustomers.setVisible(false);
                btnCustomers.setManaged(false);
                btnCreateTicket.setVisible(false);
                btnCreateTicket.setManaged(false);
            } else if (loggedInUser.getRoleInt() == 2) {
                lblUserRole.setText("Coordinator");
                btnManageUsers.setVisible(false);
                btnManageUsers.setManaged(false);

            } else if (loggedInUser.getRoleInt()==67) {
                lblUserRole.setText("GOD");
            }

        }


        //Live search
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredEvents.setPredicate(event -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (event.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (event.getLocation().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (event.getInfo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });
        // Event Table setup
        setupTable();
        loadData();
    }
    @FXML
    private void handleFilter(){
    }
    private void setupTable() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colSold.setCellValueFactory(new PropertyValueFactory<>("ticketsSold"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCoordinator.setCellValueFactory(new PropertyValueFactory<>("coordinatorName"));


    }

    private void loadData() {
        events = FXCollections.observableArrayList(eventManager.getAllEvents());

        filteredEvents = new FilteredList<>(events, p -> true);

        SortedList<Event> sortedData = new SortedList<>(filteredEvents);
        sortedData.comparatorProperty().bind(tblEvents.comparatorProperty());

        tblEvents.setItems(sortedData);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void onClickOpenManageUsers(ActionEvent actionEvent) throws IOException {
        openWindow("/dk/easv/Views/ManageUsers-view.fxml", "Manage Users");
    }

    @FXML
    private void onClickOpenVoucher(ActionEvent actionEvent) throws IOException {
        openWindow("/dk/easv/Views/Voucher-view.fxml", "Voucher Info");
    }

    @FXML
    private void onClickOpenCreateEditEvent(ActionEvent actionEvent) throws IOException {
        openWindow("/dk/easv/Views/CreateEditEvent-view.fxml", "Event");
    }

    @FXML
    private void onClickOpenManageEvents(ActionEvent actionEvent) throws IOException {
        openWindow("/dk/easv/Views/ManageEvent-view.fxml", "Manage Events");
    }

    @FXML
    private void onClickOpenCreateTicket(ActionEvent actionEvent) throws IOException {
        openWindow("/dk/easv/Views/CreateTicket-view.fxml", "Create Ticket");
    }

    @FXML
    private void onClickOpenCustomers(ActionEvent actionEvent) throws IOException {
        openWindow("/dk/easv/Views/Customers-view.fxml", "Customers");
    }

    @FXML
    private void onClickOpenViewEvent(ActionEvent actionEvent) throws IOException {
        dk.easv.event_tickets_easv_bar.BE.Event selectedEvent = tblEvents.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Event Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an event from the list first.");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/Views/Event-view.fxml"));
        Scene scene = new Scene(loader.load());

        EventController controller = loader.getController();
        controller.setEvent(selectedEvent);

        Stage stage = new Stage();
        stage.setTitle("Event Info");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void handleLogout(ActionEvent actionEvent) throws IOException {
        Session.clear();
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/Views/Login-view.fxml"));
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loader.load()));
        loginStage.setTitle("Login");
        loginStage.show();
    }

    // Genbrugsmethod til åbning af nye vinduer
    private void openWindow(String path, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}