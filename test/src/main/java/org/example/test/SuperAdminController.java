package org.example.test;

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

public class SuperAdminController implements Initializable{
     @FXML
    private TableView<UsersSA> TableSuperAdmin;

    @FXML
    private TableColumn<UsersSA, String> colAdresaSA;

    @FXML
    private TableColumn<UsersSA, String> colCNPSA;

    @FXML
    private TableColumn<UsersSA, String> colEmailSA;

    @FXML
    private TableColumn<UsersSA, String> colFunctieSA;

    @FXML
    private TableColumn<UsersSA, String> colIBANSA;

    @FXML
    private TableColumn<UsersSA, String> colNrTelSA;

    @FXML
    private TableColumn<UsersSA, String> colNumeSA;

    @FXML
    private TableColumn<UsersSA, String> colPrenumeSA;

    ObservableList<UsersSA> list;
    @FXML
    protected void logOutButtonAction(ActionEvent e){
        HelloController.logOutButtonAction(e, SuperAdminController.class);
    }
    public void getUsers(){
        Connection connection = null;
        Statement selectStatement = null;
        ResultSet rs = null;
        ObservableList<UsersSA> tempList = FXCollections.observableArrayList();

        String getAllData  = "SELECT * FROM Utilizatori";

        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();

        try{
            selectStatement = connection.createStatement();
            selectStatement.execute(getAllData);
            rs = selectStatement.executeQuery(getAllData);

            while(rs.next()){
                tempList.add(new UsersSA(rs.getString("Nume"), rs.getString("Prenume"), rs.getString("Email"), rs.getString("Adresa"), rs.getString("NumarTelefon"), rs.getString("CNP"), rs.getString("ContIBAN"), rs.getString("Functie")));
            }
        }catch (Exception sqlex){
            System.err.println("An SQL Exception occured. Details are provided below:");
            sqlex.printStackTrace(System.err);
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch(SQLException ex) {
                }
                rs = null;
            }
            if (selectStatement != null) {
                try {
                    selectStatement.close();
                }
                catch(SQLException ex) {}
                selectStatement = null;
            }
            if (connection != null) {
                try {
                    connection.close();
                }
                catch(SQLException ex) {}
                connection = null;
            }
        }
        list = tempList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colNumeSA.setCellValueFactory(new PropertyValueFactory<UsersSA, String>("Nume"));
        colPrenumeSA.setCellValueFactory(new PropertyValueFactory<UsersSA, String>("Prenume"));
        colEmailSA.setCellValueFactory(new PropertyValueFactory<UsersSA, String>("Email"));
        colAdresaSA.setCellValueFactory(new PropertyValueFactory<UsersSA, String>("Adresa"));
        colNrTelSA.setCellValueFactory(new PropertyValueFactory<UsersSA, String>("NumarTelefon"));
        colCNPSA.setCellValueFactory(new PropertyValueFactory<UsersSA, String>("CNP"));
        colIBANSA.setCellValueFactory(new PropertyValueFactory<UsersSA, String>("IBAN"));
        colFunctieSA.setCellValueFactory(new PropertyValueFactory<UsersSA, String>("Functie"));
        getUsers();
        TableSuperAdmin.setItems(list);
    }
}
