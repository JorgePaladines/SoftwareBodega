/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import bodega.model.Conexion;
import bodega.model.Producto;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author SEGUIRESA-PC
 */
public class EditarProductoController implements Initializable {

    @FXML
    private Label lEditar;
    @FXML
    private Button bCambiar;
    @FXML
    private Button backButton;
    
    private Producto producto;
    @FXML
    private ScrollPane scrollPane; //El ScrollPane es necesario pues puden haber muchos campos agregados, y no se sabe cuántos
    //Así que se necesita poder colocarlos todos y revisarlos sin cambiar el tamaño de la ventana
    
    private int numCampos;
    
    private VBox contenedor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    //Aquí recibe el parámetro del producto que va a ser editado
    public void initData(Producto p){
        this.producto = p;
        
        //Aquí se va a llenar el ScrollPane
        this.contenedor = new VBox(); //VBox para que contenga tantos Labels y TextFields como sea necesario
        this.contenedor.setAlignment(Pos.CENTER);
        
        //Loop que llena el VBox
        for(int i = 0; i < p.getListaCampos().getSize(); i++){
            Label label = new Label(p.getListaCampos().getListaCampos().get(i).getTitulo());
            TextField textField = new TextField();
            textField.setText((String)p.getListaCampos().getListaCampos().get(i).getCampo());
            this.contenedor.getChildren().addAll(label,textField);
        }
        
        //Colocar el VBox en el ScrollPane
        this.scrollPane.setContent(this.contenedor);
    }

    @FXML
    private void submit(ActionEvent event) {
        //Función que se encarga de validar los campos al momento de Insertar o Actualizar un producto
        boolean camposBienColocados = Validacion.validarCampos(this.producto.getListaCampos());
        
        try{
            if(camposBienColocados){
                //Se establece la conexión
                Conexion conn = new Conexion();
                
                //Hacer el UPDATE del producto
                int filasActualizadas = conn.actualizarProducto(producto.getIdProducto(), this.producto.getListaCampos().getListaCampos(), this.contenedor);
                
                //Si el número de filas es mayor a 0, y en sí, si llega a esta línea, todo salió bien
                if (filasActualizadas > 0){
                    System.out.println("Actualización exitosa del producto");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText("ACTUALIZACIÓN DE PRODUCTO EXITOSA");
                    alert.setContentText("Se ha actualizado exitosamente el producto en la base de datos");
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
        //Si hubo un problema al actualizar el producto en la base de datos, se manda la alerta correspondiente
        catch(SQLException e){
            System.out.println("La actualización de datos no se pudo realizar");
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("ACTUALIZACIÓN DE PRODUCTO FALLIDA");
            alert.setContentText("No se ha podido actualizar el producto en la base de datos");
            alert.showAndWait();
        }
        
        
    }

    @FXML
    private void back(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("CargarDatosVentana.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    
}
