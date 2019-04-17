/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import bodega.model.Conexion;
import bodega.model.Producto;
import bodega.model.Usuario;
import java.sql.ResultSetMetaData;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author SEGUIRESA-PC
 */
public class InsertarProductoController implements Initializable {

    @FXML
    private Label tituloNuevoProd;
    @FXML
    private Button bInsertar;
    @FXML
    private Button backButton;
    @FXML
    private Label tituloModificar;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Spinner<Integer> modifCantText;
    @FXML
    private Text nota;
    
    private Conexion conexion;
    private ObservableList<String> datos;
    @FXML
    private Label prodNombre;
    @FXML
    private Label prodCantidad;
    @FXML
    private ScrollPane scrollPane;

    private Usuario usuario;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*this.conexion = new Conexion(this.usuario);
        ResultSet rs = this.conexion.mostrarDatos();
        this.datos = FXCollections.observableArrayList();
        
        //Llenar el combobox
        this.llenarComboBox(rs);
        this.comboBox.setItems(this.datos);
        
        //Llenar el ScrollPane
        this.llenarScrollPane();*/
    }
    
    public void initData(Usuario usuario){
        this.usuario = usuario;
        
        this.conexion = new Conexion(this.usuario);
        ResultSet rs = this.conexion.mostrarDatos();
        this.datos = FXCollections.observableArrayList();
        
        //Llenar el combobox
        this.llenarComboBox(rs);
        this.comboBox.setItems(this.datos);
        
        //Llenar el ScrollPane
        this.llenarScrollPane();
    }
    
    //Esto se encargará de llenar el comboBox para modificar el Stock
    private void llenarComboBox(ResultSet rs){
        try{
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnas = rsmd.getColumnCount(); //número de columnas
            String agregar;
            while(rs.next()){
                agregar = "";
                agregar += rs.getString(7) + " " + rs.getString(14); //El "7" es donde se ubica el campo de Descripcion
                this.datos.add(agregar + " - Stock: ");
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
    }
    
    private void llenarScrollPane(){
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        for(int i = 0; i < this.conexion.numeroCampos(); i++){
            String tituloCampo = this.conexion.obtenerCamposNombres().get(i);
            String textoALabel = tituloCampo.substring(0, 1).toUpperCase() + tituloCampo.substring(1) + ":  ";
            Label label = new Label(textoALabel);
            TextField field = new TextField();
            HBox box = new HBox(label, field);
            vbox.getChildren().add(box);
        }
        this.scrollPane.setContent(vbox);
    }

    //Método para insertar un nuevo producto
    @FXML
    private void callInsertar(ActionEvent event) {
        //Función que se encarga de validar los campos al momento de Insertar o Actualizar un producto
        boolean camposBienColocados = Validacion.validarInsertar((VBox)this.scrollPane.getContent(),this.conexion.obtenerTiposDeCampos());
        
        //Si todo está bien, se establece la conexión y se mandan los campos a la función
        //para insertar el producto
        try{
            if(camposBienColocados){
                //Hacer el INSERT del producto
                int filasIngresadas = this.conexion.insertarProducto((VBox)this.scrollPane.getContent());
                
                //Si el número de filas es mayor a 0, y en sí, si llega a esta línea, todo salió bien
                if (filasIngresadas > 0){
                    System.out.println("Registro exitoso de nuevo producto");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText("INGRESO DE PRODUCTO EXITOSO");
                    alert.setContentText("Se ha insertado exitosamente el nuevo producto en la base de datos");
                    alert.showAndWait();
                }
            }
            else{
                System.out.println("Campos mal colocados");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("CAMPOS LLENADOS INCORRECTAMENTE");
                alert.setContentText("Revise que todos los campos estén bien colocados");
                alert.showAndWait();
            }
        }
        //Si hubo un problema al insertar el producto en la base de datos, se manda la alerta correspondiente
        catch(SQLException e){
            System.out.println("El ingreso de datos no se pudo realizar");
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("INGRESO DE PRODUCTO FALLIDO");
            alert.setContentText("No se ha podido insertar el nuevo producto en la base de datos");
            alert.showAndWait();
        }
    }

    @FXML
    private void back(ActionEvent event) throws IOException{
        //Cerrar la conexión antes de salir de la ventana
        try{
         this.conexion.close();   
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
    private void comboSelect(ActionEvent event) {
        String output = comboBox.getSelectionModel().getSelectedItem().toString();
        
        this.prodNombre.setText(output.split(" ")[0]+":");
        //this.prodCantidad.setText(output.split(" ")[3].split("")[1]+" en stock");
    }
    
    
    
}
