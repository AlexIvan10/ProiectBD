package org.example.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class PacientController {
    @FXML
    protected void logOutButtonAction(ActionEvent e){
        HelloController.logOutButtonAction(e, PacientController.class);
    }
}
