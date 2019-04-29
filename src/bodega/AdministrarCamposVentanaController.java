package bodega;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bodega.model.Conexion;
import bodega.model.Usuario;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SEGUIRESA-PC
 */
public class AdministrarCamposVentanaController implements Initializable {

    @FXML
    private Button backButton;
    
    private Conexion conexion;
    @FXML
    private SplitPane splitPane;
    @FXML
    private VBox tipoChoiceBox;
    @FXML
    private Label lTipo;
    @FXML
    private ChoiceBox<String> choiceTipo;
    @FXML
    private VBox nombreTextBox;
    @FXML
    private Label lNombre;
    @FXML
    private TextField tNombre;
    @FXML
    private Button bCrear;
    @FXML
    private Button bEliminar;
    @FXML
    private ChoiceBox<String> choiceCampo;
    
    private Usuario usuario;
    
    private boolean CREATE = false;
    private boolean DROP = false;
    @FXML
    private Label labelCREARNUEVOCAMPO;
    @FXML
    private Label labelENELINVENTARIO;
    @FXML
    private Label labelELIMINARUNCAMPO;
    @FXML
    private Label labelDELINVENTARIO;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*this.conexion = new Conexion(this.usuario);
        //Se llenan los choiceboxes
        //Primero la lista de tipos de campos: String, Integer o Float
        ObservableList<String> tiposDeCampos = FXCollections.observableArrayList();
        tiposDeCampos.addAll("String","Integer","Float");
        this.choiceTipo.setItems(tiposDeCampos);
        
