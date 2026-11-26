package logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Aqui esta toda la logica de una partida: las naves, las balas,
 * los power-ups, el puntaje, las vidas y el nivel.
 * La vista (PanelJuego) solo llama actualizar() y dibuja lo que hay aca.
 *
 * @author Equipo Duelo Estelar
 */
public class Juego {

    public static final int ANCHO = 800;
    public static final int ALTO = 600;

    public static final int PUNTOS_POR_IMPACTO = 50;
    public static final int PUNTOS_POR_NIVEL = 500;
    public static final int RACHA_PARA_PODER = 5;
    public static final int VIDAS_INICIALES = 3;
    public static final int VIDAS_MAXIMAS = 5;

    private final NaveEcuador nave;
    private final NaveEnemiga enemigo;

    private final List<Bala> balasJugador = new ArrayList<>();
    // esta lista la modifica el HiloEnemigo y el timer al mismo tiempo,
    // por eso es CopyOnWriteArrayList (con ArrayList salia ConcurrentModificationException)
    private final List<Bala> balasEnemigo = new CopyOnWriteArrayList<>();
    private final List<PowerUp> poderes = new ArrayList<>();
    private final List<Explosion> explosiones = new ArrayList<>();

    private final String nombreJugador;
    private int puntaje = 0;
    private int vidas = VIDAS_INICIALES;
    private int nivel = 1;
    private int tiempoSegundos = 0;
    private int racha = 0; // disparos seguidos que le han pegado al enemigo
    private volatile boolean terminado = false;

    private String mensaje = "";
    private int tiempoMensaje = 0;

    private HiloEnemigo hiloEnemigo;
    private Jugador resultado;
    private final Random random = new Random();

    /**
     * Crea una partida nueva.
     *
     * @param nombreJugador nombre que ingreso el usuario
     */
    public Juego(String nombreJugador) {
        this.nombreJugador = nombreJugador;
        nave = new NaveEcuador(ANCHO / 2 - NaveEcuador.TAMANO / 2, ALTO - 90);
        enemigo = new NaveEnemiga(ANCHO / 2 - 35, 50);
    }

    /**
     * Arranca el hilo que hace disparar al enemigo.
     */
    public void iniciar() {
        hiloEnemigo = new HiloEnemigo(this);
        hiloEnemigo.start();
        mostrarMensaje("NIVEL 1");
    }

    /**
     * Se llama en cada tick del Timer. Mueve todo y revisa colisiones.
     */
    public void actualizar() {
        if (terminado) {
            moverExplosiones(); // que se termine de ver la explosion final
            return;
        }

        nave.mover();
        if (nave.puedeDisparar()) {
            balasJugador.add(nave.disparar());
            Sonido.reproducir("disparo.wav");
        }
        enemigo.mover();

        moverBalasJugador();
        moverBalasEnemigo();
        moverPoderes();
        moverExplosiones();

        if (tiempoMensaje > 0) {
            tiempoMensaje--;
        }
    }

    private void moverBalasJugador() {
        for (Bala b : balasJugador) {
            b.mover();
            if (b.getBounds().intersects(enemigo.getBounds())) {
                b.setVisible(false);
                impactoAlEnemigo(b);
            } else if (b.salioDePantalla()) {
                // fallo el disparo, se pierde la racha
                b.setVisible(false);
                racha = 0;
            }
        }
        balasJugador.removeIf(b -> !b.isVisible());
    }

    private void impactoAlEnemigo(Bala b) {
        puntaje += PUNTOS_POR_IMPACTO;
        racha++;
        explosiones.add(new Explosion(b.getCentroX(), b.getY(), 40));
        Sonido.reproducir("colision.wav");

        // si encadena 5 disparos seguidos cae el poder especial
        if (racha >= RACHA_PARA_PODER) {
            racha = 0;
            poderes.add(new PoderDisparo(enemigo.getCentroX() - 18, enemigo.getY() + enemigo.getAlto()));
            mostrarMensaje("RACHA x5! ATRAPA EL PODER");
        }
        revisarNivel();
    }

    /**
     * Revisa si con el puntaje actual ya toca subir de nivel.
     */
    private void revisarNivel() {
        int nuevoNivel = calcularNivel(puntaje);
        if (nuevoNivel > nivel) {
            nivel = nuevoNivel;
            enemigo.subirNivel(nivel);
            // cada vez que se sube de nivel cae un corazon en un lugar al azar
            poderes.add(new Corazon(random.nextInt(ANCHO - 60) + 20, 0));
            mostrarMensaje("NIVEL " + nivel + " - EL ENEMIGO CRECE!");
        }
    }

