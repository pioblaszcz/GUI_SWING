import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main extends JFrame {
    MyPanel panel = new MyPanel();
    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        this.setMenuPanel();
        this.setDefaultCloseOperation(3);
        this.setTitle("Simple draw");
        this.setLocation(50,50);
        this.setContentPane(this.panel);
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
    }

    private void setMenuPanel(){
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem= new JMenuItem("Save");

        JMenu drawMenu = new JMenu("Draw");

        JMenu figureMenuInner = new JMenu("Figure");
        JMenuItem circleItem = new JCheckBoxMenuItem("Circle");
        JMenuItem sqItem = new JCheckBoxMenuItem("Square");
        JMenuItem penItem = new JCheckBoxMenuItem("Pen");

        circleItem.setSelected(true);

        circleItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        sqItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        penItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));

        circleItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                circleItem.setSelected(true);
                sqItem.setSelected(false);
                penItem.setSelected(false);
                panel.setCurrentShape("Circle");
            }
        });

        sqItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                circleItem.setSelected(false);
                sqItem.setSelected(true);
                penItem.setSelected(false);
                panel.setCurrentShape("Square");
            }
        });

        penItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                circleItem.setSelected(false);
                sqItem.setSelected(false);
                penItem.setSelected(true);
                panel.setCurrentShape("Pen");
            }
        });

        figureMenuInner.add(circleItem);
        figureMenuInner.add(sqItem);
        figureMenuInner.add(penItem);

        JMenuItem colorItem = new JMenuItem("Color");
        JMenuItem clearItem = new JMenuItem("Clear");

        fileMenu.add(newMenuItem);
        drawMenu.add(figureMenuInner);
        drawMenu.add(colorItem);
        drawMenu.add(clearItem);

        menuBar.add(fileMenu);
        menuBar.add(drawMenu);


        this.setJMenuBar(menuBar);

        JToolBar toolBar = new JToolBar();
        JLabel statusLabel = new JLabel("Circle");

        this.add(toolBar);
    }
}

class MyPanel  extends JPanel implements MouseListener, KeyListener, MouseMotionListener{
    private int x,y;
    private int prevX, prevY;
    private String currentShape = "Circle";
    public MyPanel() {
        this.setPreferredSize(new Dimension(500, 400));
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.addMouseMotionListener(this);
        this.setFocusable(true);
    }
    public void setCurrentShape(String shape) {
        this.currentShape = shape;
    }
    private void drawCircle(int x, int y) {
        Graphics g = getGraphics();
        int radius = 25;
        g.setColor(new Color((int)(Math.random() * 0x1000000))
        );
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    private void drawSquare(int x, int y) {
        Graphics g = getGraphics();
        int side = 50;
        g.setColor(new Color((int)(Math.random() * 0x1000000))
        );
        g.fillRect(x - side / 2, y - side / 2, side, side);
    }

    private void drawPen(int px,int py,int x, int y) {
        Graphics g = getGraphics();
        g.setColor(Color.red);
        g.drawLine(px, py, x, y);
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            if (currentShape.equals("Circle")) {
                drawCircle(this.x, this.y);
            } else if (currentShape.equals("Square")) {
                drawSquare(this.x, this.y);
            }
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if (currentShape.equals("Pen")) {
            this.prevX = e.getX();
            this.prevY = e.getY();
        }
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if (currentShape.equals("Pen")) {
            this.x = e.getX();
            this.y = e.getY();
            drawPen(this.prevX, this.prevY, this.x, this.y);
            this.prevX = this.x;
            this.prevY = this.y;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}