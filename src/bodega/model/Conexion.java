/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega.model;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author SEGUIRESA-PC
 */
public class Conexion {
    
    private String USERNAME;
    private String PASSWORD;
    private String CONN_STRING="jdbc:mysql://10.0.10.50:3306/seguistore";
    
    private Connection conn;
    
    private String dbName = "seguistore";
    
    private LinkedList<String> listaNombresCampos = new LinkedList<String>();
    private LinkedList<String> listaTiposDeCampos =  new LinkedList<String>();
    private LinkedList<Integer> listaIdsProductos =  new LinkedList<Integer>();
    
    public Conexion(Usuario usuario){
        this.USERNAME = usuario.getUsername();
        this.PASSWORD = usuario.getPassword();
        
        System.out.println(this.USERNAME);
        System.out.println(this.PASSWORD);
        
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
            System.out.println("Connected!");
            
            this.conn = conn;
            
            this.conn.setSchema(this.dbName);
            
            //Llenar la lista de los nombres de los campos
            Statement stmnt = (Statement) this.conn.createStatement();
            String sql = "SHOW tables";
            ResultSet rs = stmnt.executeQuery(sql);
            
            //La variable campoProducto es usada para colocar la tabla de "producto" al final de la lista
            String campoProducto = "";
            //La variable campoDescripcion es usada para colocar la tabla de "descripcion" al inicio de la lista
            String campoDescripcion = "";
            while(rs.next()){
                if(!rs.getString(1).equalsIgnoreCase("productos") &&
                        !rs.getString(1).equalsIgnoreCase("descripcion") &&
                        !rs.getString(1).equalsIgnoreCase("cliente")){
                    this.listaNombresCampos.add(rs.getString(1));
                }
                else if(rs.getString(1).equalsIgnoreCase("productos")){
                    campoProducto = rs.getString(1);
                }
                else{
                    campoDescripcion = rs.getString(1);
                }
            }
            this.listaNombresCampos.addFirst(campoDescripcion);
            this.listaNombresCampos.addLast(campoProducto);
            
            //Llenar la lista de los Tipos de los Campos
            sql = "SELECT * FROM ";
            for(int i = 0; i < this.numeroCampos(); i++){
                String tablaNombre = this.listaNombresCampos.get(i);
                String query = sql + tablaNombre;
                rs = stmnt.executeQuery(query);
                rs.next();
                this.listaTiposDeCampos.add(rs.getString("Tipo"));
            }
            
            //Llenar la lista de los IDs de los productos
            sql = "SELECT * FROM productos";
            rs = stmnt.executeQuery(sql);
            while(rs.next())
                this.listaIdsProductos.add(Integer.parseInt(rs.getString(1)));
        }
        catch(SQLException e){
            System.out.println("ERROR - NO SE PUDO CONECTAR");
            e.printStackTrace();
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
            rs = stmnt.executeQuery(select);
        }
        catch(SQLException e){
            System.err.println(e);
        }  
        return rs;
    }
    
    public ResultSet mostrarClientes(int idProducto){
        ResultSet rs = null;
        try{
            Statement stmnt = (Statement) this.conn.createStatement();
            //Primero se selecciona todos los productos
            String select = "SELECT * FROM cliente where idProducto = " + idProducto;
            rs = stmnt.executeQuery(select);
        }
        catch(SQLException e){
            System.err.println(e);
        }  
        return rs;
    }
    
    /*
    Muestra sólo el nombre del producto (Descripción)
    Esta función es para eliminar productos por nombre en la ventana correspodiente
    */
    public ResultSet mostrarNombresDeProductos(){
        ResultSet rs = null;
        try{
            Statement stmnt = (Statement) this.conn.createStatement();
            String select = "SELECT * FROM productos p LEFT JOIN descripcion on p.idProducto = descripcion.idProducto";
            rs = stmnt.executeQuery(select);
            //System.out.println(rs);
        }
        catch(SQLException e){
            System.err.println(e);
        }
        return rs;
    }
    
    //Inserta el producto en la base de datos.
    //Si algo sale mal, se lanza la excepción y se debe trapar dentro de la clase que llamó a este método
    public int insertarProducto(VBox contenedor, String imagenLink) throws SQLException{
            //Primero crear un nuevo producto
            String insertProducto = "INSERT INTO productos(imagenLink) VALUES('" + imagenLink + "')";
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
    public int actualizarProducto(int id, LinkedList<Campo> listaCampos, VBox box, String imagenLink) throws SQLException{
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
        
        String sql;
        if(imagenLink != null)
            sql = "UPDATE productos set imagenLink = '" + imagenLink + "' where idProducto = " + id;
        else
            sql = "UPDATE productos set date_updated =  current_timestamp where idProducto = " + id;
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        filasActualizadas = stmt.executeUpdate();
        
        return filasActualizadas;
    }
    
    //Método para crear un nuevo campo desde la ventana de "Administrar Campos"
    public boolean crearCampo(String nombre, String tipo){
        Statement stmt = null;
        try{
            stmt = this.conn.createStatement();
            String sql = "CREATE TABLE " + nombre + " (" +
                    "id int(11) unsigned auto_increment primary key, " +
                    "idProducto int(11) unsigned, " +
                    "titulo varchar(50), " +
                    "campo varchar(50) not null, " +
                    "tipo varchar(20) default '"+ tipo + "', " +
                    "date_created timestamp default current_timestamp, " +
                    "date_updated timestamp default current_timestamp on update current_timestamp, " +
                    "foreign key(idProducto) references productos(idProducto))";
            
            //System.out.println(sql);
            
            stmt.executeUpdate(sql);
            
            /*
            Cuando se cree un nuevo campo, va a saltar errores en la conexión ya que viene a ser
            una nueva tabla vacía. Así que hay que llenarlas de campos vacíos
            */
            llenarCamposVacios(nombre,tipo);

            return true;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
   
    //Funcion que se encarga de llenar todos los registros de una nueva tabla para que no se caiga el programa
    private void llenarCamposVacios(String nombre, String tipo) throws SQLException{
        String sql = "INSERT INTO " + nombre 
                    + "(idProducto, titulo, campo) VALUES(?, ?, ?)";
        PreparedStatement stmt;
        for(int i = 0; i < this.listaIdsProductos.size(); i++){
            int id = this.listaIdsProductos.get(i);
            //System.out.println(id);
            stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1,id);
            String Nombre = nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
            stmt.setString(2,Nombre);
            if(tipo.equalsIgnoreCase("String"))
                stmt.setString(3,"");
            else if(tipo.equalsIgnoreCase("Float"))
                stmt.setFloat(3,0.f);
            else
                stmt.setInt(3, 0);

            //System.out.println(stmt.toString());

            stmt.executeUpdate();
        }
    }
    
    public boolean eliminarCampo(String nombre){
        Statement stmt = null;
        try{
            stmt = this.conn.createStatement();
            String sql = "DROP TABLE " + nombre;
            //System.out.println(sql);
            stmt.executeUpdate(sql);
            return true;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarProducto(int idProducto){
        Statement stmt = null;
        try{
            stmt = this.conn.createStatement();
            for(int i = 0; i < this.listaNombresCampos.size(); i++){
                String tabla = this.listaNombresCampos.get(i);
                String sql = "DELETE FROM " + tabla + " where idProducto = " + idProducto;
                stmt.executeUpdate(sql);
            }
            return true;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean agregarCliente(int idProducto, String nombres, String apellidos, String telefono, String fecha_venta){
        try{
            String sql = "INSERT INTO cliente(idProducto,nombres,apellidos,telefono,fecha_venta) values(?,?,?,?,?)";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, idProducto);
            stmt.setString(2, nombres);
            stmt.setString(3, apellidos);
            stmt.setString(4, telefono);
            stmt.setString(5, fecha_venta);
            stmt.executeUpdate();
            return true;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean actualizarCliente(int idCliente, String nombres, String apellidos, String telefono, LocalDate fecha_venta){
        String sql = "UPDATE cliente SET nombres = '"
                + nombres
                + "', apellidos = '"
                + apellidos
                + "', telefono = '"
                + telefono
                + "', fecha_venta = '"
                + fecha_venta
                + "' where id = "
                + idCliente;
        PreparedStatement stmt;
        try {
            stmt = this.conn.prepareStatement(sql);
            int filasActualizadas = stmt.executeUpdate();
        } catch (SQLException ex) {
            return false;
        }
            
        return true;
    }

    public Connection getConn() {
        return conn;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public String getCONN_STRING() {
        return CONN_STRING;
    }

    public String getDbName() {
        return dbName;
    }

    //Cerrar la conexión
    public void close() throws SQLException{
        this.conn.close();
    }
    
}
