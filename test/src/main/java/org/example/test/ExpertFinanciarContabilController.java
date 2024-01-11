package org.example.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ExpertFinanciarContabilController {
    @FXML
    protected void logOutButtonAction(ActionEvent e){
        HelloController.logOutButtonAction(e, ExpertFinanciarContabilController.class);
    }
}
