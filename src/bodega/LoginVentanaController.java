/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import bodega.model.Conexion;
import bodega.model.Usuario;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

/**
 * FXML Controller class
 *
 * @author SEGUIRESA-PC
 */
public class LoginVentanaController implements Initializable {

    @FXML
    private Label lLogin;
    @FXML
    private Label lUsuario;
    @FXML
    private Label lContrasena;
    
    private Conexion conn;
    @FXML
    private TextField tUser;
    @FXML
    private TextField tPass;
    @FXML
    private Button bIngresar;
    
    private String[] listaPrivilegios;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void login(ActionEvent event) {
        try{
            Connection conexion = DriverManager.getConnection("jdbc:mysql://10.0.10.50:3306/seguistore", tUser.getText(), tPass.getText());
            
            System.out.println("Conecction realizada con éxito");
            
            Statement st = conexion.createStatement();
            String sql = "SHOW GRANTS FOR CURRENT_USER";
            ResultSet rs = st.executeQuery(sql);
            rs.next();

            String privilegios = rs.getString(1).substring(6, rs.getString(1).length());
            this.listaPrivilegios = privilegios.split(", ");
            
            Usuario usuario = new Usuario(tUser.getText(),tPass.getText(),this.listaPrivilegios);
            
            //Pasar el Usuario por parámetro al controlador de InicioController y el resto

            //Cargar el FXML Loader, como tipo de objecto FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Inicio.fxml"));


            //Colocar la escena como en los demás casos
            Stage stage = (Stage) bIngresar.getScene().getWindow();
            Scene scene = new Scene( (Parent) loader.load());
            
            InicioController controller = loader.<InicioController>getController();
            controller.setUser(usuario);
            
            stage.setScene(scene);
            
            
        }
        catch(IOException e){
            System.out.println("Hubo un problema al cargar la ventana");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("PROBLEMAS EN LA CARGA DEL PROGRAMA");
            alert.setContentText("Ocurrió un error al momento de tratar de cargar la ventana. Intente de nuevo");
            alert.showAndWait();
        }
        catch(SQLException e){
            System.out.println("El usuario o la contraseña es incorrecto");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("USUARIO O CONTRASEÑA INCORRECTOS");
            alert.setContentText("Revise que las credenciales estés bien colocadas");
            alert.showAndWait();
        }
    }
}
