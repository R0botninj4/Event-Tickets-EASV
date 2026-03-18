package dk.easv.event_tickets_easv_bar.GUI.Interface;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public interface ClosableWindow {

    default void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }

    default void enableEscClose(Button closeButton) {
        Scene scene = closeButton.getScene();
        if (scene != null) {
            addEscHandler(scene, closeButton);
        } else {
            closeButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    addEscHandler(newScene, closeButton);
                }
            });
        }
    }

    private void addEscHandler(Scene scene, Button closeButton) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                closeWindow(new ActionEvent(closeButton, null));
            }
        });
    }
}