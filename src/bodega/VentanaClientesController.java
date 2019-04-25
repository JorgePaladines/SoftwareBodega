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
        cargarClientes();
        
        for(int i = 0; i < 6; i++){
            if(this.usuario.getPrivilegios()[i].equalsIgnoreCase("CREATE")){
                this.agregarCliente.setDisable(false);
                break;
            }
        }
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
