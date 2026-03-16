package dk.easv.event_tickets_easv_bar.GUI.Login;

import dk.easv.event_tickets_easv_bar.BE.User;
import dk.easv.event_tickets_easv_bar.BLL.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;

    private final UserManager userManager = new UserManager();

    @FXML
    private void handleLogin(ActionEvent actionEvent) {
        try {
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            User user = userManager.login(username, password);

            if (user == null) {
                lblMessage.setText("Forkert brugernavn eller password");
                lblMessage.setStyle("-fx-text-fill: red;");
                return;
            }

            // GEM USER I SESSION
            Session.setUser(user);

            // ÅBN EVENT MAIN
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/dk/easv/Views/EventMain-view.fxml")
            );

            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Event Manager");
            stage.setScene(scene);
            stage.show();

            // LUK LOGIN
            ((Stage) txtUsername.getScene().getWindow()).close();

        } catch (Exception e) {
            lblMessage.setText("Fejl: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
