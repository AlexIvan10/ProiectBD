package org.example.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ExpertFinanciarContabilController implements Initializable{
    @FXML
    private Label numeContabilFld;

    @FXML
    private Label oreContabilFld;

    @FXML
    private Label prenumeContabilFld;

    @FXML
    private Label salariuContabil;

    @FXML
    protected void logOutButtonAction(ActionEvent e){
        HelloController.logOutButtonAction(e, ExpertFinanciarContabilController.class);
    }

    @FXML
    protected void operatiiContabileButtonAction(ActionEvent e){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MedicController.class.getResource("OperatiiContabileExpert.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 870, 500);
            Stage stageGAR = new Stage();
            stageGAR.setScene(scene);
            stageGAR.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            String contabilInfo = "SELECT Nume, Prenume, NumarOre, Salariu FROM Utilizatori, Angajat WHERE Utilizatori.ID_Utilizator = Angajat.ID_Utilizator AND Email = '" + UserData.getEmail() + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(contabilInfo);
            while(resultSet.next()){
                numeContabilFld.setText(resultSet.getString("Nume"));
                prenumeContabilFld.setText(resultSet.getString("Prenume"));
                oreContabilFld.setText(resultSet.getString("NumarOre"));
                salariuContabil.setText(resultSet.getString("Salariu"));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            closeOperation(connection);
            closeOperation(statement);
            closeOperation(resultSet);
        }
    }
    public void closeOperation(AutoCloseable operation) {
        try {
            if (operation != null) {
                operation.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
