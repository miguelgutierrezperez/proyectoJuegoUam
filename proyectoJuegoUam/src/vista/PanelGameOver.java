package vista;

import eventos.ManejadorBotones;
import logica.Juego;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;


public class PanelGameOver extends PanelFondo {

    private final JLabel lblNombre;
    private final JLabel lblPuntaje;
    private final JLabel lblTiempo;
    private final JLabel lblNivel;
    private final JLabel[] lblTop = new JLabel[3];

    public PanelGameOver(ManejadorBotones manejador) {
        super();
        crearTexto("GAME OVER", 56, Color.RED, 0, 25, 800);

        lblNombre = crearTexto("", 22, Color.WHITE, 60, 125, 330);
        lblPuntaje = crearTexto("", 20, Color.WHITE, 60, 175, 330);
        lblTiempo = crearTexto("", 20, Color.WHITE, 60, 215, 330);
        lblNivel = crearTexto("", 20, Color.WHITE, 60, 255, 330);

        crearTexto("TOP 3", 28, COLOR_TITULO, 410, 120, 330);
        for (int i = 0; i < 3; i++) {
            lblTop[i] = crearTexto("", 20, Color.WHITE, 410, 175 + i * 42, 330);
        }

        crearBoton("Jugar otra vez", ManejadorBotones.REPETIR, manejador, 200, 400, 180);
        crearBoton("Volver al inicio", ManejadorBotones.INICIO, manejador, 420, 400, 180);
    }


    public void mostrarDatos(Jugador jugador, List<Jugador> top3) {
        lblNombre.setText("Piloto: " + jugador.getNombre());
        lblPuntaje.setText("Puntaje final: " + jugador.getPuntaje());
        lblTiempo.setText("Tiempo jugado: " + Juego.formatearTiempo(jugador.getTiempoSegundos()));
        lblNivel.setText("Nivel alcanzado: " + jugador.getNivel());

        String[] medallas = {"1.", "2.", "3."};
        Color[] colores = {new Color(255, 215, 0), new Color(200, 200, 210), new Color(205, 127, 50)};
        for (int i = 0; i < 3; i++) {
            if (i < top3.size()) {
                Jugador j = top3.get(i);
                lblTop[i].setText(medallas[i] + " " + j.getNombre() + "   " + j.getPuntaje() + " pts");
            } else {
                lblTop[i].setText(medallas[i] + " ---");
            }
            lblTop[i].setForeground(colores[i]);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dibujarRecuadro(g, 60, 110, 330, 200);
        dibujarRecuadro(g, 410, 110, 330, 200);
    }
}

