/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega.model;
import java.sql.*;

/**
 *
 * @author SEGUIRESA-PC
 */
public class Conexion {
    
    private String USERNAME="root";
    private String PASSWORD="mysqlseguiresapass";
    private String CONN_STRING=
            "jdbc:mysql://10.0.10.50:3306/seguistore";
    
    private Connection conn;
    
    private String dbName = "seguistore";
    
    public Conexion(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
            System.out.println("Connected!");
            
            this.conn = conn;
        }
        catch(SQLException e){
            System.out.println("ERROR - NO SE PUDO CONECTAR");
            System.err.println(e);
        }
    }
    
    //Muestra el inventario
    public ResultSet mostrarDatos(){
        ResultSet rs = null;
        try{
            Statement stmnt = (Statement) this.conn.createStatement();
            String select = "SELECT * FROM productos";
            rs = stmnt.executeQuery(select);
            /*while(rs.next()){
                System.out.println("ID:"+rs.getString(1)+" test_tablecol1:"+rs.getString(2));
            };*/
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        return rs;
    }
    
    //Inserta el producto en la base de datos.
    //Si algo sale mal, se lanza la excepción y se debe trapar dentro de la clase que llamó a este método
    public int insertarProducto(String tipo, String descripcion, String caracteristicas,
                                String marca,String modelo, String cantidad,
                                String pvp, String costo) throws SQLException{
        
            String sql = "INSERT INTO productos(tipo, descripcion, caracteristicas, "
                    + "marca, modelo, cantidad, pvp, costo ) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement  stmt = this.conn.prepareStatement(sql);
            
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

            int filasIngresadas = stmt.executeUpdate();
            return filasIngresadas;   
    }
    
    //Actualiza el producto en la base de datos.
    //Si algo sale mal, se lanza la excepción y se debe trapar dentro de la clase que llamó a este método
    public int actualizarProducto(int id, String tipo, String descripcion, String caracteristicas,
                                String marca, String modelo, String cantidad,
                                String pvp, String costo) throws SQLException{
        
        String sql = "UPDATE productos SET tipo = ?, descripcion = ?, caracteristicas = ?, "
                + "marca = ?, modelo = ?, cantidad = ?, pvp = ?, costo = ? "
                + "where idProducto = ?";
        
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        
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
        
        stmt.setInt(9, id);
        
        int filasActualizadas = stmt.executeUpdate();
        
        return filasActualizadas;
    }
    
    //Cerrar la conexión
    public void close() throws SQLException{
        this.conn.close();
    }
    
}
