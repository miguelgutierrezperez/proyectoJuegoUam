package logica;

import java.io.Serializable;

public class Jugador implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nombre;
    private final int puntaje;
    private final int tiempoSegundos;
    private final int nivel;


    public Jugador(String nombre, int puntaje, int tiempoSegundos, int nivel) {
        this.nombre = nombre;
        this.puntaje = puntaje;
        this.tiempoSegundos = tiempoSegundos;
        this.nivel = nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getTiempoSegundos() {
        return tiempoSegundos;
    }

    public int getNivel() {
        return nivel;
    }

    @Override
    public String toString() {
        return nombre + " - " + puntaje + " pts";
    }
}

