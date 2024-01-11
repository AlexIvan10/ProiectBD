package org.example.test;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.*;

public class HelloController{
    @FXML
    private Label logInMessageLabel;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    protected void logInButtonOnAction(ActionEvent e) {
        if(!emailField.getText().isBlank() && !passwordField.getText().isBlank()){
            validateLogIn(e);
        }
        else{
            logInMessageLabel.setText("Please enter email and password");
        }
    }
    public void validateLogIn(ActionEvent e){
        Connection connection = null;
        Statement selectStatement = null;
        ResultSet rs = null;

        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();

        String verifyLogin = "SELECT * FROM utilizatori WHERE Email = '" + emailField.getText() + "' AND Nume = '" + passwordField.getText() + "'";

        try{

            selectStatement = connection.createStatement();
            selectStatement.execute(verifyLogin);
            rs = selectStatement.executeQuery(verifyLogin);
            if(rs.next()){
                String functie = rs.getString("Functie");
                Parent root;
                FXMLLoader loader;
                Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
                if(functie.equals("Medic")){
                    loader = new FXMLLoader(HelloController.class.getResource("Medic.fxml"));
                    root = loader.load();
                }
                else if(functie.equals("AsistentMedical")){
                    loader = new FXMLLoader(HelloController.class.getResource("AsistentMedical.fxml"));
                    root = loader.load();
                }
                else if(functie.equals("Receptioner")){
                    loader = new FXMLLoader(HelloController.class.getResource("Receptioner.fxml"));
                    root = loader.load();
                }
                else if(functie.equals("Expert Financiar Contabil")){
                    loader = new FXMLLoader(HelloController.class.getResource("ExpertFinanciarContabil.fxml"));
                    root = loader.load();
                }
                else if(functie.equals("Inspector Resurse Umane")){
                    loader = new FXMLLoader(HelloController.class.getResource("ResurseUmane.fxml"));
                    root = loader.load();
                }
                else if(functie.equals("Admin")){
                    loader = new FXMLLoader(HelloController.class.getResource("Admin.fxml"));
                    root = loader.load();
                }
                else if(functie.equals("Super Admin")){
                    loader = new FXMLLoader(HelloController.class.getResource("SuperAdmin.fxml"));
                    root = loader.load();
                }
                else {
                    loader = new FXMLLoader(HelloController.class.getResource("Pacient.fxml"));
                    root = loader.load();
                }

                stage.setScene(new Scene(root, 1000, 700));
                stage.show();
            }
            else{
                logInMessageLabel.setText("Invalid email or password");
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
    }
    public static void logOutButtonAction(ActionEvent e, Class<?> controllerClass) {
        try {
            Parent root;
            FXMLLoader loader = new FXMLLoader(controllerClass.getResource("hello-view.fxml"));
            root = loader.load();
            HelloController helloController = loader.getController();
            Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            stage.setTitle("Policlinica");
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}