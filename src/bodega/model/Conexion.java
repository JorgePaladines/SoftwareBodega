/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega.model;
import java.sql.*;
import java.util.LinkedList;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author SEGUIRESA-PC
 */
public class Conexion {
    
    private String USERNAME="root";
    private String PASSWORD="mysqlseguiresapass";
    private String CONN_STRING="jdbc:mysql://10.0.10.50:3306/seguistore";
    
    private Connection conn;
    
    private String dbName = "seguistore";
    
    private LinkedList<String> listaNombresCampos = new LinkedList<String>();
    
    public Conexion(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
            System.out.println("Connected!");
            
            this.conn = conn;
            
            Statement stmnt = (Statement) this.conn.createStatement();
            String show = "SHOW tables";
            ResultSet rs = stmnt.executeQuery(show);
            while(rs.next()){
                this.listaNombresCampos.add(rs.getString(1));
            }
            
            /*
            System.out.println("");
            System.out.println("TABLAS:");
            
            for(int i = 0; i < this.listaNombresCampos.size(); i++){
                System.out.println(this.listaNombresCampos.get(i));
            }
            */
        }
        catch(SQLException e){
            System.out.println("ERROR - NO SE PUDO CONECTAR");
            System.err.println(e);
        }
    }
    
    //Retorna el número de los campos, sin considerar la tabla Producto como un campo
    public int numeroCampos(){
        return this.listaNombresCampos.size()-1;
    }
    
    public LinkedList<String> obtenerCamposNombres(){
        return this.listaNombresCampos;
    }
    
    //Muestra el inventario
    public ResultSet mostrarDatos(){
        ResultSet rs = null;
        try{
            Statement stmnt = (Statement) this.conn.createStatement();
            //Primero se selecciona todos los productos
            String select = "SELECT * FROM productos p";
            //Ahora hay que hacerle left join a cada tipo de campo que existe
            for(int i = 0; i < this.listaNombresCampos.size()-1; i++){
                select += " LEFT JOIN "+this.listaNombresCampos.get(i)
                        + " on p.idProducto = "+this.listaNombresCampos.get(i)+".idProducto";
            }
            //System.out.println(select);
            rs = stmnt.executeQuery(select);

        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        return rs;
    }
    
    //Inserta el producto en la base de datos.
    //Si algo sale mal, se lanza la excepción y se debe trapar dentro de la clase que llamó a este método
    public int insertarProducto(VBox contenedor) throws SQLException{
        
            //Primero crear un nuevo producto
            /*String insertProducto = "INSERT INTO productos VALUES()";
            PreparedStatement  stmt = this.conn.prepareStatement(insertProducto);
            int filasIngresadas = stmt.executeUpdate();
            
            //Luego obtener el idProducto que fue insertado
            String ultimoID = "SELECT MAX(idProducto) FROM productos";
            Statement stmnt = (Statement) this.conn.createStatement();
            ResultSet rs = stmnt.executeQuery(ultimoID);
            rs.next();
            //Aqui está:
            int idProducto = Integer.parseInt(rs.getString(1));*/

            /*Ahora iterar a través de todas las tablas de los distintos campos
            y llenarlas con lo que colocó el usuario*/
            
            for(int i = 0; i < contenedor.getChildren().size(); i++){
                //Se obtiene cada TextField
                TextField tf = (TextField)((HBox)contenedor.getChildren().get(i)).getChildren().get(1);
                //El texto dentro de cada TextField
                String texto = tf.getText();
                System.out.println(texto);
            }
            
            /*
            String sql = "INSERT INTO productos(tipo, descripcion, caracteristicas, "
                    + "marca, modelo, cantidad, pvp, costo ) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            stmt.setString(1, tipo);
            stmt.setString(2, descripcion);
            stmt.setString(3, caracteristicas);
            stmt.setString(4, marca);
            stmt.setString(5, modelo);

            if(cantidad.equalsIgnoreCase("")) stmt.setInt(6, 1);
            else stmt.setInt(6, Integer.parseInt(cantidad));

            if(pvp.equalsIgnoreCase("")) stmt.setFloat(7, 0.0f);
            else stmt.setFloat(7, Float.parseFloat(pvp));

            if(costo.equalsIgnoreCase("")) stmt.setFloat(8, 0.0f);
            else stmt.setFloat(8, Float.parseFloat(costo));

            filasIngresadas = stmt.executeUpdate();*/
            return 1;   
    }
    
    //Actualiza el producto en la base de datos.
    //Si algo sale mal, se lanza la excepción y se debe trapar dentro de la clase que llamó a este método
    public int actualizarProducto(int id, LinkedList<Campo> listaCampos, VBox box) throws SQLException{
        int filasActualizadas = 0;
        int textoATomar = 1;
        
        for(Campo c: listaCampos){
            String sql = "UPDATE "
                    + c.getTitulo().toLowerCase()
                    + " SET campo = '"
                    + ((TextField)box.getChildren().get(textoATomar)).getText()
                    + "' where id = "
                    + c.getId();
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            filasActualizadas = stmt.executeUpdate();
            
            textoATomar = textoATomar + 2;
        }
        
        String sql = "UPDATE productos set date_updated =  current_timestamp where idProducto = " + id;
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        filasActualizadas = stmt.executeUpdate();
        
        return filasActualizadas;
        
    }
    
    //Cerrar la conexión
    public void close() throws SQLException{
        this.conn.close();
    }
    
}
