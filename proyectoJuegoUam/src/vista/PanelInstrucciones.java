package vista;

import eventos.ManejadorBotones;
import logica.Recursos;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class PanelInstrucciones extends PanelFondo {


    public PanelInstrucciones(ManejadorBotones manejador) {
        super();
        crearTexto("INSTRUCCIONES", 40, COLOR_TITULO, 0, 25, 800);

        agregarRegla("jugador_0.png", "<b>Moverse:</b> flechas izquierda / derecha (o A y D).", 98);
        agregarRegla("bala_jugador.png", "<b>Disparar:</b> barra espaciadora (se puede dejar oprimida).", 143);
        agregarRegla("enemigo_0.png", "Cada disparo que le pegues al enemigo vale <b>50 puntos</b>. "
                + "Cada <b>500 puntos</b> subes de nivel: el enemigo crece, se mueve mas rapido y dispara mas.", 188);
        agregarRegla("poder.png", "Si le pegas <b>5 disparos seguidos</b> sin fallar cae un rayo. "
                + "Atrapalo y tu nave se vuelve dorada y dispara mucho mas rapido por unos segundos. "
                + "Si fallas un disparo la racha vuelve a cero.", 258);
        agregarRegla("corazon.png", "Cada vez que subes de nivel cae un corazon. Si lo atrapas "
                + "recuperas <b>una vida</b> (maximo 5).", 340);
        agregarRegla("bala_enemigo.png", "Si una bala enemiga te pega pierdes una vida. Empiezas con <b>3 vidas</b>. "
                + "Cuando te quedas sin vidas se acaba el juego.", 398);

        crearBoton("Volver", ManejadorBotones.ATRAS, manejador, 200, 490, 180);
        crearBoton("Inicio", ManejadorBotones.INICIO, manejador, 420, 490, 180);
    }

    private void agregarRegla(String imagen, String texto, int y) {
        JLabel icono = new JLabel(new ImageIcon(Recursos.cargarImagen(imagen, 34, 34)));
        icono.setBounds(85, y, 40, 40);
        add(icono);

        JLabel label = new JLabel("<html>" + texto + "</html>");
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
        label.setVerticalAlignment(JLabel.TOP);
        label.setBounds(140, y + 2, 580, 66);
        add(label);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dibujarRecuadro(g, 70, 85, 660, 380);
    }
}

