import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main extends JFrame implements MouseListener{
    int x,y;

    ArrayList<Integer> alX = new ArrayList<>();
    ArrayList<Integer> alY = new ArrayList<>();

    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        this.setSize(500, 500);
        this.setTitle("Drawing app");
        this.setLayout(null);
        this.setVisible(true);
        this.setResizable(false);

        setDefaultCloseOperation(3);
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();

        alX.add(x);
        alY.add(y);

        Graphics g = getGraphics();
        g.setColor(new Color((int)(Math.random() * 0x1000000))
        );
        g.fillRoundRect(x,y,50,50, 50, 50);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}