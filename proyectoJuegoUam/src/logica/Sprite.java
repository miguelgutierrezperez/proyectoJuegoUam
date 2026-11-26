package logica;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;


public abstract class Sprite {

    protected int x;
    protected int y;
    protected int ancho;
    protected int alto;
    protected boolean visible = true;
    protected Image imagen;


    public Sprite(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }


    protected void cargarImagen(String nombreImagen) {
        imagen = Recursos.cargarImagen(nombreImagen, ancho, alto);
    }


    public abstract void mover();


    public void dibujar(Graphics g, ImageObserver obs) {
        Image img = getImagen();
        if (img != null) {
            g.drawImage(img, x, y, obs);
        } else {
            g.setColor(Color.MAGENTA);
            g.fillRect(x, y, ancho, alto);
        }
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, ancho, alto);
    }

    public Image getImagen() {
        return imagen;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }


    public int getCentroX() {
        return x + ancho / 2;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
