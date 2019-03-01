package bodega;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bodega.model.Conexion;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SEGUIRESA-PC
 */
public class AdministrarCamposVentanaController implements Initializable {

    @FXML
    private Button backButton;
    
    private Conexion conexion;
    @FXML
    private SplitPane splitPane;
    @FXML
    private VBox tipoChoiceBox;
    @FXML
    private Label lTipo;
    @FXML
    private ChoiceBox<String> choiceTipo;
    @FXML
    private VBox nombreTextBox;
    @FXML
    private Label lNombre;
    @FXML
    private TextField tNombre;
    @FXML
    private Button bCrear;
    @FXML
    private Button bEliminar;
    @FXML
    private ChoiceBox<String> choiceCampo;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.conexion = new Conexion();
    }

    @FXML
    private void back(ActionEvent event) throws IOException{
        try{this.conexion.close();}
        catch(SQLException e){System.err.println(e);}
        
        Parent root = FXMLLoader.load(getClass().getResource("Inicio.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void crearCampo(ActionEvent event) {
    }

    @FXML
    private void eliminarCampo(ActionEvent event) {
    }
    
}
