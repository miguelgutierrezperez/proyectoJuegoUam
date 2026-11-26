package eventos;

import vista.VentanaPrincipal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlTeclado implements ActionListener {

    public static final String INICIAR = "INICIAR";
    public static final String INSTRUCCIONES = "INSTRUCCIONES";
    public static final String JUGAR = "JUGAR";
    public static final String REPETIR = "REPETIR";
    public static final String ATRAS = "ATRAS";
    public static final String INICIO = "INICIO";
    public static final String SALIR = "SALIR";

    private final VentanaPrincipal ventana;

    public ControlTeclado(VentanaPrincipal ventana) {
        this.ventana = ventana;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case INICIAR:
                ventana.mostrarRegistro();
                break;
            case INSTRUCCIONES:
                ventana.mostrarInstrucciones();
                break;
            case JUGAR:
                ventana.empezarPartida();
                break;
            case REPETIR:
                ventana.repetirPartida();
                break;
            case ATRAS:
                ventana.volverAtras();
                break;
            case INICIO:
                ventana.mostrarBienvenida();
                break;
            case SALIR:
                System.exit(0);
                break;
            default:
                System.out.println("Boton sin accion: " + e.getActionCommand());
        }
    }
}