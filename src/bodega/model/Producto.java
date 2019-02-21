/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega.model;

/**
 *
 * @author SEGUIRESA-PC
 */
public class Producto {
    
    //El orden en que se encuentran enlistados es el orden en el que MySQL tiene la tabla
    
    private int idProducto;
    private String tipo;
    private String descripcion;
    private String caracteristicas;
    private String marca;
    private String modelo;
    private int cantidad;
    private float pvp;
    private float costo;
    private String date_created;
    private String date_updated;
    
    public Producto(){
        this.tipo = "";
        this.descripcion = "";
        this.caracteristicas = "";
        this.modelo = "";
        this.cantidad = 1;
        this.pvp = 0.0f;
        this.costo = 0.0f;
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
    
}
