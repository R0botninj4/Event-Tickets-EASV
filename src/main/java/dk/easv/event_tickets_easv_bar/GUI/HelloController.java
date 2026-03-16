package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.GUI.Login.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Label welcomeText;

    @FXML
    private Button btnManageUsers;

    @FXML
    private Label lblUserRole;

    private User loggedInUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Hent bruger fra Session
        loggedInUser = Session.getUser();

        if (loggedInUser == null) return;

        if (loggedInUser.getRole() == 1) { // ADMIN
            lblUserRole.setText("Admin");
        }
        else if (loggedInUser.getRole() == 2) { // COORDINATOR
            lblUserRole.setText("Coordinator");

            // Coordinator må ikke se Manage Users
            btnManageUsers.setVisible(false);
            btnManageUsers.setManaged(false);

            //Vis der er ting der skal fjernes 
            //fx:id.setVisible(false);
            //fx:id.setManaged(false);
        }
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/Views/Event-view.fxml"));
        Scene scene = new Scene(loader.load());

        Stage stage = new Stage();
        stage.setTitle("Event Info");
        stage.setScene(scene);

        stage.setResizable(false); // only here

        stage.show();
    }

    @FXML
    private void handleLogout(ActionEvent actionEvent) throws IOException {

        // Ryd session
        Session.clear();

        // Hent stage fra den knap der blev trykket på
        Stage stage = (Stage) ((Button) actionEvent.getSource())
                .getScene()
                .getWindow();

        stage.close();

        // Åbn login igen
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/dk/easv/Views/Login-view.fxml")
        );

        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loader.load()));
        loginStage.setTitle("Login");
        loginStage.show();
    }


    // 🔥 Smart genbrugsmethode
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
