package logica;


public abstract class PowerUp extends Sprite {

    private static final int TAMANO = 36;
    private final int velocidad = 3;


    public PowerUp(int x, int y, String nombreImagen) {
        super(x, y, TAMANO, TAMANO);
        cargarImagen(nombreImagen);
    }


    @Override
    public void mover() {
        y += velocidad;
        if (y > Juego.ALTO) {
            visible = false;
        }
    }


    public abstract void aplicar(Juego juego);


    public abstract String getMensaje();
}
