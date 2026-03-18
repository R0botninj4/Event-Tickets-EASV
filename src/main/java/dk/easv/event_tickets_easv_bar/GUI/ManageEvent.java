package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.GUI.Interface.ClosableWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ManageEvent implements ClosableWindow {

    @FXML
    private Button closeButton; // knappen, der lukker vinduet

    @FXML
    private void Close(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    public void initialize() {
        enableEscClose(closeButton); // <-- gør Esc til close
    }
}
