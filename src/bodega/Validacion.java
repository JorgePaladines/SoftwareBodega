/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega;

import javafx.scene.control.Alert;

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
    
}
