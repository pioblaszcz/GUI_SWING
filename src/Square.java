import java.awt.*;
import java.awt.geom.Rectangle2D;

class Square extends Shape {
    private int x, y, side;
    private Color color;

    public Square(int x, int y, int side) {
        this.x = x;
        this.y = y;
        this.side = side;
        this.color = new Color((int)(Math.random() * 0x1000000));
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x - side / 2, y - side / 2, side, side);
    }

    @Override
    public boolean contains(int x, int y) {
        return x >= this.x - side / 2 && x <= this.x + side / 2 && y >= this.y - side / 2 && y <= this.y + side / 2;
    }
}
