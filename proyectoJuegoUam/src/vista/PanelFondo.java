package vista;

import logica.Recursos;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;


public class PanelFondo extends JPanel {

    protected static final Color COLOR_BOTON = new Color(90, 40, 170);
    protected static final Color COLOR_TITULO = new Color(255, 210, 60);

    private final Image fondo;


    public PanelFondo() {
        setLayout(null);
        fondo = Recursos.cargarImagen("fondo_menu.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }

    protected JButton crearBoton(String texto, String comando, ActionListener listener, int x, int y, int ancho) {
        JButton boton = new JButton(texto);
        boton.setActionCommand(comando);
        boton.addActionListener(listener);
        boton.setBounds(x, y, ancho, 42);
        boton.setFont(new Font("Arial", Font.BOLD, 17));
        boton.setBackground(COLOR_BOTON);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(boton);
        return boton;
    }

    protected JLabel crearTexto(String texto, int tamano, Color color, int x, int y, int ancho) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, tamano));
        label.setForeground(color);
        label.setBounds(x, y, ancho, tamano + 14);
        add(label);
        return label;
    }

    protected void dibujarRecuadro(Graphics g, int x, int y, int ancho, int alto) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(x, y, ancho, alto, 25, 25);
        g.setColor(new Color(150, 100, 255));
        g.drawRoundRect(x, y, ancho, alto, 25, 25);
    }
}
