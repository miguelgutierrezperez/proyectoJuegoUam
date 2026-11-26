package logica;

import java.awt.Image;


public abstract class SpriteAnimado extends Sprite {

    protected Image[] cuadros;
    private int cuadroActual = 0;
    private int contador = 0;
    private final int retardo;


    public SpriteAnimado(int x, int y, int ancho, int alto, int retardo) {
        super(x, y, ancho, alto);
        this.retardo = retardo;
    }


    protected void cargarCuadros(String prefijo, int cantidad) {
        cuadros = new Image[cantidad];
        for (int i = 0; i < cantidad; i++) {
            cuadros[i] = Recursos.cargarImagen(prefijo + i + ".png", ancho, alto);
        }
        cuadroActual = 0;
    }


    public void animar() {
        contador++;
        if (contador >= retardo) {
            contador = 0;
            cuadroActual++;
            if (cuadroActual >= cuadros.length) {
                cuadroActual = 0;
            }
        }
    }


    @Override
    public Image getImagen() {
        if (cuadros == null || cuadros.length == 0) {
            return imagen;
        }
        return cuadros[cuadroActual];
    }

    protected int getCuadroActual() {
        return cuadroActual;
    }
}
