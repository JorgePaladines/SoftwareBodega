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

/**
 * FXML Controller class
 *
 * @author SEGUIRESA-PC
 */
public class InsertarProductoController implements Initializable {

    @FXML
    private Label tituloNuevoProd;
    @FXML
    private Label ldesc;
    @FXML
    private Label lcar;
    @FXML
    private Label lmarc;
    @FXML
    private Label ltipo;
    @FXML
    private Label lmodel;
    @FXML
    private Label lcant;
    @FXML
    private Label lpvp;
    @FXML
    private Label lcost;
    @FXML
    private TextField tdesc;
    @FXML
    private TextField tcar;
    @FXML
    private TextField tmarc;
    @FXML
    private TextField ttipo;
    @FXML
    private TextField tmodel;
    @FXML
    private TextField tcant;
    @FXML
    private TextField tpvp;
    @FXML
    private TextField tcost;
    @FXML
    private Button bInsertar;
    @FXML
    private Button backButton;
    @FXML
    private Label tituloModificar;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private TextField modifCantText;
    @FXML
    private Text nota;
    
    private Conexion conexion;
    private ObservableList<String> datos;
    @FXML
    private Label prodNombre;
    @FXML
    private Label prodCantidad;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.conexion = new Conexion();
        ResultSet rs = this.conexion.mostrarDatos();
        this.datos = FXCollections.observableArrayList();
        
        try{
            while(rs.next()){
                this.datos.add(rs.getString(2)+" - "+rs.getString(3)+" ["+rs.getString("cantidad")+"]");
            };
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        this.comboBox.setItems(this.datos);
    }

    //Método para insertar un nuevo producto
    @FXML
    private void callInsertar(ActionEvent event) {
        //Función que se encarga de validar los campos al momento de Insertar o Actualizar un producto
        boolean camposBienColocados = Validacion.validarProducto(ttipo.getText(), tdesc.getText(),
                                        tcar.getText(), tmodel.getText(), tcant.getText(),
                                        tpvp.getText(), tcost.getText());
        
        //Si todo está bien, se establece la conexión y se mandan los campos a la función
        //para insertar el producto
        try{
            if(camposBienColocados){
                Conexion conn = new Conexion();
                
                //Hacer el INSERT del producto
                int filasIngresadas = conn.insertarProducto(ttipo.getText(), tdesc.getText(), tcar.getText(),
                                    tmarc.getText(), tmodel.getText(), tcant.getText(),
                                    tpvp.getText(), tcost.getText());
                
                //Si el número de filas es mayor a 0, y en sí, si llega a esta línea, todo salió bien
                if (filasIngresadas > 0){
                    System.out.println("Registro exitoso de nuevo producto");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText("INGRESO DE PRODUCTO EXITOSO");
                    alert.setContentText("Se ha insertado exitosamente el nuevo producto en la base de datos");
                    alert.showAndWait();
                }
                
                //Probar que se pueda cerrar la conexión
                try{
                    conn.close();
                }
                catch(SQLException e){
                    System.err.println(e);
                    System.out.println("ERROR AL CERRAR LA CONEXIÓN");
                }
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
        Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void comboSelect(ActionEvent event) {
        String output = comboBox.getSelectionModel().getSelectedItem().toString();
        
        this.prodNombre.setText(output.split(" ")[2]+":");
        this.prodCantidad.setText(output.split(" ")[3].split("")[1]+" en stock");
    }
    
    
    
}
