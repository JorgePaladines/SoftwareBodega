/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import bodega.model.Cliente;
import bodega.model.Conexion;
import bodega.model.Producto;
import bodega.model.Usuario;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
public class ClienteEditarController implements Initializable {

    @FXML
    private Label titulo;
    @FXML
    private Label labelProducto;
    @FXML
    private Label labelDescripcion;
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
    @FXML
    private Button backButton;
    
    private Usuario usuario;
    
    private int idProducto;
    
    private Producto producto;
    
    private Cliente cliente;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void initData(Usuario usuario, int idProducto, Producto producto, Cliente cliente){
        this.usuario = usuario;
        this.idProducto = idProducto;
        this.producto = producto;
        this.cliente = cliente;
        this.labelDescripcion.setText((String) this.producto.getListaCampos().getListaCampos().get(0).getCampo());
        
        this.tNombres.setText(cliente.getNombres());
        this.tApellidos.setText(cliente.getApellidos());
        this.tTelefono.setText(cliente.getTelefono());
        this.dFecha.setValue(LocalDate.parse(cliente.getFecha_venta()));
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
    private void actualizar(ActionEvent event) {
        Conexion conn = new Conexion(this.usuario);
        
        boolean exito = conn.actualizarCliente(this.cliente.getId(),
                                                tNombres.getText(),
                                                tApellidos.getText(),
                                                tTelefono.getText(),
                                                dFecha.getValue());
        
        if(exito){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("CLIENTE ACTUALIZADO CORRECTAMENTE");
            alert.setContentText("El cliente ha sido actualizado en la base de datos.");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("NO SE PUDO ACTUALIZAR EL CLIENTE");
            alert.setContentText("Hubo un problema en la actualización del cliente. Intente de nuevo");
            alert.showAndWait();
        }
        
        try{
            conn.close();
        }
        catch(SQLException e){
            System.err.println(e);
            System.out.println("ERROR AL CERRAR LA CONEXIÓN");
        }
    }
    
}
