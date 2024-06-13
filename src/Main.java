import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Main extends JFrame {
    private String currentAppTitle = "Simple Draw";
    private String fileLoadedPath;
    JLabel modeLabel = new JLabel("Circle");
    JLabel statusLabel = new JLabel("New");
    MyPanel panel = new MyPanel(this.statusLabel);
    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        this.setMenuPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(this.currentAppTitle);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        this.add(createToolBar(), BorderLayout.PAGE_END);
        this.pack();
        this.setVisible(true);
        this.setResizable(false);

    }

    private void updateTitle() {
        this.setTitle(this.currentAppTitle);
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
        fileMenu.addSeparator();
        fileMenu.add(quitMenuItem);

        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Simple Graphics Editor Files", "sge");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getTitle().equals("Simple Draw")) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(filter);

                    if (fileChooser.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        if (!file.getName().toLowerCase().endsWith(".sge")) {
                            file = new File(file.getParentFile(), file.getName() + ".sge");
                        }
                        panel.saveToFile(file);
                        currentAppTitle = "Simple Draw: " + file.getName();
                        updateTitle();
                        fileLoadedPath = file.getAbsolutePath();
                    }
                }else{
                    File file = new File(fileLoadedPath);
                    panel.saveToFile(file);
                }
            }
        });

        saveAsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(filter);
                if (fileChooser.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    if (!file.getName().toLowerCase().endsWith(".sge")) {
                        file = new File(file.getParentFile(), file.getName() + ".sge");
                    }
                    panel.saveToFile(file);
                    currentAppTitle = "Simple Draw: " + file.getName();
                    updateTitle();
                    fileLoadedPath = file.getAbsolutePath();
                }
            }
        });

        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(filter);
                if (fileChooser.showOpenDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    currentAppTitle = "Simple Draw: " + file.getName();
                    updateTitle();
                    fileLoadedPath = file.getAbsolutePath();
                    panel.loadFromFile(file);
                }
            }
        });

        var thisHelper = this;
        quitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleExit();
            }
        });

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
        drawMenu.addSeparator();
        drawMenu.add(clearItem);

        menuBar.add(fileMenu);
        menuBar.add(drawMenu);


        this.setJMenuBar(menuBar);
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        toolBar.add(new JLabel("Mode: "));
        toolBar.add(this.modeLabel);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(this.statusLabel);

        return toolBar;
    }
    private void handleExit() {
        if (!panel.getIsSaved()) {
            int option = JOptionPane.showConfirmDialog(this,
                    "Do you want to save changes before quitting?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                saveFileAndExit();
            } else if (option == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
            // If option is JOptionPane.CANCEL_OPTION or dialog closed, do nothing (cancel exit)
        } else {
            System.exit(0);
        }
    }
    private void saveFileAndExit() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Simple Graphics Editor Files", "sge"));
        if (fileChooser.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".sge")) {
                file = new File(file.getParentFile(), file.getName() + ".sge");
            }
            panel.saveToFile(file);
            System.exit(0);
        }
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
    private boolean isSaved = false;
    private boolean isModified = false;
    private final JLabel statusLabel;

    public MyPanel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
        this.setPreferredSize(new Dimension(500, 400));
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.addMouseMotionListener(this);
        this.setFocusable(true);
        this.updateStatus();
    }
    private void updateStatus() {
        if (this.isModified) {
            statusLabel.setText("Modified");
        } else if(!this.isSaved){
            statusLabel.setText("New");
        }else{
            statusLabel.setText("Saved");
        }
    }

    public void setCurrentShape(String shape) {
        this.currentShape = shape;
    }

    public void setCursorColor() {
        this.cursorColor = JColorChooser.showDialog(this, "Choose color", Color.red);
    }
    public void saveToFile(File file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(shapes);
            out.writeObject(lines);
            this.isSaved = true;
            this.isModified = false;
            updateStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getIsSaved(){
        return this.isSaved;
    }
    public void loadFromFile(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            shapes = (ArrayList<Shape>) in.readObject();
            lines = (ArrayList<Line>) in.readObject();
            repaint();
            this.isSaved = true;
            this.isModified =false;
            updateStatus();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
            deleteMode = false;
            int response = JOptionPane.showConfirmDialog(this, "Cofirm shape deletion", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                isSaved = false;
                this.updateStatus();
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
            this.isSaved = false;
            this.isModified = true;
            this.updateStatus();
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
            this.isSaved = false;
            this.isModified = true;
            this.updateStatus();
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

abstract class Shape implements Serializable {
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

class Line implements Serializable {
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