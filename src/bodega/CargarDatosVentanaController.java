/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import bodega.model.Campo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableRow;
import javafx.stage.Stage;
import javafx.util.Callback;
import bodega.model.Conexion;
import bodega.model.Producto;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * FXML Controller class
 *
 * @author SEGUIRESA-PC
 */
public class CargarDatosVentanaController implements Initializable {

    @FXML
    private Button callButton;
    @FXML
    private TableView<Producto> datosTabla;
    
    private ObservableList<Producto> datos;
    
    private Conexion conexion;
    @FXML
    private Button backButton;

    /*Esto servirá para que se muestre en el UI todas las columnas que tiene el producto del inventario
    Como debe poder agregarse campos al producto, deben primero contarse cuántos campos hay para que
    se puedan mostrar como columnas en la interfaz*/
    private int numColumnas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Hacer la conexión
        this.conexion = new Conexion();
   
        //Llamar al método que retorna todo el set de productos
        ResultSet rs = this.conexion.mostrarDatos();

        //Crear la lista visible, vacía al inicio
        this.datos = FXCollections.observableArrayList();
        
        //Ahora contar el número de columnas
        ResultSetMetaData rsmd;
        
        //Recorrer el set y colocar la información del producto en un nuevo objeto Producto
        try{ //Se necesita un try catch por el rs
            
            /*Se guarda el número de columnas de cada fila.*/
            rsmd = rs.getMetaData();
            this.numColumnas = rsmd.getColumnCount();
            
            /*Esto sólo hace que se llene el Observable List con los productos
            Va a hacer falta que se muestren los campos correctos
            
            Eso lo hace la función mostrarDatos()*/
            while(rs.next()){
                Producto p = new Producto(rs,this.numColumnas);
                this.datos.add(p);
            };
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        //Función para que se pueda hacer doble clic sobre cada columna y editar el producto
        this.dobleClicEditar();
        
        try{
            this.conexion.close();
        }
        catch(SQLException e){
            System.err.println(e);
            System.out.println("ERROR AL CERRAR LA CONEXIÓN");
        }
            
    }
    
    //Función para que se pueda hacer doble clic sobre cada columna y editar el producto
    private void dobleClicEditar(){
        datosTabla.setRowFactory( tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    //Se guarda el producto que se hizo doble clic
                    Producto producto = row.getItem();
                    
                    //System.out.println(producto.getIdProducto());
                    try{
                        //Pasar el producto por parámetro al controlador de EditarProducto
                        
                        //Cargar el FXML Loader, como tipo de objecto FXMLLoader
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditarProducto.fxml"));
                        
                        //Colocar la escena como en los demás casos
                        Stage stage = (Stage) backButton.getScene().getWindow();
                        Scene scene = new Scene( (Parent) loader.load());
                        stage.setScene(scene);
                        
                        //Pasar el parámetro de esta manera
                        EditarProductoController controller = loader.<EditarProductoController>getController();
                        controller.initData(producto);
                        
                    }
                    catch(IOException e){
                        System.out.println("ERROR AL TRATAR DE EDITAR EL PRODUCTO");
                        System.err.println(e);
                    }
                }
            });
            return row ;
        });
    }

    //Cuando se hace clic en el botón "Cargar Datos", se colocan los items en la tabla
    @FXML
    private void cargarDatos(ActionEvent event) {
        
        int contar = 0;//Esto será para que no se agreguen más columnas de las que deben ser
        
        for(int i = 0; i < this.datos.size(); i++){
            for(int j = 0; j < this.conexion.numeroCampos(); j++){
                final int finalIdx = j;
                //this.datos.get(i).setCampo(j);
                
                //System.out.println("Titulo: "+this.datos.get(i).getTituloCampo(j));
                //System.out.println("Campo: "+this.datos.get(i).getCampo(j).getCampo());
                
                TableColumn<Producto, Object> tc = new TableColumn<Producto,Object>(this.datos.get(i).getTituloCampo(j));
                
                //Asignar lo que debe leer el campo de texto de la columna
                tc.setCellValueFactory(param ->
                    new ReadOnlyObjectWrapper<>(param.getValue().getCampo(finalIdx).getCampo())
                );

                //Para evitar que se repitan columnas
                if(contar<this.conexion.numeroCampos()){
                    this.datosTabla.getColumns().add(tc);
                    contar++;
                }
                
                //System.out.println("");
            }
        }
        datosTabla.setItems(datos);
    }

    //Botón para regresar
    @FXML
    private void back(ActionEvent event) throws IOException{
        try{
         this.conexion.close();   
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        Parent root = FXMLLoader.load(getClass().getResource("Inicio.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    
}
