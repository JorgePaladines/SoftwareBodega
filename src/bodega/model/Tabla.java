/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author SEGUIRESA-PC
 */
public class Tabla {
    private final SimpleStringProperty id;
    private final SimpleStringProperty field;
    
    public Tabla(){
        this.id = new SimpleStringProperty("");
        this.field = new SimpleStringProperty("");
    }
    
    public Tabla(String id, String field){
        this.id = new SimpleStringProperty(id);
        this.field = new SimpleStringProperty(field);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getField() {
        return field.get();
    }

    public void setField(String field) {
        this.field.set(field);
    }
    
}
