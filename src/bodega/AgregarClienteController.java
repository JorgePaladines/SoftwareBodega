/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import bodega.model.Conexion;
import bodega.model.Producto;
import bodega.model.Usuario;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SEGUIRESA-PC
 */
public class AgregarClienteController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Label titulo;
    @FXML
    private Label lNombres;
    @FXML
    private TextField tNombres;
    @FXML
    private Label lApellidos;
    @FXML
    private TextField tApellidos;
    @FXML
    private Label lTelefono;
    @FXML
    private TextField tTelefono;
    @FXML
    private Label lFecha;
    @FXML
    private DatePicker dFecha;
    @FXML
    private Button bAnadir;
    
    private Usuario usuario;
    
    private int idProducto;
    
    private Producto producto;
    
    private Conexion conn;
    @FXML
    private Label labelProducto;
    @FXML
    private Label labelDescripcion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void initData(Usuario usuario, int idProducto, Producto producto){
        this.usuario = usuario;
        this.idProducto = idProducto;
        this.producto = producto;
        this.labelDescripcion.setText((String) this.producto.getListaCampos().getListaCampos().get(0).getCampo());
    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VentanaClientes.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene( (Parent) loader.load());
        stage.setScene(scene);
        
        VentanaClientesController controller = loader.<VentanaClientesController>getController();
        controller.initData(this.usuario,this.producto);
    }

    @FXML
    private void anadir(ActionEvent event) {
        this.conn = new Conexion(this.usuario);
        
        boolean exito = this.conn.agregarCliente(idProducto, this.tNombres.getText(), this.tApellidos.getText(), this.tTelefono.getText(), this.dFecha.getValue().toString());
        
        if(exito){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("AGREGADO DE CLIENTE EXITOSO");
            alert.setContentText("Se ha añadido un nuevo cliente a la venta de este producto");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("AGREGADO DE CLIENTE FALLIDO");
            alert.setContentText("No se ha añadido un nuevo cliente a la venta de este producto");
            alert.showAndWait();
        }
        
        try{
         this.conn.close();   
        }
        catch(SQLException e){
            System.err.println(e);
        }
    }
    
}
