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
    private LinkedList<String> listaTiposDeCampos =  new LinkedList<String>();
    
    public Conexion(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
            System.out.println("Connected!");
            
            this.conn = conn;
            
            //Llenar la lista de los nombres de los campos
            Statement stmnt = (Statement) this.conn.createStatement();
            String sql = "SHOW tables";
            ResultSet rs = stmnt.executeQuery(sql);
            while(rs.next()){
                this.listaNombresCampos.add(rs.getString(1));
            }
            
            //Llenar la lista de los Tipos de los Campos
            sql = "SELECT * FROM ";
            for(int i = 0; i < this.numeroCampos(); i++){
                String tablaNombre = this.listaNombresCampos.get(i);
                String query = sql + tablaNombre;
                rs = stmnt.executeQuery(query);
                rs.next();
                this.listaTiposDeCampos.add(rs.getString("Tipo"));
            }

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

    public LinkedList<String> obtenerTiposDeCampos() {
        return listaTiposDeCampos;
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
            String insertProducto = "INSERT INTO productos VALUES()";
            PreparedStatement  stmt = this.conn.prepareStatement(insertProducto);
            int filasIngresadas = stmt.executeUpdate();
            
            //Luego obtener el idProducto que fue insertado
            String ultimoID = "SELECT MAX(idProducto) FROM productos";
            Statement stmnt = (Statement) this.conn.createStatement();
            ResultSet rs = stmnt.executeQuery(ultimoID);
            rs.next();
            //Aqui está:
            int idProducto = Integer.parseInt(rs.getString(1));

            /*Ahora iterar a través de todas las tablas de los distintos campos
            y llenarlas con lo que colocó el usuario*/
            for(int i = 0; i < contenedor.getChildren().size(); i++){
                //Se obtiene cada TextField
                TextField tf = (TextField)((HBox)contenedor.getChildren().get(i)).getChildren().get(1);
                //El texto dentro de cada TextField
                String texto = tf.getText();
                
                String sql = "INSERT INTO " + this.listaNombresCampos.get(i) 
                        + "(idProducto, titulo, campo, tipo) VALUES(?, ?, ?, ?)";
                
                stmt = this.conn.prepareStatement(sql);
                //id del producto
                stmt.setInt(1, idProducto);
                //Titulo del Campo. El que aparecerá en el inventario
                stmt.setString(2, this.listaNombresCampos.get(i).substring(0,1).toUpperCase()
                                    + this.listaNombresCampos.get(i).substring(1));
                //El texto del campo
                stmt.setString(3, texto);
                //El tipo de campo que es. String, Float o Integer
                stmt.setString(4, this.listaTiposDeCampos.get(i));
                
                filasIngresadas = stmt.executeUpdate();
            }

            return filasIngresadas;   
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
