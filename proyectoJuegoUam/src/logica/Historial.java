package logica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class Historial {

    private static final String ARCHIVO = "historial.dat";

    private Historial() {
    }

    public static void guardar(Jugador jugador) {
        List<Jugador> lista = leerTodos();
        lista.add(jugador);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.out.println("Error guardando el historial: " + e.getMessage());
        }
    }


    public static List<Jugador> leerTodos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Jugador>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudo leer el historial, se empieza uno nuevo");
            return new ArrayList<>();
        }
    }


    public static void ordenarPorPuntaje(List<Jugador> lista) {
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            boolean huboCambio = false;
            for (int j = 0; j < n - 1 - i; j++) {
                Jugador a = lista.get(j);
                Jugador b = lista.get(j + 1);
                boolean cambiar = a.getPuntaje() < b.getPuntaje()
                        || (a.getPuntaje() == b.getPuntaje() && a.getTiempoSegundos() > b.getTiempoSegundos());
                if (cambiar) {
                    lista.set(j, b);
                    lista.set(j + 1, a);
                    huboCambio = true;
                }
            }
            // si en una pasada no se cambio nada ya esta ordenada
            if (!huboCambio) {
                break;
            }
        }
    }


    public static List<Jugador> getTop3() {
        List<Jugador> lista = leerTodos();
        ordenarPorPuntaje(lista);
        List<Jugador> top = new ArrayList<>();
        for (int i = 0; i < lista.size() && i < 3; i++) {
            top.add(lista.get(i));
        }
        return top;
    }
}
