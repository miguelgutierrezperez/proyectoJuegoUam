package vista;

import eventos.JuegoListener;
import eventos.ManejadorBotones;
import logica.Historial;
import logica.Jugador;
import logica.Sonido;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;


public class VentanaPrincipal extends JFrame implements JuegoListener {

    private static final String BIENVENIDA = "BIENVENIDA";
    private static final String INSTRUCCIONES = "INSTRUCCIONES";
    private static final String REGISTRO = "REGISTRO";
    private static final String JUEGO = "JUEGO";
    private static final String GAME_OVER = "GAME_OVER";

    private final CardLayout tarjetas = new CardLayout();
    private final JPanel contenedor = new JPanel(tarjetas);
    private final PanelRegistro panelRegistro;
    private final PanelJuego panelJuego;
    private final PanelGameOver panelGameOver;

    private String pantallaActual = BIENVENIDA;
    private String pantallaAnterior = BIENVENIDA;
    private String ultimoNombre = "";

    public VentanaPrincipal() {
        setTitle("Duelo Estelar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        ManejadorBotones manejador = new ManejadorBotones(this);
        panelRegistro = new PanelRegistro(manejador);
        panelJuego = new PanelJuego(this);
        panelGameOver = new PanelGameOver(manejador);

        contenedor.add(new PanelBienvenida(manejador), BIENVENIDA);
        contenedor.add(new PanelInstrucciones(manejador), INSTRUCCIONES);
        contenedor.add(panelRegistro, REGISTRO);
        contenedor.add(panelJuego, JUEGO);
        contenedor.add(panelGameOver, GAME_OVER);

        contenedor.setPreferredSize(new Dimension(800, 600));
        add(contenedor);
        pack();
        setLocationRelativeTo(null);

        Sonido.reproducir("inicio.wav");
    }

    private void cambiarPantalla(String nombre) {
        if (!nombre.equals(pantallaActual)) {
            pantallaAnterior = pantallaActual;
            pantallaActual = nombre;
        }
        tarjetas.show(contenedor, nombre);
    }

    public void mostrarBienvenida() {
        panelJuego.detener();
        cambiarPantalla(BIENVENIDA);
        Sonido.reproducir("inicio.wav");
    }

    public void mostrarInstrucciones() {
        cambiarPantalla(INSTRUCCIONES);
    }


    public void mostrarRegistro() {
        cambiarPantalla(REGISTRO);
        panelRegistro.enfocarCampo();
    }

    public void volverAtras() {
        if (pantallaAnterior.equals(REGISTRO)) {
            mostrarRegistro();
        } else if (pantallaAnterior.equals(INSTRUCCIONES)) {
            mostrarInstrucciones();
        } else {
            cambiarPantalla(BIENVENIDA);
        }
    }

    public void empezarPartida() {
        String nombre = panelRegistro.getNombre();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe un nombre para poder jugar", "Falta el nombre",
                    JOptionPane.WARNING_MESSAGE);
            panelRegistro.enfocarCampo();
            return;
        }
        ultimoNombre = nombre;
        cambiarPantalla(JUEGO);
        panelJuego.iniciarPartida(nombre);
    }

    public void repetirPartida() {
        if (ultimoNombre.isEmpty()) {
            mostrarRegistro();
            return;
        }
        cambiarPantalla(JUEGO);
        panelJuego.iniciarPartida(ultimoNombre);
    }

    @Override
    public void juegoTerminado(Jugador resultado) {
        panelGameOver.mostrarDatos(resultado, Historial.getTop3());
        cambiarPantalla(GAME_OVER);
    }
}
