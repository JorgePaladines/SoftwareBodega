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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.conn = new Conexion();
        try{
            ResultSet privileges = null;
            DatabaseMetaData metaData = conn.getConn().getMetaData();

            privileges = metaData.getTablePrivileges(this.conn.getConn().getCatalog(),"seguistore", "descripcion");
            
            System.out.println(privileges.first());
            System.out.println(privileges.next());
            
            /*while(privileges.next()){
                 String tableName = privileges.getString("TABLE_NAME");
                 System.out.println("table name:" + tableName);
            }*/
            
        }catch(SQLException ex){
            Logger.getLogger(LoginVentanaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
