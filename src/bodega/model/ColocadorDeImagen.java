/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bodega.model;

import bodega.EditarProductoController;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 *
 * @author SEGUIRESA-PC
 */
public class ColocadorDeImagen {
    
    public static BufferedImage colocarImagen(FileChooser chooser, BufferedImage originalImage, ImageView imagen, Producto producto, StringBuilder imagenLink, AnchorPane root, Conexion conn){
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(root.getScene().getWindow());
        if(file != null){
            try {
                imagen.setImage(new Image(new FileInputStream(file)));
                originalImage = ImageIO.read(new File(file.toString()));
                //Con lo siguiente se saca el nombre de la imagen (se quita el directorio y la extensión)
                String[] direccionImagenLista = file.toString().split("\\\\");
                String nombreImagen = direccionImagenLista[direccionImagenLista.length-1].split("\\.")[0];
                
                //Acá se encuentra el url nuevo
                imagenLink.delete(0, imagenLink.length()).append(".\\\\src\\\\imagenes\\\\" + nombreImagen + ".jpg");
                
                

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                
                System.out.println("No se encontró la imagen");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("ERROR EN LA LECTURA DE LA IMAGEN");
                alert.setContentText("No se pudo detectar una imagen válida");
                alert.showAndWait();
                
                return null;
            } catch (IOException ex) {
                ex.printStackTrace();
                
                System.out.println("Error al procesar datos");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("Error durante la escritura/lectura del archivo");
                alert.setContentText("No se pudo colocar/leer la imagen seleccionada.");
                alert.showAndWait();
                
                return null;
            }
            catch(IllegalArgumentException ex){
                ex.printStackTrace();
                
                System.out.println("La imagen no es válida");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("ARCHIVO DE IMAGEN NO VÁLIDO");
                alert.setContentText("Asegúrese de que está insertando un archivo de imagen correcto.");
                alert.showAndWait();
                
                return null;
                
            }
            catch(Exception e){
                e.printStackTrace();
                
                System.out.println("Error no conocido");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("HUBO UN ERROR DESCONOCIDO");
                alert.setContentText("Sucedió un error desconocido. Revise el estado de la base de datos o del programa.");
                alert.showAndWait();
                
                return null;
            }
        }
        
        return originalImage;
    }
}
