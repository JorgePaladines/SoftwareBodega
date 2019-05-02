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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SEGUIRESA-PC
 */
public class VentanaClientesController implements Initializable {

    @FXML
    private Button agregarCliente;
    @FXML
    private Button backButton;
    @FXML
    private TableView<Cliente> datosTabla;
    
    private ObservableList<Cliente> datos;
    
    private Conexion conexion;
    private Producto producto;
    private Usuario usuario;
    private int numColumnas;
    @FXML
    private TableColumn<Cliente, String> tcNombres;
    @FXML
    private TableColumn<Cliente, String> tcApellidos;
    @FXML
    private TableColumn<Cliente, String> tcTelefono;
    @FXML
    private TableColumn<Cliente, String> tcFecha;
    
    private boolean permisoUpdate;

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
        this.permisoUpdate = false;
        cargarClientes();
        
        for(int i = 0; i < this.usuario.getPrivilegios().length; i++){
            if(this.usuario.getPrivilegios()[i].equalsIgnoreCase("INSERT")){
                this.agregarCliente.setDisable(false);
            }
            if(this.usuario.getPrivilegios()[i].equalsIgnoreCase("UPDATE")){
                this.permisoUpdate = true;
            }
        }
        
        //Función para que se pueda hacer doble clic sobre cada columna y editar el cliente
        this.dobleClicEditar(); 
    }
    
    private void cargarClientes(){
        this.conexion = new Conexion(this.usuario);
        
        if(this.datos != null){
            this.datos.clear();
        }

        //Llamar al método que retorna todo el set de clientes
        ResultSet rs = this.conexion.mostrarClientes(this.producto.getIdProducto());
        
        this.datos = FXCollections.observableArrayList();
        
        //Ahora contar el número de columnas
        ResultSetMetaData rsmd;
        
        try{ //Se necesita un try catch por el rs
            
            /*Se guarda el número de columnas de cada fila.*/
            rsmd = rs.getMetaData();
            this.numColumnas = rsmd.getColumnCount();
            
            while(rs.next()){
                Cliente c = new Cliente(rs,this.numColumnas);
                this.datos.add(c);
            };
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        tcNombres.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombres"));
        tcApellidos.setCellValueFactory(new PropertyValueFactory<Cliente, String>("apellidos"));
        tcTelefono.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefono"));
        tcFecha.setCellValueFactory(new PropertyValueFactory<Cliente, String>("fecha_venta"));
        
        datosTabla.setItems(datos);
        
        try{
         this.conexion.close();   
        }
        catch(SQLException e){
            System.err.println(e);
        }
    }
    
    //Función para que se pueda hacer doble clic sobre cada columna y editar el cliente
    private void dobleClicEditar(){
        if(this.permisoUpdate){
            datosTabla.setRowFactory( tv -> {
                TableRow<Cliente> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                        //Se guarda el producto que se hizo doble clic
                        Cliente cliente = row.getItem();

                        //System.out.println(producto.getIdProducto());
                        try{
                            //Pasar el producto por parámetro al controlador de EditarProducto

                            //Cargar el FXML Loader, como tipo de objecto FXMLLoader
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClienteEditar.fxml"));

                            //Colocar la escena como en los demás casos
                            Stage stage = (Stage) backButton.getScene().getWindow();
                            Scene scene = new Scene( (Parent) loader.load());
                            stage.setScene(scene);

                            //Pasar el parámetro de esta manera
                            ClienteEditarController controller = loader.<ClienteEditarController>getController();
                            controller.initData(this.usuario, this.producto.getIdProducto(), this.producto, cliente);

                        }
                        catch(IOException e){
                            System.out.println("ERROR AL TRATAR DE EDITAR EL CLIENTE");
                            System.err.println(e);
                        }
                    }
                });
                return row ;
            });
        }
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

    @FXML
    private void actionAgregarCliente(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarCliente.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene( (Parent) loader.load());
        stage.setScene(scene);
        
        AgregarClienteController controller = loader.<AgregarClienteController>getController();
        controller.initData(usuario,this.producto.getIdProducto(), this.producto);
    }
    
}
