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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
        //Se llenan los choiceboxes
        //Primero la lista de tipos de campos: String, Integer o Float
        ObservableList<String> tiposDeCampos = FXCollections.observableArrayList();
        tiposDeCampos.addAll("String","Integer","Float");
        this.choiceTipo.setItems(tiposDeCampos);
        
        //Luego la lista de campos existentes. Estos pueden ser seleccionados para ser eliminados
        ObservableList<String> nombresCampos = FXCollections.observableArrayList();
        nombresCampos.setAll(this.conexion.obtenerCamposNombres());
        nombresCampos.remove(nombresCampos.size()-1); //Se le resta 1 puesto que la lista arrojada contiene
        //la palabra "productos", lo que no es un campo a eliminar
        this.choiceCampo.setItems(nombresCampos);
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
        String nombre = this.tNombre.getText();
        String tipo = this.choiceTipo.getValue();
        boolean datosBienColocados = true;
        boolean creacionExitosa = true;
        
        if(nombre.equalsIgnoreCase("")){
            System.out.println("Nombre vacío");
            datosBienColocados = false;
        }
        if(tipo == null){
            System.out.println("Tipo vacío");
            datosBienColocados = false;
        }
        if(datosBienColocados){
            //System.out.println(nombre);
            //System.out.println(tipo);
            nombre = nombre.toLowerCase();
            creacionExitosa = this.conexion.crearCampo(nombre, tipo);
        }
        else{
            creacionExitosa = false;
            //Mandar alerta
        }
        
        if(creacionExitosa){
            
        }
    }

    @FXML
    private void eliminarCampo(ActionEvent event) {
    }
    
}
