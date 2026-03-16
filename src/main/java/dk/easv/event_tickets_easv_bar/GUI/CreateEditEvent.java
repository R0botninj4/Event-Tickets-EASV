package dk.easv.event_tickets_easv_bar.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CreateEditEvent implements ClosableWindow {

    @FXML
    private void Close(ActionEvent event) {
        closeWindow(event);
    }
}