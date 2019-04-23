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
    private BufferedImage originalImage;

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
        this.imagenLink = null;
        
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
    }

    @FXML
    private void submit(ActionEvent event) {
        //Función que se encarga de validar los campos al momento de Insertar o Actualizar un producto
        boolean camposBienColocados = Validacion.validarEditar(this.contenedor, this.conexion.obtenerTiposDeCampos());
        
        try{
            if(camposBienColocados){
                try {
                    //Se trata de guardar la nueva imagen
                    ImageIO.write(this.originalImage, "jpg", new File(this.imagenLink));
                } catch (IOException ex) {
                    Logger.getLogger(EditarProductoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
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
            else{
                System.out.println("Campos mal colocados");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("CAMPOS LLENADOS INCORRECTAMENTE");
                alert.setContentText("Revise que todos los campos estén bien colocados");
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
    }
    
    @FXML
    private void cambiarImagen(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        StringBuilder linkViejoBuilder = new StringBuilder();
        StringBuilder linkNuevoBuilder = new StringBuilder();
        
        this.originalImage = ColocadorDeImagen.colocarImagen(chooser, this.originalImage, this.imagen, this.producto, linkNuevoBuilder, linkViejoBuilder, this.root, this.conexion);

        //String linkViejo = chooser.showOpenDialog(root.getScene().getWindow()).toString();
        
        this.imagenLink = linkNuevoBuilder.toString();
        
    }

    @FXML
    private void back(ActionEvent event) throws IOException{
        //Probar que se pueda cerrar la conexión
        try{
            this.conexion.close();
        }
        catch(SQLException e){
            System.err.println(e);
            System.out.println("ERROR AL CERRAR LA CONEXIÓN");
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CargarDatosVentana.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene( (Parent) loader.load());
        stage.setScene(scene);
        
        CargarDatosVentanaController controller = loader.<CargarDatosVentanaController>getController();
        controller.initData(usuario);
    }
}
