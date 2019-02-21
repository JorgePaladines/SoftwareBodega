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
    private String campo;
    
    public Campo(int idCampo, int idProducto, String campo){
        this.id = idCampo;
        this.idProducto = idProducto;
        this.campo = campo;
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

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }
}
