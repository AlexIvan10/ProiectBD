package org.example.test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class OperatiiContabileExpertController implements Initializable{
    @FXML
    private Button backButtonE;

    @FXML
    private Label cheltuieliFld;

    @FXML
    private Label profitFld;

    @FXML
    private Label venitFld;

    @FXML
    public void backButton(ActionEvent e) {
        Stage stage = (Stage) backButtonE.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        DatabaseConnection connectionNow = new DatabaseConnection();
        connection = connectionNow.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        int polyclinicID = 0;
        int venit = 0;
        int cheltuieli = 0;
        int profit = 0;
        try{
            String getPolyclinicID = "SELECT ID_Policlinici FROM Utilizatori WHERE Email = '" + UserData.getEmail() + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getPolyclinicID);
            while(resultSet.next()){
                polyclinicID = resultSet.getInt("ID_Policlinici");
            }
            String getVenit = "SELECT utilizatori.id_policlinici, SumaPlatita " +
                                  "FROM Utilizatori, pacienti, bonurifiscale " +
                                  "WHERE Utilizatori.id_utilizator = pacienti.id_utilizator " +
                                  "AND pacienti.id_pacient = bonurifiscale.id_pacient " +
                                  "AND ID_Policlinici = " + polyclinicID;
            String getCheltuieli = "SELECT utilizatori.id_policlinici, Salariu " +
                                   "FROM Utilizatori, Angajat " +
                                   "WHERE utilizatori.id_utilizator = angajat.id_utilizator " +
                                   "AND ID_Policlinici = " + polyclinicID;
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getVenit);
            while(resultSet.next()){
                venit += resultSet.getInt("SumaPlatita");
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getCheltuieli);
            while(resultSet.next()){
                cheltuieli += resultSet.getInt("Salariu");
            }
            profit = venit - cheltuieli;
            venitFld.setText(String.valueOf(venit));
            cheltuieliFld.setText(String.valueOf(cheltuieli));
            profitFld.setText(String.valueOf(profit));
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
