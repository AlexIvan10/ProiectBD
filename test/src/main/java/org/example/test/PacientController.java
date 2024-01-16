package org.example.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PacientController implements Initializable {

    @FXML
    private Label dataProgramarePacientFld;

    @FXML
    private Label durataPacientFld;

    @FXML
    private Label numePacientFld;

    @FXML
    private Label oraProgramarePacientFld;

    @FXML
    private Label prenumePacientFld;

    @FXML
    private Label pretPacientFld;

    @FXML
    private Label serviciuPacientFld;

    @FXML
    protected void logOutButtonAction(ActionEvent e){
        HelloController.logOutButtonAction(e, PacientController.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        String pacientInfo = "SELECT Nume, Prenume FROM Utilizatori, Pacienti WHERE utilizatori.id_utilizator = pacienti.id_utilizator AND Email = '" + UserData.getEmail() + "'";
        String pacientProgramare = "SELECT OraProgramare, DataProgramare, NumeServiciu, Pret, DurataMinuta" +
                             " FROM utilizatori, pacienti, serviciimedicale, programarepacienti" +
                             " WHERE utilizatori.id_utilizator = pacienti.id_utilizator AND pacienti.ID_Pacient = programarepacienti.ID_Pacient AND programarepacienti.ID_Serviciu = serviciimedicale.ID_Serviciu AND Email = '" + UserData.getEmail() + "'";


        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(pacientInfo);
            while(resultSet.next()){
                numePacientFld.setText(resultSet.getString("Nume"));
                prenumePacientFld.setText(resultSet.getString("Prenume"));
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery(pacientProgramare);
            if(resultSet.next()){
                dataProgramarePacientFld.setText(resultSet.getString("DataProgramare"));
                    oraProgramarePacientFld.setText(resultSet.getString("OraProgramare"));
                    serviciuPacientFld.setText(resultSet.getString("NumeServiciu"));
                    pretPacientFld.setText(resultSet.getString("Pret") + " RON");
                    durataPacientFld.setText(resultSet.getString("DurataMinuta") + " minute");
            }
            else{
                dataProgramarePacientFld.setText("Pacientul nu este programat");
                    oraProgramarePacientFld.setText("-");
                    serviciuPacientFld.setText("-");
                    pretPacientFld.setText("-");
                    durataPacientFld.setText("-");
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
