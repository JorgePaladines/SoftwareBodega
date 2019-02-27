/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import bodega.model.Campo;
import bodega.model.ListaCampos;
import java.util.LinkedList;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Clase que se encarga de validar los campos al momento de Insertar o Actualizar un producto
 * @author SEGUIRESA-PC
 */
public class Validacion {
    
    //Función que se encarga de validar los campos al momento de Insertar o Actualizar un producto
    public static boolean validarProducto(String tipo, String descripcion, String caracteristicas,
                                        String modelo, String cantidad, String pvp,
                                        String costo){
        
        //Este flag se asegurará de que todos los campos estés bien colocados.
        //Si no es así, se hará false y no se establecerá la conexión con la base de datos
        boolean camposBienColocados = true;
        
        //Primero asegurarse de que los campos que no deben ser null, no se puedan dejar vacíos
        if(tipo.equalsIgnoreCase("") || descripcion.equalsIgnoreCase("") ||
            caracteristicas.equalsIgnoreCase("") || modelo.equalsIgnoreCase("")){
            
            System.out.println("Hacen falta campos en el registro del producto");
            
            //Se arrojará una alerta en caso de que esto ocurra
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("ALERTA");
            alert.setContentText("Hacen falta campos en el registro del producto");
            alert.showAndWait();
            
            camposBienColocados = false;
        }
        
        //Luego se asegura que los números sean números y que los float sean float
        try{
            //Si se logra convertir, no pasa nada
            Object parseTest = Integer.parseInt(cantidad);
            parseTest = Float.parseFloat(pvp);
            parseTest = Float.parseFloat(costo);
        }
        catch(NumberFormatException e){
            //Si hay un error, hacer que flag sea false y lanzar otra alerta
            System.err.println(e);
            System.out.println("Los números no fueron bien ingresados");
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("ALERTA");
            alert.setContentText("Los números no fueron bien ingresados");
            alert.showAndWait();
            
            camposBienColocados = false;
        }
        
        return camposBienColocados;
    }
    
    //Valida en la actualización de un producto
    public static boolean validarCampos(VBox contenedor, LinkedList<Campo> campos){
        //Este flag se asegurará de que todos los campos estés bien colocados.
        //Si no es así, se hará false y no se establecerá la conexión con la base de datos
        boolean camposBienColocados = true;
        
        int indexCampo = 0; //Esto servirá para que el programa recorra la lista de campos al mismo tiempo
                      //que recorre la lista de Nodos del VBox
        //Se lo necesita para revisar tabla por tabla qué tipo es.
        //Y si es un número, se lo valida como tal
        for(int i = 1; i < contenedor.getChildren().size(); i = i + 2){
            //Si está vacío el campo, entonces retorna FALSE
            if(((TextField)contenedor.getChildren().get(i)).getText().equals("")){
                camposBienColocados = false;
                break;
            }
            //Si está mal escrito el campo de un Integer, entonces retorna FALSE
            if(campos.get(indexCampo).getTipo().equals("Integer")){
                //Se lo intenta convertir
                try{
                    Integer.parseInt((String) campos.get(indexCampo).getCampo());
                    //Si no hay problema el for prosigue
                }
                //Pero si sale mal entonces se avisa que el campo está mal colocado
                catch(NumberFormatException e){
                    System.out.println("Campo de número mal llenado");
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText("CAMPO MAL LLENADO");
                    alert.setContentText("Uno de los campos no ha sido llenado con datos erróneos");
                    alert.showAndWait();
                    
                    camposBienColocados = false;
                    break;
                }
            }
            
            indexCampo++;
        }
        
        return camposBienColocados;
    }
    
    //Valida en la inserción de un nuevo producto
    public static boolean validarTextFields(VBox contenedor){
        //Este flag se asegurará de que todos los campos estés bien colocados.
        //Si no es así, se hará false y no se establecerá la conexión con la base de datos
        boolean camposBienColocados = true;
        
        //Lo que contiene el VBox deberían ser HBox's que contienen el label y el textfield
        for(int i = 0; i < contenedor.getChildren().size(); i++){
            //Cada HBox que contiene el VBox
            HBox hboxInterno = (HBox) contenedor.getChildren().get(i);
            //Cada TextField de los HBox
            TextField tf = (TextField)hboxInterno.getChildren().get(1);
            if(tf.getText().equals("")){
                camposBienColocados = false;
                break;
            }
            
        }
        
        return camposBienColocados;
    }
    
}
