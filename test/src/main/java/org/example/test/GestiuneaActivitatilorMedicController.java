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

public class GestiuneaActivitatilorMedicController implements Initializable{
    @FXML
    private TableColumn<UsersM, String> dataColM;

    @FXML
    private TableColumn<UsersM, String> durataColM;

    @FXML
    private TableColumn<UsersM, String> numeColM;

    @FXML
    private TableColumn<UsersM, String> prenumeColM;

    @FXML
    private TableColumn<UsersM, String> pretColM;

    @FXML
    private TableColumn<UsersM, String> serviciuColM;

    @FXML
    private TableColumn<UsersM, String> raportColM;

    @FXML
    private TableView<UsersM> tableMedic;

    @FXML
    private Button backButtonM;

    ObservableList<UsersM> list;
    @FXML
    public void backButton(ActionEvent e) {
        Stage stage = (Stage) backButtonM.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numeColM.setCellValueFactory(new PropertyValueFactory<UsersM, String>("NumePacient"));
        prenumeColM.setCellValueFactory(new PropertyValueFactory<UsersM, String>("PrenumePacient"));
        serviciuColM.setCellValueFactory(new PropertyValueFactory<UsersM, String>("Serviciu"));
        dataColM.setCellValueFactory(new PropertyValueFactory<UsersM, String>("Data"));
        durataColM.setCellValueFactory(new PropertyValueFactory<UsersM, String>("Durata"));
        pretColM.setCellValueFactory(new PropertyValueFactory<UsersM, String>("Pret"));
        raportColM.setCellValueFactory(new PropertyValueFactory<UsersM, String>("RaportMedical"));
        getUsers();
        tableMedic.setItems(list);
    }
    public void getUsers(){
        Connection connection = null;
        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        Statement statement2 = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        ObservableList<UsersM> tempList = FXCollections.observableArrayList();
        String findMedicID = "SELECT ID_Medic from Utilizatori, Angajat, Medic WHERE utilizatori.ID_Utilizator = angajat.ID_Utilizator AND angajat.ID_Angajat = medic.ID_Medic AND Email = '" + UserData.getEmail() + "'";
        String selectPacienti = "SELECT ID_Policlinici, pacienti.ID_Pacient, Nume, Prenume, NumeServiciu, DataProgramare, DurataMinuta, Pret FROM utilizatori, pacienti, serviciimedicale, programarepacienti, medic WHERE utilizatori.ID_Utilizator = pacienti.ID_Utilizator AND pacienti.id_medic = medic.id_medic AND pacienti.id_pacient = programarepacienti.id_pacient AND serviciimedicale.id_serviciu = programarepacienti.id_serviciu AND Medic.ID_Medic = ? ";

        try{
            int medicID = 0;
            String raport = "";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(findMedicID);
            while(resultSet.next()){
                medicID = resultSet.getInt("ID_Medic");
            }
            preparedStatement = connection.prepareStatement(selectPacienti);
            preparedStatement.setInt(1, medicID);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String getRaport = "SELECT * FROM rapoartemedicale WHERE ID_Pacient = " + resultSet.getInt("ID_Pacient");
                statement2 = connection.createStatement();
                resultSet2 = statement2.executeQuery(getRaport);
                while(resultSet2.next()){
                    raport = resultSet2.getString("DetaliiRaport");
                }
                tempList.add(new UsersM(resultSet.getString("Nume"), resultSet.getString("Prenume"), resultSet.getString("NumeServiciu"), resultSet.getString("DataProgramare"), resultSet.getString("DurataMinuta"), resultSet.getString("Pret"), raport));
            }
        }catch (Exception sqlex){
            System.err.println("An SQL Exception occured. Details are provided below:");
            sqlex.printStackTrace(System.err);
        }
        finally {
            closeOperation(connection);
            closeOperation(statement);
            closeOperation(preparedStatement);
            closeOperation(resultSet);
        }
        list = tempList;
    }
    public void closeOperation(AutoCloseable operation){
        try {
            if (operation != null) {
                operation.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
