/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import bodega.model.Conexion;
import bodega.model.Producto;
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
import javafx.scene.control.Button;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.conn = new Conexion();
        
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
        
        Parent root = FXMLLoader.load(getClass().getResource("Inicio.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void eliminar(ActionEvent event) {
        String seleccion = this.listView.getSelectionModel().getSelectedItem();
        if(seleccion != null){
            System.out.println(this.listView.getItems().indexOf(seleccion));
            Producto p = this.productos.get(this.listView.getItems().indexOf(seleccion));
            int idProducto = p.getIdProducto();
            
        }
    }
    
}