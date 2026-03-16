package dk.easv.event_tickets_easv_bar.GUI.Interface;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public interface ClosableWindow {

    default void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }
}