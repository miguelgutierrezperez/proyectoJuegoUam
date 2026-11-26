package logica;


public class PoderDisparo extends PowerUp {

    public PoderDisparo(int x, int y) {
        super(x, y, "poder.png");
    }

    @Override
    public void aplicar(Juego juego) {
        juego.getNave().activarPoder();
        Sonido.reproducir("poder.wav");
    }

    @Override
    public String getMensaje() {
        return "DISPARO RAPIDO!";
    }
}
