package logica;


public class Explosion extends SpriteAnimado {

    private static final int CUADROS = 5;
    private static final int RETARDO = 4;
    private int ticks = 0;


    public Explosion(int centroX, int centroY, int tamano) {
        super(centroX - tamano / 2, centroY - tamano / 2, tamano, tamano, RETARDO);
        cargarCuadros("explosion_", CUADROS);
    }

    @Override
    public void mover() {
        ticks++;
        animar();
        if (ticks >= CUADROS * RETARDO) {
            visible = false;
        }
    }
}