        //Luego la lista de campos existentes. Estos pueden ser seleccionados para ser eliminados
        ObservableList<String> nombresCampos = FXCollections.observableArrayList();
        nombresCampos.setAll(this.conexion.obtenerCamposNombres());
        nombresCampos.remove(nombresCampos.size()-1); //Se le resta 1 puesto que la lista arrojada contiene
        //la palabra "productos", lo que no es un campo a eliminar
        nombresCampos.remove("descripcion");//También se saca la dexcripción ya que este es el título del producto
        //Si se elimina, no se podrá identificar los productos al momento de eliminarlos
        this.choiceCampo.setItems(nombresCampos);*/
    }
    
    public void initData(Usuario usuario){
        this.usuario = usuario;
        
        //Colocar permisos de CREATE y DROP
        for(int i = 0; i < 6; i++){
            if(this.usuario.getPrivilegios()[i].equalsIgnoreCase("CREATE")){
                this.CREATE = true;
            }
            if(this.usuario.getPrivilegios()[i].equalsIgnoreCase("DROP")){
                this.DROP = true;
            }
        }
        
        this.conexion = new Conexion(this.usuario);
        //Se llenan los choiceboxes
        //Primero la lista de tipos de campos: String, Integer o Float
        ObservableList<String> tiposDeCampos = FXCollections.observableArrayList();
        tiposDeCampos.addAll("String","Integer","Float");
        this.choiceTipo.setItems(tiposDeCampos);
        
        //Luego la lista de campos existentes. Estos pueden ser seleccionados para ser eliminados
        ObservableList<String> nombresCampos = FXCollections.observableArrayList();
        nombresCampos.setAll(this.conexion.obtenerCamposNombres());
        nombresCampos.remove(nombresCampos.size()-1); //Se le resta 1 puesto que la lista arrojada contiene
        //la palabra "productos", lo que no es un campo a eliminar
        nombresCampos.remove("descripcion");//También se saca la dexcripción ya que este es el título del producto
        //Si se elimina, no se podrá identificar los productos al momento de eliminarlos
        this.choiceCampo.setItems(nombresCampos);
        
        if(this.CREATE){
            this.labelCREARNUEVOCAMPO.setDisable(false);
            this.labelENELINVENTARIO.setDisable(false);
            this.lNombre.setDisable(false);
            this.tNombre.setDisable(false);
            this.lTipo.setDisable(false);
            this.choiceTipo.setDisable(false);
            this.bCrear.setDisable(false);
        }
        
        if(this.DROP){
            this.labelELIMINARUNCAMPO.setDisable(false);
            this.labelDELINVENTARIO.setDisable(false);
            this.choiceCampo.setDisable(false);
            this.bEliminar.setDisable(false);
        }
    }

    @FXML
    private void back(ActionEvent event) throws IOException{
        try{this.conexion.close();}
        catch(SQLException e){System.err.println(e);}
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Inicio.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene( (Parent) loader.load());
        stage.setScene(scene);
        
        InicioController controller = loader.<InicioController>getController();
        controller.setUser(usuario);
    }

    @FXML
    private void crearCampo(ActionEvent event) {
        String nombre = this.tNombre.getText();
        String tipo = this.choiceTipo.getValue();
        boolean datosBienColocados = true;
        boolean creacionExitosa = true;
        
        if(nombre.equalsIgnoreCase("")){
            System.out.println("Nombre vacío");
            datosBienColocados = false;
        }
        if(tipo == null){
            System.out.println("Tipo vacío");
            datosBienColocados = false;
        }
        if(datosBienColocados){
            //System.out.println(nombre);
            //System.out.println(tipo);
            nombre = nombre.toLowerCase();
            creacionExitosa = this.conexion.crearCampo(nombre, tipo);
        }
        else{
            creacionExitosa = false;
            //Mandar alerta
            System.out.println("Datos mal colocados");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("LOS DATOS DEL CAMPO A CREAR NO ESTÁN BIEN COLOCADOS");
            alert.setContentText("Por favor asegurarse de que tenga un nombre y halla escogido un tipo");
            alert.showAndWait();
        }
        
        if(creacionExitosa){
            //Si sale bien, mandar la alerta
            System.out.println("Creación de nuevo campo exitosa");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("SE HA CREADO EL NUEVO CAMPO");
            alert.setContentText("El campo se ha creado exitosamente");
            alert.showAndWait();
        }
        else{
            //Si sale mal, mandar la alerta
            System.out.println("Creación de nuevo campo fallida");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("NO SE HA PODIDO CREAR EL NUEVO CAMPO");
            alert.setContentText("Hubo una falla en la creación del campo");
            alert.showAndWait();
        }
    }

    @FXML
    private void eliminarCampo(ActionEvent event) {
        String nombre = this.choiceCampo.getValue();
        if(nombre != null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("VA A ELIMINAR UN CAMPO DE LOS PRODUCTOS DEL INVENTARIO");
            alert.setContentText("¿Está seguro de que desea eliminar este campo de los productos del inventario? "
                    + "No se podrá recuperar una vez realizado");
            ButtonType yesButton = new ButtonType("Aceptar", ButtonBar.ButtonData.NO);
            ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesButton, cancelButton);
            alert.showAndWait().ifPresent(type -> {
                    if(type == yesButton){
                        //Ahora sí llamar a la función que lo elimina
                        boolean exitoso = this.conexion.eliminarCampo(nombre);
                        
                        Alert al = new Alert(Alert.AlertType.INFORMATION);
                        if(exitoso){
                            //Si todo sale bien, también hay que sacar el nombre del campo de la lista
                            this.choiceCampo.getItems().remove(this.choiceCampo.getItems().indexOf(nombre));
                            this.choiceCampo.setValue(null);
                            
                            //Lanzar la alerta
                            System.out.println("Eliminación de campo exitosa");
                            
                            al.setTitle("");
                            al.setHeaderText("SE HA ELIMINADO EL CAMPO");
                            al.setContentText("El campo se ha eliminado exitosamente");
                            al.showAndWait();
                        }
                        else{
                            //Lanzar alerta
                            al.setAlertType(Alert.AlertType.ERROR);
                            System.out.println("Eliminación de campo fallida");
                            al.setTitle("");
                            al.setHeaderText("NO SE HA PODIDO ELIMINAR EL CAMPO");
                            al.setContentText("Hubo una falla en la eliminación del campo");
                            al.showAndWait();
                        }
                    }
                    else{
                        System.out.println("Cancelado");
                    }
            });
        }
        else{
            System.out.println("Debe de escoger un campo a eliminar");
        }
    }
    
}
