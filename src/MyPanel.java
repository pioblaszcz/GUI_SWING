import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

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
