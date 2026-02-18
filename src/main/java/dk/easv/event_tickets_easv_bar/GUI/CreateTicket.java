package dk.easv.event_tickets_easv_bar.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CreateTicket {
    @FXML
    private void Close(ActionEvent event) {

        Stage stage = (Stage) ((Button) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }
}
