package org.example.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ResurseUmaneController {
    @FXML
    protected void logOutButtonAction(ActionEvent e){
        HelloController.logOutButtonAction(e, ResurseUmaneController.class);
    }
}
