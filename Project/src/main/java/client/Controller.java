package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    VBox leftPanel, rightPanel;

    // МЕТОД: для закрытия программы
    public void btnExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    // МЕТОД: копирование
    public void corybtnAction(ActionEvent actionEvent) {

    }
}
