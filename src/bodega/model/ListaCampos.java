/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega.model;

import java.util.LinkedList;

/**
 * Esta clase contiene la lista de todos los campos que tendrá cada producto en el inventario
 * @author SEGUIRESA-PC
 */
public class ListaCampos {
    
    //Va a tener la lista de los campos de cada producto
    //Esta clase debe contenerla cada producto pues se encarga de manejar sus columnas
    private LinkedList<Campo> listaCampos;
    private int size;
    
    public ListaCampos(){
        this.listaCampos = new LinkedList<Campo>();
        this.size = 0;
    }

    public LinkedList<Campo> getListaCampos() {
        return listaCampos;
    }

    public void setListaCampos(LinkedList<Campo> listaCampos) {
        this.listaCampos = listaCampos;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public void llenarLista(int idProducto){

    }
}
