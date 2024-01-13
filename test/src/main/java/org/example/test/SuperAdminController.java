package org.example.test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

import javax.swing.*;
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
        updateTable();
    }
    public void updateTable(){
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
    public void removeUser(ActionEvent e){
        Connection connection;
        PreparedStatement preparedStatement = null;
        Statement statement = null;
        String userToDelete = "DELETE FROM Utilizatori WHERE ID_Utilizator = ?";
        String verifyPolyclinicID = "SELECT * FROM Utilizatori WHERE Email = ?";
        String verifySuperAdminPolyclinicID = "SELECT * FROM Utilizatori WHERE Email = '" + UserData.getEmail() + "'";

        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();

        try{
            TableView.TableViewSelectionModel<UsersSA> selectedID = TableSuperAdmin.getSelectionModel();
            UsersSA selectedUser = selectedID.getSelectedItem();
            if(selectedUser.getFunctie().equals("Super Admin")){
                FXMLLoader fxmlLoader = new FXMLLoader(SuperAdminController.class.getResource("DeleteSuperAdminError.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 400, 320);
                Stage stageESA = new Stage();
                stageESA.setScene(scene);
                stageESA.show();
            }
            else{
                ResultSet resultSet;
                int userPolyclinicID;
                int userID;
                int superAdminPolyclinicID = 0;
                statement = connection.createStatement();
                statement.execute(verifySuperAdminPolyclinicID);
                resultSet = statement.executeQuery(verifySuperAdminPolyclinicID);
                while(resultSet.next()){
                    superAdminPolyclinicID = resultSet.getInt("ID_Policlinici");
                }

                preparedStatement = connection.prepareStatement(verifyPolyclinicID);
                preparedStatement.setString(1, selectedUser.getEmail());
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    userPolyclinicID = resultSet.getInt("ID_Policlinici");
                    userID = resultSet.getInt("ID_Utilizator");
                    if(superAdminPolyclinicID == userPolyclinicID){
                        TableSuperAdmin.getItems().remove(selectedID.getSelectedIndex());
                        preparedStatement = connection.prepareStatement(userToDelete);
                        preparedStatement.setInt(1, userID);
                        preparedStatement.execute();
                        updateTable();
                    }
                    else{
                        FXMLLoader fxmlLoader = new FXMLLoader(SuperAdminController.class.getResource("DifferentPolyclinic.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 400, 320);
                        Stage stageEDP = new Stage();
                        stageEDP.setScene(scene);
                        stageEDP.show();
                    }
                }
            }

        }catch (Exception ex){
            System.err.println("An SQL Exception occured. Details are provided below:");
            ex.printStackTrace(System.err);
        }
    }
}
