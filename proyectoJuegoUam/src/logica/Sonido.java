package logica;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.File;

public class Sonido {

    private static Clip musica;
    private static boolean avisoMostrado = false;

    private Sonido() {
    }


    public static void reproducir(String nombre) {
        Thread hilo = new Thread(() -> {
            try {
                AudioInputStream audio = AudioSystem.getAudioInputStream(new File(Recursos.RUTA_SONIDOS + nombre));
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                // cuando termina se cierra para no dejar el clip abierto en memoria
                clip.addLineListener(evento -> {
                    if (evento.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
                clip.start();
            } catch (Exception e) {
                mostrarAviso(nombre);
            }
        });
        hilo.setDaemon(true);
        hilo.start();
    }


    public static synchronized void iniciarMusica(String nombre) {
        detenerMusica();
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(Recursos.RUTA_SONIDOS + nombre));
            musica = AudioSystem.getClip();
            musica.open(audio);
            musica.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            musica = null;
            mostrarAviso(nombre);
        }
    }


    public static synchronized void detenerMusica() {
        if (musica != null) {
            musica.stop();
            musica.close();
            musica = null;
        }
    }

    // solo muestra el error una vez para no llenar la consola
    private static void mostrarAviso(String nombre) {
        if (!avisoMostrado) {
            System.out.println("No se pudo reproducir el sonido " + nombre + " (revisar resources/sounds)");
            avisoMostrado = true;
        }
    }
}