    /**
     * Calcula el nivel de forma recursiva: cada 500 puntos es un nivel mas.
     * Ej: calcularNivel(1200) = 1 + calcularNivel(700) = 1 + 1 + calcularNivel(200) = 3
     *
     * @param puntos puntaje
     * @return el nivel que corresponde a ese puntaje
     */
    public static int calcularNivel(int puntos) {
        if (puntos < PUNTOS_POR_NIVEL) {
            return 1; // caso base
        }
        return 1 + calcularNivel(puntos - PUNTOS_POR_NIVEL);
    }

    private void moverBalasEnemigo() {
        for (Bala b : balasEnemigo) {
            b.mover();
            if (!nave.estaInvencible() && b.getBounds().intersects(nave.getBounds())) {
                b.setVisible(false);
                perderVida();
            } else if (b.salioDePantalla()) {
                b.setVisible(false);
            }
        }
        balasEnemigo.removeIf(b -> !b.isVisible());
    }

    private void perderVida() {
        vidas--;
        racha = 0;
        nave.recibirGolpe();
        explosiones.add(new Explosion(nave.getCentroX(), nave.getY() + nave.getAlto() / 2, 70));
        Sonido.reproducir("colision.wav");
        if (vidas <= 0) {
            terminar();
        }
    }

    private void moverPoderes() {
        for (PowerUp p : poderes) {
            p.mover();
            if (p.isVisible() && p.getBounds().intersects(nave.getBounds())) {
                p.aplicar(this); // polimorfismo: cada power-up hace lo suyo
                mostrarMensaje(p.getMensaje());
                p.setVisible(false);
            }
        }
        poderes.removeIf(p -> !p.isVisible());
    }

    private void moverExplosiones() {
        for (Explosion e : explosiones) {
            e.mover();
        }
        explosiones.removeIf(e -> !e.isVisible());
    }

    /**
     * Lo llama el HiloEnemigo cada vez que el enemigo tiene que disparar.
     */
    public void dispararEnemigo() {
        if (!terminado) {
            balasEnemigo.addAll(enemigo.disparar(nivel));
        }
    }

    /**
     * @return milisegundos entre cada disparo del enemigo (baja con el nivel)
     */
    public int getIntervaloDisparo() {
        int intervalo = 1400 - (nivel - 1) * 150;
        return Math.max(intervalo, 400);
    }

    /**
     * Suma una vida sin pasarse del maximo.
     */
    public void sumarVida() {
        if (vidas < VIDAS_MAXIMAS) {
            vidas++;
        }
    }

    /**
     * Lo llama el timer del reloj cada segundo.
     */
    public void contarSegundo() {
        if (!terminado) {
            tiempoSegundos++;
        }
    }

    private void terminar() {
        terminado = true;
        detener();
        resultado = new Jugador(nombreJugador, puntaje, tiempoSegundos, nivel);
        Historial.guardar(resultado);
    }

    /**
     * Para el hilo del enemigo (por ejemplo si se sale al menu a mitad de partida).
     */
    public void detener() {
        if (hiloEnemigo != null) {
            hiloEnemigo.detener();
        }
    }

    private void mostrarMensaje(String texto) {
        mensaje = texto;
        tiempoMensaje = 120;
    }

    /**
     * Pasa los segundos a formato mm:ss.
     *
     * @param segundos segundos totales
     * @return el tiempo como texto, ej: 02:05
     */
    public static String formatearTiempo(int segundos) {
        return String.format("%02d:%02d", segundos / 60, segundos % 60);
    }

    // ---------------- getters ----------------

    public NaveEcuador getNave() {
        return nave;
    }

    public NaveEnemiga getEnemigo() {
        return enemigo;
    }

    public List<Bala> getBalasJugador() {
        return balasJugador;
    }

    public List<Bala> getBalasEnemigo() {
        return balasEnemigo;
    }

    public List<PowerUp> getPoderes() {
        return poderes;
    }

    public List<Explosion> getExplosiones() {
        return explosiones;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getVidas() {
        return vidas;
    }

    public int getNivel() {
        return nivel;
    }

    public int getTiempoSegundos() {
        return tiempoSegundos;
    }

    public int getRacha() {
        return racha;
    }

    public boolean isTerminado() {
        return terminado;
    }

    public Jugador getResultado() {
        return resultado;
    }

    /**
     * @return el mensaje que se muestra en el centro, o "" si ya se acabo su tiempo
     */
    public String getMensaje() {
        return tiempoMensaje > 0 ? mensaje : "";
    }
}
