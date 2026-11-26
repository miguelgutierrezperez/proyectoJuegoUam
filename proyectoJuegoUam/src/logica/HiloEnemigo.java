package logica;

public class HiloEnemigo extends Thread {

    private final Juego juego;
    private volatile boolean activo = true; // volatile porque lo cambia otro hilo


    public HiloEnemigo(Juego juego) {
        this.juego = juego;
        setDaemon(true); // asi si se cierra la ventana el hilo no deja el programa abierto
    }

    @Override
    public void run() {
        while (activo) {
            try {
                Thread.sleep(juego.getIntervaloDisparo());
            } catch (InterruptedException e) {
                break; // lo interrumpieron porque se acabo el juego
            }
            if (activo && !juego.isTerminado()) {
                juego.dispararEnemigo();
            }
        }
    }


    public void detener() {
        activo = false;
        interrupt();
    }
}