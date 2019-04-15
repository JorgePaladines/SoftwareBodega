/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author SEGUIRESA-PC
 */
public class InicioController implements Initializable {
    
    @FXML
    private AnchorPane anchor;
    @FXML
    private BorderPane border;
    @FXML
    private Button verInventario;
    @FXML
    private Button insertarProducto;
    @FXML
    private Button retirarProducto;
    @FXML
    private Button administrarCampos;
    @FXML
    private Button logOut;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("CargarDatosVentana.fxml"));
        Stage stage = (Stage) verInventario.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void insertarNuevo(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("InsertarProducto.fxml"));
        Stage stage = (Stage) insertarProducto.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    
    @FXML
    private void adminisCampos(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("AdministrarCamposVentana.fxml"));
        Stage stage = (Stage) administrarCampos.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void retirarProd(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("EliminarProducto.fxml"));
        Stage stage = (Stage) retirarProducto.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void logOutButton(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("LoginVentana.fxml"));
        Stage stage = (Stage) insertarProducto.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
