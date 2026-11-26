package logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NaveEnemiga extends SpriteAnimado {

    private static final int TAMANO_BASE = 70;
    private static final int TAMANO_MAXIMO = 220;
    private static final int CRECIMIENTO = 20; // pixeles que crece por nivel

    private int velocidad = 2;
    private int direccion = 1; // 1 = derecha, -1 = izquierda
    private final Random random = new Random();


    public NaveEnemiga(int x, int y) {
        super(x, y, TAMANO_BASE, TAMANO_BASE, 15);
        cargarCuadros("enemigo_", 2);
    }


    @Override
    public void mover() {
        x += velocidad * direccion;

        if (x <= 0) {
            x = 0;
            direccion = 1;
        } else if (x + ancho >= Juego.ANCHO) {
            x = Juego.ANCHO - ancho;
            direccion = -1;
        } else if (random.nextInt(150) == 0) {
            direccion = -direccion;
        }

        animar();
    }


    public void subirNivel(int nivel) {
        int nuevoTamano = TAMANO_BASE + (nivel - 1) * CRECIMIENTO;
        if (nuevoTamano > TAMANO_MAXIMO) {
            nuevoTamano = TAMANO_MAXIMO;
        }
        // se corre a la izquierda la mitad de lo que crece para que crezca "desde el centro"
        x -= (nuevoTamano - ancho) / 2;
        ancho = nuevoTamano;
        alto = nuevoTamano;
        velocidad = 2 + (nivel - 1);

        if (x < 0) {
            x = 0;
        }
        if (x + ancho > Juego.ANCHO) {
            x = Juego.ANCHO - ancho;
        }
        cargarCuadros("enemigo_", 2); // toca volver a escalar las imagenes
    }


    public List<Bala> disparar(int nivel) {
        List<Bala> nuevas = new ArrayList<>();
        int velBala = 5 + nivel / 2;
        int bx = getCentroX() - 6;
        int by = y + alto - 10;

        nuevas.add(new Bala(bx, by, 0, velBala, false, "bala_enemigo.png"));
        if (nivel >= 4) {
            nuevas.add(new Bala(bx, by, -2, velBala, false, "bala_enemigo.png"));
            nuevas.add(new Bala(bx, by, 2, velBala, false, "bala_enemigo.png"));
        }
        return nuevas;
    }

    public int getVelocidad() {
        return velocidad;
    }
}
