package logica;


public class Corazon extends PowerUp {

    public Corazon(int x, int y) {
        super(x, y, "corazon.png");
    }

    @Override
    public void aplicar(Juego juego) {
        juego.sumarVida();
        Sonido.reproducir("poder.wav");
    }

    @Override
    public String getMensaje() {
        return "+1 VIDA";
    }
}
