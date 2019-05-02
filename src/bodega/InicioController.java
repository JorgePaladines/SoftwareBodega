/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import bodega.model.Usuario;
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
    
    private Usuario usuario;

    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CargarDatosVentana.fxml"));
        Stage stage = (Stage) verInventario.getScene().getWindow();
        Scene scene = new Scene( (Parent) loader.load());
        stage.setScene(scene);
        
        CargarDatosVentanaController controller = loader.<CargarDatosVentanaController>getController();
        controller.initData(this.usuario);

        /*
        Parent root = FXMLLoader.load(getClass().getResource("CargarDatosVentana.fxml"));
        Stage stage = (Stage) verInventario.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        */
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void setUser(Usuario usuario){
        this.usuario = usuario;
        
        //Permisos
        
        //Estos dos son para ver si hay permisos de create o de drop. Si no hay ninguno, no se
        //habilita el botón de Administrar campos
        boolean create = false;
        boolean drop = false;
        
        //Se recorre la lista, sólo los 6 primeros elementos, y se habilitan aquellas opciones que sí pueden usarse
        for(int i = 0; i < this.usuario.getPrivilegios().length; i++){
            if(this.usuario.getPrivilegios()[i].equalsIgnoreCase("INSERT")){
                this.insertarProducto.setDisable(false);
            }
            if(this.usuario.getPrivilegios()[i].equalsIgnoreCase("DELETE")){
                this.retirarProducto.setDisable(false);
            }
            if(this.usuario.getPrivilegios()[i].equalsIgnoreCase("CREATE")){
                create = true;
            }
            if(this.usuario.getPrivilegios()[i].equalsIgnoreCase("DROP")){
                drop = true;
            }
        }
        
        //Aquí, si hay permiso de CREATE o de DROP, se habilita el botón.
        //Ya por dentro es que se ve cuál de ellas se permite
        if(create || drop)
            this.administrarCampos.setDisable(false);
    }

    @FXML
    private void insertarNuevo(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InsertarProducto.fxml"));
        Stage stage = (Stage) insertarProducto.getScene().getWindow();
        Scene scene = new Scene( (Parent) loader.load());
        stage.setScene(scene);
        
        InsertarProductoController controller = loader.<InsertarProductoController>getController();
        controller.initData(this.usuario);
    }
    
    @FXML
    private void adminisCampos(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdministrarCamposVentana.fxml"));
        Stage stage = (Stage) administrarCampos.getScene().getWindow();
        Scene scene = new Scene( (Parent) loader.load());
        stage.setScene(scene);
        
        AdministrarCamposVentanaController controller = loader.<AdministrarCamposVentanaController>getController();
        controller.initData(this.usuario);
    }

    @FXML
    private void retirarProd(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EliminarProducto.fxml"));
        Stage stage = (Stage) retirarProducto.getScene().getWindow();
        Scene scene = new Scene( (Parent) loader.load());
        stage.setScene(scene);
        
        EliminarProductoController controller = loader.<EliminarProductoController>getController();
        controller.initData(this.usuario);
    }

    @FXML
    private void logOutButton(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("LoginVentana.fxml"));
        Stage stage = (Stage) insertarProducto.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
