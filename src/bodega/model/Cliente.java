/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author SEGUIRESA-PC
 */
public class Cliente {
    
    private int id;
    private int idProducto;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String fecha_venta;
    private String date_created;
    private String date_updated;

    public Cliente(ResultSet rs, int numColumnas) throws SQLException {
        this.id = Integer.parseInt(rs.getString(1));
        this.idProducto = Integer.parseInt(rs.getString(2));
        this.nombres = rs.getString(3);
        this.apellidos = rs.getString(4);
        this.telefono = rs.getString(5);
        this.fecha_venta = rs.getString(6);
        this.date_created = rs.getString("date_created");
        this.date_updated = rs.getString("date_updated");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(String fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }
    
    @Override
    public String toString(){
        return this.nombres + " " + this.apellidos;
    }
}
