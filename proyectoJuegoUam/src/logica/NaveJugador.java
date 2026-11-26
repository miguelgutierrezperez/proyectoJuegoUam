package logica;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;


public class NaveJugador extends SpriteAnimado {

    public static final int TAMANO = 56;


    public static final int DURACION_PODER = 500;

    private static final int RECARGA_NORMAL = 28; // ticks entre disparo y disparo (unos 0.4 s)
    private static final int RECARGA_PODER = 8;   // con el poder dispara como 3 veces mas rapido
    private static final int TIEMPO_INVENCIBLE = 90;

    private final int velocidad = 6;
    private boolean izquierda;
    private boolean derecha;
    private boolean disparando;
    private int recarga = 0;
    private boolean conPoder = false;
    private int tiempoPoder = 0;
    private int invencible = 0;


    public NaveJugador(int x, int y) {
        super(x, y, TAMANO, TAMANO, 6);
        cargarCuadros("jugador_", 2);
    }


    @Override
    public void mover() {
        int dx = 0;
        if (izquierda) {
            dx -= velocidad;
        }
        if (derecha) {
            dx += velocidad;
        }
        x += dx;

        // limites para que no se salga de la ventana
        if (x < 0) {
            x = 0;
        }
        if (x > Juego.ANCHO - ancho) {
            x = Juego.ANCHO - ancho;
        }

        animar();

        if (recarga > 0) {
            recarga--;
        }
        if (conPoder) {
            tiempoPoder--;
            if (tiempoPoder <= 0) {
                quitarPoder();
            }
        }
        if (invencible > 0) {
            invencible--;
        }
    }


    public boolean puedeDisparar() {
        return disparando && recarga == 0;
    }

    public Bala disparar() {
        if (conPoder) {
            recarga = RECARGA_PODER;
            return new Bala(getCentroX() - 3, y - 12, 0, -12, true, "bala_poder.png");
        }
        recarga = RECARGA_NORMAL;
        return new Bala(getCentroX() - 3, y - 12, 0, -10, true, "bala_jugador.png");
    }


    public void activarPoder() {
        conPoder = true;
        tiempoPoder = DURACION_PODER;
        cargarCuadros("jugador_poder_", 2);
    }

    private void quitarPoder() {
        conPoder = false;
        tiempoPoder = 0;
        cargarCuadros("jugador_", 2);
    }


    public void recibirGolpe() {
        invencible = TIEMPO_INVENCIBLE;
    }

    public boolean estaInvencible() {
        return invencible > 0;
    }


    @Override
    public void dibujar(Graphics g, ImageObserver obs) {
        if (invencible > 0 && (invencible / 5) % 2 == 0) {
            return; // en este tick no se dibuja, asi se ve el parpadeo
        }
        super.dibujar(g, obs);
    }


    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 10, y + 8, ancho - 20, alto - 14);
    }

    public void setIzquierda(boolean izquierda) {
        this.izquierda = izquierda;
    }

    public void setDerecha(boolean derecha) {
        this.derecha = derecha;
    }

    public void setDisparando(boolean disparando) {
        this.disparando = disparando;
    }

    public boolean tienePoder() {
        return conPoder;
    }

    public int getTiempoPoder() {
        return tiempoPoder;
    }
}
