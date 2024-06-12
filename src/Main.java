import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main extends JFrame {
    MyPanel panel = new MyPanel();
    JLabel modeLabel = new JLabel("Circle");
    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        this.setMenuPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Simple draw");
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        this.add(createToolBar(), BorderLayout.PAGE_END);
        this.pack();
        this.setVisible(true);
        this.setResizable(false);

    }

    private void setMenuPanel(){
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem= new JMenuItem("Save");
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        JMenuItem quitMenuItem = new JMenuItem("Quit");

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(quitMenuItem);

//        saveMenuItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                saveToFile();
//            }
//        });
//
//        saveAsMenuItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                saveAsToFile();
//            }
//        });
//
//        openMenuItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                openFile();
//            }
//        });

        JMenu drawMenu = new JMenu("Draw");

        JMenu figureMenuInner = new JMenu("Figure");
        JMenuItem circleItem = new JCheckBoxMenuItem("Circle");
        JMenuItem sqItem = new JCheckBoxMenuItem("Square");
        JMenuItem penItem = new JCheckBoxMenuItem("Pen");

        circleItem.setSelected(true);

        circleItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        sqItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        penItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));

        circleItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                circleItem.setSelected(true);
                sqItem.setSelected(false);
                penItem.setSelected(false);
                panel.setCurrentShape("Circle");
                modeLabel.setText("Circle");
            }
        });

        sqItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                circleItem.setSelected(false);
                sqItem.setSelected(true);
                penItem.setSelected(false);
                panel.setCurrentShape("Square");
                modeLabel.setText("Square");
            }
        });

        penItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                circleItem.setSelected(false);
                sqItem.setSelected(false);
                penItem.setSelected(true);
                panel.setCurrentShape("Pen");
                modeLabel.setText("Pen");
            }
        });

        figureMenuInner.add(circleItem);
        figureMenuInner.add(sqItem);
        figureMenuInner.add(penItem);

        JMenuItem colorItem = new JMenuItem("Color");
        JMenuItem clearItem = new JMenuItem("Clear");

        colorItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setCursorColor();
            }
        });

        clearItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.clearPanel();
            }
        });

        colorItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        clearItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));

        drawMenu.add(figureMenuInner);
        drawMenu.add(colorItem);
        drawMenu.add(clearItem);

        menuBar.add(fileMenu);
        menuBar.add(drawMenu);


        this.setJMenuBar(menuBar);
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        toolBar.add(new JLabel("Mode: "));
        toolBar.add(modeLabel);

        return toolBar;
    }
}

class MyPanel extends JPanel implements MouseListener, KeyListener, MouseMotionListener {
    private int x, y;
    private int prevX, prevY;
    private Color cursorColor = Color.red;
    private String currentShape = "Circle";
    private ArrayList<Shape> shapes = new ArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();
    private boolean deleteMode = false;

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

    public void setCursorColor() {
        this.cursorColor = JColorChooser.showDialog(this, "Choose color", Color.red);
    }

    private void drawCircle(int x, int y) {
        int radius = 25;
        Circle circle = new Circle(x, y, radius);
        shapes.add(circle);
        repaint();
    }

    private void drawSquare(int x, int y) {
        int side = 50;
        Square square = new Square(x, y, side);
        shapes.add(square);
        repaint();
    }

    private void drawPen(int px, int py, int x, int y) {
        Line line = new Line(px, py, x, y, this.cursorColor);
        lines.add(line);
        repaint();
    }

    public void clearPanel() {
        shapes.clear();
        lines.clear();
        repaint();
    }

    private void deleteShapesAt(int x, int y) {
        ArrayList<Shape> shapesToRemove = new ArrayList<>();
        for (Shape shape : shapes) {
            if (shape.contains(x, y)) {
                shapesToRemove.add(shape);
            }
        }

        if (!shapesToRemove.isEmpty()) {
            int response = JOptionPane.showConfirmDialog(this, "Cofirm shape deletion", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                shapes.removeAll(shapesToRemove);
                repaint();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape shape : shapes) {
            shape.draw(g);
        }
        for (Line line : lines) {
            line.draw(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
        if (deleteMode) {
            deleteShapesAt(x, y);
        } else if (currentShape.equals("Pen")) {
            this.prevX = x;
            this.prevY = y;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
        if (currentShape.equals("Pen") && !deleteMode) {
            drawPen(this.prevX, this.prevY, this.x, this.y);
            this.prevX = this.x;
            this.prevY = this.y;
        }
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
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            deleteMode = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            deleteMode = false;
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
}

abstract class Shape {
    public abstract void draw(Graphics g);
    public abstract boolean contains(int x, int y);
}

class Circle extends Shape {
    private int x, y, radius;
    private Color color;

    public Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = new Color((int)(Math.random() * 0x1000000));
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public boolean contains(int x, int y) {
        double dx = this.x - x;
        double dy = this.y - y;
        return dx * dx + dy * dy <= radius * radius;
    }
}

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

class Line {
    int x1, y1, x2, y2;
    Color color;

    public Line(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }
}