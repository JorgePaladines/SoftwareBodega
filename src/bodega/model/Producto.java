/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author SEGUIRESA-PC
 */
public class Producto {
    
    //El orden en que se encuentran enlistados es el orden en el que MySQL tiene la tabla
    
    private int idProducto;
    private String date_created;
    private String date_updated;
    
    /*Lista que contiene todas las columnas de la tabla del inventario
    Un campo es "Descripción", otro es "Modelo", etc
    
    Por el momento es una idea temporal, ya que no se sabe si realmente se piensa extender
    la cantidad de columnas de los reportes*/
    private ListaCampos listaCampos;
    
    private String tipo;
    private String descripcion;
    private String caracteristicas;
    private String marca;
    private String modelo;
    private int cantidad;
    private float pvp;
    private float costo;
    
    public Producto(){
        //this.listaCampos.llenarLista(this.idProducto);
        
        this.tipo = "";
        this.descripcion = "";
        this.caracteristicas = "";
        this.modelo = "";
        this.cantidad = 1;
        this.pvp = 0.0f;
        this.costo = 0.0f;
    }
    
    public Producto(ResultSet rs, int size) throws SQLException{
        this.idProducto = Integer.parseInt(rs.getString(1));
        this.date_created = rs.getString("date_created");
        this.date_updated = rs.getString("date_updated");
        
        this.listaCampos = new ListaCampos();
        
        this.listaCampos.llenarLista(idProducto,size,rs);
    }
    
    public Producto(String tipo, String descripcion, String caracteristicas,
                    String marca, int cantidad, float pvp, float costo){
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.caracteristicas = caracteristicas;
        this.cantidad = cantidad;
        this.pvp = pvp;
        this.costo = costo;
    }
    
    public Producto(int idProducto, String tipo, String descripcion, String caracteristicas,
                    String marca, String modelo, int cantidad, float pvp, float costo, String date_created,
                    String date_updated){
        this.idProducto = idProducto;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.caracteristicas = caracteristicas;
        this.marca = marca;
        this.modelo = modelo;
        this.cantidad = cantidad;
        this.pvp = pvp;
        this.costo = costo;
        this.date_created = date_created;
        this.date_updated = date_updated;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPvp() {
        return pvp;
    }

    public void setPvp(float pvp) {
        this.pvp = pvp;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public ListaCampos getListaCampos() {
        return listaCampos;
    }

    public void setListaCampos(ListaCampos listaCampos) {
        this.listaCampos = listaCampos;
    }

    //Obtener un campo específico de la lista de campos
    public Campo getCampo(int i) {
        return this.listaCampos.getListaCampos().get(i);
    }

    //Obtener un titulo específico de la lista de campos
    public String getTituloCampo(int i) {
        return this.listaCampos.getListaCampos().get(i).getTitulo();
    }
}
