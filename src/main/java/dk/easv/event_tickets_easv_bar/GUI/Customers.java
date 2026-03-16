package dk.easv.event_tickets_easv_bar.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Customers implements ClosableWindow {

    @FXML
    private void Close(ActionEvent event) {
        closeWindow(event);
    }
}
