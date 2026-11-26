package vista;

import eventos.JuegoListener;
import eventos.TecladoJuego;
import logica.Bala;
import logica.Explosion;
import logica.Juego;
import logica.NaveJugador;
import logica.PowerUp;
import logica.Recursos;
import logica.Sonido;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelJuego extends JPanel implements ActionListener {

    private final Timer timer;
    private final Timer reloj;
    private final JuegoListener listener;
    private final Image fondo;
    private final Image iconoVida;

    private Juego juego;
    private boolean finAvisado = false;
    private int desplazamientoFondo = 0;

    public PanelJuego(JuegoListener listener) {
        this.listener = listener;
        setBackground(Color.BLACK);
        setFocusable(true);
        setDoubleBuffered(true);

        fondo = Recursos.cargarImagen("fondo_juego.png");
        iconoVida = Recursos.cargarImagen("corazon.png", 22, 22);

        timer = new Timer(15, this);
        reloj = new Timer(1000, e -> {
            if (juego != null) {
                juego.contarSegundo();
            }
        });
        addKeyListener(new TecladoJuego(this));
    }


    public void iniciarPartida(String nombre) {
        detener();
        juego = new Juego(nombre);
        juego.iniciar();
        finAvisado = false;
        Sonido.iniciarMusica("juego.wav");
        timer.start();
        reloj.start();
        requestFocusInWindow();
    }


    public void detener() {
        timer.stop();
        reloj.stop();
        if (juego != null) {
            juego.detener();
        }
        Sonido.detenerMusica();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        juego.actualizar();
        desplazamientoFondo = (desplazamientoFondo + 1) % Juego.ALTO;

        if (juego.isTerminado() && !finAvisado) {
            finAvisado = true;
            finDelJuego();
        }
        repaint();
    }

    private void finDelJuego() {
        reloj.stop();
        Sonido.detenerMusica();
        Sonido.reproducir("gameover.wav");

        Timer espera = new Timer(2000, e -> {
            timer.stop();
            listener.juegoTerminado(juego.getResultado());
        });
        espera.setRepeats(false);
        espera.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (juego == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(fondo, 0, desplazamientoFondo, Juego.ANCHO, Juego.ALTO, this);
        g.drawImage(fondo, 0, desplazamientoFondo - Juego.ALTO, Juego.ANCHO, Juego.ALTO, this);

        juego.getEnemigo().dibujar(g, this);
        if (!juego.isTerminado()) {
            juego.getNave().dibujar(g, this);
        }
        for (Bala b : juego.getBalasJugador()) {
            b.dibujar(g, this);
        }
        for (Bala b : juego.getBalasEnemigo()) {
            b.dibujar(g, this);
        }
        for (PowerUp p : juego.getPoderes()) {
            p.dibujar(g, this);
        }
        for (Explosion ex : juego.getExplosiones()) {
            ex.dibujar(g, this);
        }

        dibujarHUD(g);
        dibujarMensaje(g);

        if (juego.isTerminado()) {
            g.setColor(new Color(0, 0, 0, 140));
            g.fillRect(0, 0, Juego.ANCHO, Juego.ALTO);
            dibujarCentrado(g, "GAME OVER", new Font("Arial", Font.BOLD, 60), Color.RED, 300);
        }
    }


    private void dibujarHUD(Graphics g) {
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, 0, Juego.ANCHO, 34);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        g.drawString("Piloto: " + juego.getNombreJugador(), 12, 23);
        g.drawString("Puntaje: " + juego.getPuntaje(), 220, 23);
        g.drawString("Nivel: " + juego.getNivel(), 380, 23);
        g.drawString("Tiempo: " + Juego.formatearTiempo(juego.getTiempoSegundos()), 480, 23);

        for (int i = 0; i < juego.getVidas(); i++) {
            g.drawImage(iconoVida, Juego.ANCHO - 30 - i * 26, 6, this);
        }

        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(Color.WHITE);
        g.drawString("Racha", 12, Juego.ALTO - 14);
        for (int i = 0; i < Juego.RACHA_PARA_PODER; i++) {
            int cx = 65 + i * 20;
            if (i < juego.getRacha()) {
                g.setColor(new Color(255, 210, 0));
                g.fillOval(cx, Juego.ALTO - 27, 14, 14);
            }
            g.setColor(Color.WHITE);
            g.drawOval(cx, Juego.ALTO - 27, 14, 14);
        }

        NaveJugador nave = juego.getNave();
        if (nave.tienePoder()) {
            int anchoBarra = 200 * nave.getTiempoPoder() / NaveJugador.DURACION_PODER;
            g.setColor(new Color(255, 210, 0));
            g.drawString("DISPARO RAPIDO", Juego.ANCHO - 330, Juego.ALTO - 14);
            g.fillRect(Juego.ANCHO - 210, Juego.ALTO - 26, anchoBarra, 12);
            g.setColor(Color.WHITE);
            g.drawRect(Juego.ANCHO - 210, Juego.ALTO - 26, 200, 12);
        }
    }

    private void dibujarMensaje(Graphics g) {
        String mensaje = juego.getMensaje();
        if (!mensaje.isEmpty() && !juego.isTerminado()) {
            dibujarCentrado(g, mensaje, new Font("Arial", Font.BOLD, 30), new Color(255, 230, 90), 330);
        }
    }

    private void dibujarCentrado(Graphics g, String texto, Font fuente, Color color, int y) {
        g.setFont(fuente);
        FontMetrics fm = g.getFontMetrics();
        int x = (Juego.ANCHO - fm.stringWidth(texto)) / 2;
        g.setColor(Color.BLACK);
        g.drawString(texto, x + 2, y + 2);
        g.setColor(color);
        g.drawString(texto, x, y);
    }

    public Juego getJuego() {
        return juego;
    }
}