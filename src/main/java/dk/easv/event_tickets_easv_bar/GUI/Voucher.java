package dk.easv.event_tickets_easv_bar.GUI;

import dk.easv.event_tickets_easv_bar.GUI.Interface.ClosableWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Voucher implements ClosableWindow {

    @FXML
    private void Close(ActionEvent event) {
        closeWindow(event);
    }


}
