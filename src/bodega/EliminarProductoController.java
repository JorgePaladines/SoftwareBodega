/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import bodega.model.Conexion;
import bodega.model.Producto;
import bodega.model.Usuario;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SEGUIRESA-PC
 */
public class EliminarProductoController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Label lTitulo;
    @FXML
    private Button bEliminar;
    @FXML
    private ListView<String> listView;
        
    private Conexion conn;
    private LinkedList<Producto> productos;
    
    private Usuario usuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*this.conn = new Conexion(this.usuario);
        
        ResultSet rs = this.conn.mostrarNombresDeProductos();
        //Lista de productos
        this.productos = new LinkedList<Producto>();
        //Lista visual de los nombres
        ObservableList<String> nombres = FXCollections.observableArrayList();
        int numeroDeColumnas = 7; //Son 7 columnas en cada Tabla de Campo
        try{
            while(rs.next()){
                Producto p = new Producto(rs,numeroDeColumnas);
                this.productos.add(p);
                nombres.add((String)p.getListaCampos().getListaCampos().getFirst().getCampo());
                //System.out.println(p.getListaCampos().getListaCampos().getFirst().getIdProducto());
                //System.out.println(p.getListaCampos().getListaCampos().getFirst().getCampo());
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        //Ahora colocar los nombres de los productos en la lista
        this.listView.setItems(nombres);*/
    }
    
    public void initData(Usuario usuario){
        this.usuario = usuario;
        
        this.conn = new Conexion(this.usuario);
        
        ResultSet rs = this.conn.mostrarNombresDeProductos();
        //Lista de productos
        this.productos = new LinkedList<Producto>();
        //Lista visual de los nombres
        ObservableList<String> nombres = FXCollections.observableArrayList();
        int numeroDeColumnas = 7; //Son 7 columnas en cada Tabla de Campo
        try{
            while(rs.next()){
                Producto p = new Producto(rs,numeroDeColumnas);
                this.productos.add(p);
                nombres.add((String)p.getListaCampos().getListaCampos().getFirst().getCampo());
                //System.out.println(p.getListaCampos().getListaCampos().getFirst().getIdProducto());
                //System.out.println(p.getListaCampos().getListaCampos().getFirst().getCampo());
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        //Ahora colocar los nombres de los productos en la lista
        this.listView.setItems(nombres);
    }

    @FXML
    private void back(ActionEvent event) throws IOException{
        //Cerrar la conexión antes de salir de la ventana
        try{
         this.conn.close();   
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Inicio.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene( (Parent) loader.load());
        stage.setScene(scene);
        
        InicioController controller = loader.<InicioController>getController();
        controller.setUser(usuario);
    }

    @FXML
    private void eliminar(ActionEvent event) {
        String seleccion = this.listView.getSelectionModel().getSelectedItem();
        if(seleccion != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("");
            alert.setHeaderText("VA A ELIMINAR UN PRODUCTO DEL INVENTARIO");
            alert.setContentText("PRODUCTO: " + seleccion + "\n\n¿Está seguro de que desea eliminar este producto del inventario? "
                    + "\nNo se podrá recuperar una vez realizado");
            ButtonType yesButton = new ButtonType("Aceptar", ButtonBar.ButtonData.NO);
            ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesButton, cancelButton);
            alert.showAndWait();
            if(alert.getResult() == yesButton){
                //System.out.println(this.listView.getItems().indexOf(seleccion));
                Producto p = this.productos.get(this.listView.getItems().indexOf(seleccion));
                File file = new File(p.getImagenLink());
                int idProducto = p.getIdProducto();
                //System.out.println(idProducto);
                boolean exitoso = this.conn.eliminarProducto(idProducto);
                
                Alert al = new Alert(Alert.AlertType.INFORMATION);
                if(exitoso){
                    file.delete();
                    
                    //Lanzar la alerta
                    System.out.println("Eliminación de producto exitosa");
                    al.setTitle("");
                    al.setHeaderText("SE HA ELIMINADO EL PRODUCTO");
                    al.setContentText("El producto se ha eliminado exitosamente");
                    al.showAndWait();
                    
                    this.listView.getItems().remove(seleccion);
                    seleccion = null;
                }
                else{
                    //Lanzar alerta
                    System.out.println("Eliminación de producto fallida");
                    al.setTitle("");
                    al.setHeaderText("NO SE HA PODIDO ELIMINAR EL PRODUCTO");
                    al.setContentText("Hubo una falla en la eliminación del producto");
                    al.showAndWait();
                }
            }
            else{
                System.out.println("Cancelado");
            }
        }
    }
}
