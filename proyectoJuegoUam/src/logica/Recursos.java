package logica;

import javax.swing.ImageIcon;
import java.awt.Image;


public class Recursos {


    public static final String RUTA_IMAGENES = "resources/images/";


    public static final String RUTA_SONIDOS = "resources/sounds/";

    // constructor privado porque esta clase solo tiene metodos estaticos
    private Recursos() {
    }


    public static Image cargarImagen(String nombre, int ancho, int alto) {
        ImageIcon icono = new ImageIcon(RUTA_IMAGENES + nombre);
        if (icono.getIconWidth() <= 0) {
            System.out.println("No se encontro la imagen: " + nombre);
            return null;
        }
        // se vuelve a meter en un ImageIcon para que la imagen quede cargada de una vez
        // (si no, a veces sale en blanco el primer frame)
        Image escalada = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada).getImage();
    }


    public static Image cargarImagen(String nombre) {
        return new ImageIcon(RUTA_IMAGENES + nombre).getImage();
    }
}
