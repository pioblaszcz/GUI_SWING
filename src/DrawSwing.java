import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class DrawSwing  extends JFrame {
    private String currentAppTitle = "Simple Draw";
    private String fileLoadedPath;
    JLabel modeLabel = new JLabel("Circle");
    JLabel statusLabel = new JLabel("New");
    MyPanel panel = new MyPanel(this.statusLabel);
    public static void main(String[] args) {
        new DrawSwing ();
    }
    public DrawSwing()
    {
        SwingUtilities.invokeLater(() -> this.createGUI());
    }
    protected void createGUI(){
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

                    if (fileChooser.showSaveDialog(DrawSwing.this) == JFileChooser.APPROVE_OPTION) {
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
                if (fileChooser.showSaveDialog(DrawSwing.this) == JFileChooser.APPROVE_OPTION) {
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
                if (fileChooser.showOpenDialog(DrawSwing.this) == JFileChooser.APPROVE_OPTION) {
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
        if (fileChooser.showSaveDialog(DrawSwing.this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".sge")) {
                file = new File(file.getParentFile(), file.getName() + ".sge");
            }
            panel.saveToFile(file);
            System.exit(0);
        }
    }
}
