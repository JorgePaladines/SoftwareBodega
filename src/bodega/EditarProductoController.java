/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import bodega.model.ColocadorDeImagen;
import java.io.IOException;
import java.awt.Desktop;
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
import bodega.model.Usuario;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

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
    
    private Conexion conexion;
    @FXML
    private ImageView imagen;
    @FXML
    private Button botonImagen;
    @FXML
    private AnchorPane root;
    
    private Usuario usuario;
    
    private String imagenLink;
    private BufferedImage aColocarImagen;
    @FXML
    private Label url;
    
    private boolean cambioDeImagen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    //Aquí recibe el parámetro del producto que va a ser editado
    public void initData(Usuario usuario, Producto p){
        this.usuario = usuario;
        this.conexion = new Conexion(usuario);
        this.url.setText(p.getImagenLink());
        this.imagenLink = null;
        this.cambioDeImagen = false;
        
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
        
        //Colocar el VBox en un StackPane para que se centre
        StackPane paneHolder = new StackPane(this.contenedor);
        //Colocar el StackPane en el ScrollPane
        this.scrollPane.setContent(paneHolder);
        //Esta línea hace que se centre todo
        paneHolder.minWidthProperty().bind(Bindings.createDoubleBinding(() -> 
        this.scrollPane.getViewportBounds().getWidth(), this.scrollPane.viewportBoundsProperty()));
        
        if(this.producto.getImagenLink() != null){
            try {
                this.imagen.setImage(new Image(new FileInputStream(this.producto.getImagenLink())));
            } catch (FileNotFoundException ex) {
                System.out.println("Habia una imagen en el producto pero fue borrada de la carpeta");
            }
        }
        
        try{
            this.conexion.close();
        }
        catch(SQLException e){
            System.err.println(e);
            System.out.println("ERROR AL CERRAR LA CONEXIÓN");
        }
        
    }

    @FXML
    private void submit(ActionEvent event) {
        this.conexion = new Conexion(usuario);
        
        //Función que se encarga de validar los campos al momento de Insertar o Actualizar un producto
        boolean camposBienColocados = Validacion.validarEditar(this.contenedor, this.conexion.obtenerTiposDeCampos());
        
        if(camposBienColocados){
            try{
                File file = new File("");
                boolean seBorraImagen = false;
                //Se elimina la imagen antigua, sólo si hay una
                if(this.url != null){
                    if(this.url.getText() != null){
                        file = new File(this.url.getText());
                        //Si se borra bien la imagen, seBorraImagen será true
                        if(file.delete())
                            seBorraImagen = true;
                    }
                    
                    //Esto es porque a veces url dice null y a veces ES null
                    if(this.url.getText().equalsIgnoreCase("null")){
                        this.url = null;
                    }
                }
                
                //System.out.println(seBorraImagen);
                //System.out.println(this.url.getText().equalsIgnoreCase("null"));
                //System.out.println(!this.cambioDeImagen);
                
                //Si hubo una imagen que debía de borrarse, pero no se borró, no se actualiza nada
                //Si no tenía que borrarse ninguna imagen, sí se actualiza, y no hay cambio de imagen
                if((seBorraImagen || this.url == null) || !this.cambioDeImagen){
                    
                    if(this.aColocarImagen != null){
                        //Se trata de guardar la nueva imagen
                        ImageIO.write(this.aColocarImagen, "jpg", new File(this.imagenLink));
                    }
                    
                    //Acá se guardará el url de la ubicacación de la imagen.
                    //Este servirá como un url viejo para borrar la imagen después de cambiarse
                    if(this.url != null && this.imagenLink != null)
                        this.url.setText(this.imagenLink);
                    
                    try{
                        //Hacer el UPDATE del producto
                        int filasActualizadas = this.conexion.actualizarProducto(producto.getIdProducto(),
                                                                                this.producto.getListaCampos().getListaCampos(),
                                                                                this.contenedor,
                                                                                this.imagenLink);

                        //Si el número de filas es mayor a 0, y en sí, si llega a esta línea, todo salió bien
                        if (filasActualizadas > 0){
                            if(producto != null)
                                producto.setImagenLink(this.imagenLink);

                            System.out.println("Actualización exitosa del producto");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("");
                            alert.setHeaderText("ACTUALIZACIÓN DE PRODUCTO EXITOSA");
                            alert.setContentText("Se ha actualizado exitosamente el producto en la base de datos");
                            alert.showAndWait();
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

                }//Sólo si se borra la antigua imagen, se procede a guardar la nueva
                else{ 
                    System.out.println("Failed to delete the file");
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("");
                    alert.setHeaderText("NO SE PUDO CAMBIAR LA IMAGEN");
                    alert.setContentText("La imagen antigua está siendo usada por algún programa. Intente de nuevo");
                    alert.showAndWait();
                }

            }//Si no se puede guardar la imagen, no se debe de tocar la base de datos
            catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("");
                alert.setHeaderText("NO SE PUDO GUARDAR LA IMAGEN");
                alert.setContentText("");
                alert.showAndWait();
                Logger.getLogger(EditarProductoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }//Si los campos están mal colocados, no se hace nada
        else{
            System.out.println("Campos mal colocados");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("CAMPOS LLENADOS INCORRECTAMENTE");
            alert.setContentText("Revise que todos los campos estén bien colocados");
            alert.showAndWait();
        }
        
        //Probar que se pueda cerrar la conexión
        //Se hace aquí para minimizar las veces que no se borra la antigua imagen por estar abierta en Java
        try{
            this.conexion.close();
        }
        catch(SQLException e){
            System.err.println(e);
            System.out.println("ERROR AL CERRAR LA CONEXIÓN");
        }
        
    }
    
    @FXML
    private void cambiarImagen(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        StringBuilder urlNuevoBuilder = new StringBuilder();
        
        this.aColocarImagen = ColocadorDeImagen.colocarImagen(chooser, this.aColocarImagen, this.imagen, this.producto, urlNuevoBuilder, this.root, this.conexion);
        
        this.imagenLink = urlNuevoBuilder.toString();
        
        //System.out.println(this.url.getText());
        //System.out.println(this.imagenLink);
        
        this.cambioDeImagen = true;
    }

    @FXML
    private void back(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CargarDatosVentana.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene( (Parent) loader.load());
        stage.setScene(scene);
        
        CargarDatosVentanaController controller = loader.<CargarDatosVentanaController>getController();
        controller.initData(usuario);
    }
}
