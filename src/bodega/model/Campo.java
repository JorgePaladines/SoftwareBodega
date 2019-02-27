/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega.model;

/**
 * La idea es que se pueda extender el producto y colocarle más campos/columnas
 * Esta es la clase general del campo que contiene su id de la DB, el id del producto a la que
 * pertenece, y su texto o número
 * @author SEGUIRESA-PC
 */
public class Campo {
    
    private int id;
    private int idProducto;
    private String titulo;
    private Object campo;
    private String tipo;
    
    public Campo(int idCampo, int idProducto, String titulo, Object campo, String tipo){
        this.id = idCampo;
        this.titulo = titulo;
        this.idProducto = idProducto;
        this.campo = campo;
        this.tipo = tipo;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Object getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
