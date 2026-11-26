package vista;

import eventos.ManejadorBotones;
import logica.Recursos;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Graphics;


public class PanelBienvenida extends PanelFondo {

    private static final String[] INTEGRANTES = {
            "Miguel Gutierrez Perez",
            "Isabella Lopez Moncada",
    };
    private static final String MATERIA = "Tecnicas de Programacion";


    public PanelBienvenida(ManejadorBotones manejador) {
        super();

        JLabel logo = new JLabel(new ImageIcon(Recursos.cargarImagen("logo_uam.png", 96, 120)));
        logo.setBounds(25, 20, 96, 120);
        add(logo);

        crearTexto("DUELO ESTELAR", 58, COLOR_TITULO, 0, 60, 800);
        crearTexto("1 vs 1 ", 20, Color.WHITE, 0, 135, 800);

        crearTexto(MATERIA, 18, new Color(180, 220, 255), 200, 205, 400);
        crearTexto("Integrantes:", 16, Color.LIGHT_GRAY, 200, 240, 400);
        int y = 268;
        for (String nombre : INTEGRANTES) {
            crearTexto(nombre, 17, Color.WHITE, 200, y, 400);
            y += 28;
        }

        crearBoton("Iniciar", ManejadorBotones.INICIAR, manejador, 300, 380, 200);
        crearBoton("Instrucciones", ManejadorBotones.INSTRUCCIONES, manejador, 300, 435, 200);
        crearBoton("Salir", ManejadorBotones.SALIR, manejador, 300, 490, 200);

        crearTexto("Universidad Autonoma de Manizales - 2026", 12, Color.GRAY, 0, 565, 800);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dibujarRecuadro(g, 200, 198, 400, 165);
    }
}
