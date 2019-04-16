/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import bodega.model.Conexion;
import java.net.URL;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
        this.conn = new Conexion();
        try{
            Statement st = this.conn.getConn().createStatement();
            String sql = "SHOW GRANTS FOR CURRENT_USER";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            
            /*ResultSet privileges = null;
            DatabaseMetaData metaData = conn.getConn().getMetaData();

            privileges = metaData.getTablePrivileges(this.conn.getConn().getCatalog(),"seguistore", "descripcion");
            */
            
            String privilegios = rs.getString(1).substring(6, rs.getString(1).length());
            this.listaPrivilegios = privilegios.split(", ");

            
            
        }catch(SQLException ex){
            Logger.getLogger(LoginVentanaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void login(ActionEvent event) {
        System.out.println(tUser.getText());
        System.out.println(tPass.getText());
        System.out.println(this.listaPrivilegios);
    }
}
