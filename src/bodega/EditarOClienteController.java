/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import bodega.model.Producto;
import bodega.model.Usuario;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SEGUIRESA-PC
 */
public class EditarOClienteController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Label nombreProducto;
    @FXML
    private ImageView imageView;
    @FXML
    private Button bEditarProducto;
    @FXML
    private Button bAdministrarClientes;
    
    private boolean update;
    
    private Producto producto;
    
    private Usuario usuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void initData(Usuario usuario, Producto p){
        this.usuario = usuario;
        this.producto = p;
        
        //Sólo se puede presionar el botón de editar si el usuario tiene permiso de UPDATE
        for(int i = 0; i < this.usuario.getPrivilegios().length; i++){
            if(this.usuario.getPrivilegios()[i].equalsIgnoreCase("UPDATE")){
                this.bEditarProducto.setDisable(false);
                this.update = true;
                break;
            }
        }
        
        try {
            this.nombreProducto.setText((String) this.producto.getListaCampos().getListaCampos().get(0).getCampo());
            this.imageView.setImage(new Image(new FileInputStream(new File(this.producto.getImagenLink()))));
        } catch (FileNotFoundException ex) {
            System.out.println("NO HAY IMAGEN");
        }
    }

    @FXML
    private void abrirEdicion(ActionEvent event) throws IOException {
        if(this.update){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditarProducto.fxml"));

            Stage stage = (Stage) bEditarProducto.getScene().getWindow();
            Scene scene = new Scene( (Parent) loader.load());
            stage.setScene(scene);

            EditarProductoController controller = loader.<EditarProductoController>getController();
            controller.initData(this.usuario, producto);
        }
        
    }

    @FXML
    private void abrirClientes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VentanaClientes.fxml"));

            Stage stage = (Stage) bEditarProducto.getScene().getWindow();
            Scene scene = new Scene( (Parent) loader.load());
            stage.setScene(scene);

            VentanaClientesController controller = loader.<VentanaClientesController>getController();
            controller.initData(this.usuario, producto);
    }
    
    @FXML
    private void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CargarDatosVentana.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene( (Parent) loader.load());
        stage.setScene(scene);
        
        CargarDatosVentanaController controller = loader.<CargarDatosVentanaController>getController();
        controller.initData(usuario);
    }
    
}
