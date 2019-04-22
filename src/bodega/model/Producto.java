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
    
    private String imagenLink;
    
    /*Lista que contiene todas las columnas de la tabla del inventario
    Un campo es "Descripción", otro es "Modelo", etc
    
    Por el momento es una idea temporal, ya que no se sabe si realmente se piensa extender
    la cantidad de columnas de los reportes*/
    private ListaCampos listaCampos;

    public Producto(ResultSet rs, int size) throws SQLException{
        this.idProducto = Integer.parseInt(rs.getString(1));
        this.date_created = rs.getString("date_created");
        this.date_updated = rs.getString("date_updated");
        
        this.listaCampos = new ListaCampos();
        
        this.listaCampos.llenarLista(idProducto,size,rs);
        
        this.imagenLink = rs.getString("imagenLink");
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
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

    public String getImagenLink() {
        return imagenLink;
    }

    public void setImagenLink(String imagenLink) {
        this.imagenLink = imagenLink;
    }
}
