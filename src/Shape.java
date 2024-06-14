import java.awt.*;
import java.io.Serializable;

abstract class Shape implements Serializable {
    public abstract void draw(Graphics g);
    public abstract boolean contains(int x, int y);
}
