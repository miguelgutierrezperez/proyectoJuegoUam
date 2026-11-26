package eventos;

import logica.Juego;
import logica.NaveJugador;
import vista.PanelJuego;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class TecladoJuego extends KeyAdapter {

    private final PanelJuego panel;

    public TecladoJuego(PanelJuego panel) {
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        cambiarTecla(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        cambiarTecla(e.getKeyCode(), false);
    }


    private void cambiarTecla(int tecla, boolean presionada) {
        Juego juego = panel.getJuego();
        if (juego == null || juego.isTerminado()) {
            return;
        }
        NaveJugador nave = juego.getNave();

        switch (tecla) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                nave.setIzquierda(presionada);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                nave.setDerecha(presionada);
                break;
            case KeyEvent.VK_SPACE:
                nave.setDisparando(presionada);
                break;
            default:
                break;
        }
    }
}
