package org.example.test;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class DeleteSuperAdminErrorController {
    @FXML
    private Button inapoiButtonSA;
    public void inapoiButton(ActionEvent e) {
        Stage stage = (Stage) inapoiButtonSA.getScene().getWindow();
        stage.close();
    }
}
