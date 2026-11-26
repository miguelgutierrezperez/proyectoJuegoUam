package logica;

public class Bala extends Sprite {

    private final int dx;
    private final int dy;
    private final boolean delJugador;


    public Bala(int x, int y, int dx, int dy, boolean delJugador, String nombreImagen) {
        super(x, y, delJugador ? 6 : 12, delJugador ? 18 : 12);
        this.dx = dx;
        this.dy = dy;
        this.delJugador = delJugador;
        cargarImagen(nombreImagen);
    }

    @Override
    public void mover() {
        x += dx;
        y += dy;
    }


    public boolean salioDePantalla() {
        return y + alto < 0 || y > Juego.ALTO || x + ancho < 0 || x > Juego.ANCHO;
    }

    public boolean esDelJugador() {
        return delJugador;
    }
}
