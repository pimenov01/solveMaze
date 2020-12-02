package view;

import controller.Controller;
import entities.Cell;
import entities.MazeView;
import entities.TypeOfCell;
import generator.Direction;
import javafx.util.Pair;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class MenuFrame extends JFrame {

    private JPanel panel;
    private JTextArea sizeArea;
    private JTextArea seedArea;
    private Controller controller;
    private JPanel[][] cells;
    private Pair<Integer, Integer> exit;
    private Pair<Integer, Integer> entrance;
    private boolean haveHole;
    private Cell[][] field;
    private String[] typeOfCell;


    public MenuFrame() {
        createMenuPanel();

        JMenu exitMenu = new JMenu("Exit");
        JMenuItem sure = new JMenuItem("Are you sure?");
        JMenuItem yes = new JMenuItem("YES");
        JMenuItem no = new JMenuItem("NO");
        exitMenu.add(sure);
        exitMenu.add(yes);
        exitMenu.add(no);
        sure.setEnabled(false);
        yes.addActionListener(actionEvent -> System.exit(0));

        JMenuBar bar = new JMenuBar();
        bar.add(exitMenu);
        bar.setBackground(new Color(255, 255, 255));
        bar.setBorder(BorderFactory.createLineBorder(new Color(0xFFFFFF), 1));

        this.setSize(500, 500);
        this.setContentPane(panel);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setJMenuBar(bar);
        this.setBackground(new Color(0x000000));
        this.setVisible(true);

    }

    public void createMenuPanel() {
        controller = new Controller();
        JButton startButton = new JButton("Press me");
        JLabel label = new JLabel("Welcome to THEMAZE");
        label.setFont(new Font("Arial", Font.BOLD, 40));
        panel = new JPanel();
        seedArea = new JTextArea("Seed");
        seedArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seedArea.setText("");
            }
        });
        sizeArea = new JTextArea("Size \n(number in 15..200)");
        sizeArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sizeArea.setText("");
            }
        });

        panel.setLayout(null);
        panel.add(sizeArea);
        panel.add(startButton);
        panel.add(seedArea);
        panel.add(label);
        startButton.setBounds(150,250, 180, 160);
        sizeArea.setBounds(150, 195, 180, 40);
        sizeArea.setBorder(new BevelBorder(BevelBorder.LOWERED));
        seedArea.setBounds(150, 165, 180, 20);
        seedArea.setBorder(new BevelBorder(BevelBorder.LOWERED));
        label.setBounds(35, 15, 2500, 180);

        startButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        startButton.setFont(new Font("Arial", Font.BOLD, 38));
        startButton.setForeground(new Color(205, 205, 205));
        startButton.setBackground(new Color(26, 11, 31));
        sizeArea.setBackground(new Color(21, 9, 16));
        sizeArea.setForeground(new Color(205, 205, 205));
        seedArea.setBackground(new Color(21, 9, 16));
        seedArea.setForeground(new Color(205, 205, 205));
        label.setForeground(new Color(205, 205, 205));
        panel.setBackground(new Color(26, 11, 31, 255));


        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    String s = sizeArea.getText();
                    int dim;
                    if (s.matches("\\d{2,3}")) {
                        dim = Integer.parseInt(s);
                        if (dim < 15 || dim > 200) {
                            sizeArea.setText("Size should be 15-200");
                            return;
                        } else {
                            if (dim % 2 == 0) {
                                dim++;
                            }
                        }
                    } else {
                        sizeArea.setText("Illegal format");
                        return;
                    }
                    controller.setHeight(dim);
                    controller.setWidth(dim);
                    if (seedArea.getText().equals("Illegal format!") || seedArea.getText().equals("Enter seed here")) {
                        controller.setSeed(System.currentTimeMillis());
                    } else {
                        try {
                            controller.setSeed(Long.parseLong(seedArea.getText()));
                        } catch (NumberFormatException ex) {
                            seedArea.setText("Illegal format!");
                        }
                    }
                    try {
                        createMazePanel(controller.start());
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
        });
        this.getContentPane().requestFocus();
    }

    public void createMazePanel(MazeView mazeView) {
        int rows = mazeView.getHeight();
        int cols = mazeView.getWidth();
        typeOfCell = mazeView.getField();
        entrance = null;
        exit = null;
        cells = new JPanel[rows][cols];
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, cols));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new JPanel();
                switch (typeOfCell[i].charAt(j)) {
                    case '+':
                        cells[i][j].setBackground(Color.BLACK);
                        break;

                    case '.':
                        cells[i][j].setBackground(Color.WHITE);
                        haveHole = true;
                        break;

                    case '0':
                        cells[i][j].setBackground(Color.GREEN);
                        break;

                    case 'X': {
                        cells[i][j].setBackground(Color.RED);
                        if (exit == null)
                            exit = new Pair<>(i, j);
                        else {
                            throw new IllegalArgumentException("There are two exits");
                        }
                    }
                    break;
                    case '*': {
                        cells[i][j].setBackground(Color.MAGENTA);
                        if (entrance == null)
                            entrance = new Pair<>(i, j);
                        else {
                            throw new IllegalArgumentException("There are two entrances");
                        }
                    }
                    break;
                }
                panel.add(cells[i][j]);
            }
        }
        int multiplier = 100;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        while (cols * multiplier > screenSize.getHeight() * 0.9) {
            multiplier--;
        }
        this.setSize(new Dimension(cols * multiplier + 16, rows * multiplier + 60));
        this.setBackground(new Color(0x000000));
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel.setFocusable(true);
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int x = entrance.getValue();
                int y = entrance.getKey();
                int tempX = x;
                int tempY = y;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER: {
                        field = new Cell[cells.length][cells.length];
                        for (int i = 0; i < cells.length; i++) {
                            for (int j = 0; j < cells.length; j++) {
                                switch (typeOfCell[i].charAt(j)) {
                                    case '+':
                                        field[i][j] = new Cell(i, j, TypeOfCell.WALL);
                                        break;

                                    case '.':
                                        field[i][j] = new Cell(i, j, TypeOfCell.HOLE);
                                        break;

                                    case '0':
                                        field[i][j] = new Cell(i, j, TypeOfCell.EMPTY_CELL);
                                        break;

                                    case 'X':
                                        field[i][j] = new Cell(i, j, TypeOfCell.EXIT);
                                        break;

                                    case '*':
                                        field[i][j] = new Cell(i, j, TypeOfCell.ENTRANCE);
                                        break;
                                }
                            }
                        }
                        solveMaze();
                    }
                    break;
                    case KeyEvent.VK_UP: {
                        entrance = new Pair<>(y - 1, x);
                        y = entrance.getKey();
                    }
                    break;
                    case KeyEvent.VK_DOWN: {
                        entrance = new Pair<>(y + 1, x);
                        y = entrance.getKey();
                    }
                    break;
                    case KeyEvent.VK_LEFT: {
                        entrance = new Pair<>(y, x - 1);
                        x = entrance.getValue();
                    }
                    break;
                    case KeyEvent.VK_RIGHT: {
                        entrance = new Pair<>(y, x + 1);
                        x = entrance.getValue();
                    }
                    break;
                }
                if (y < rows && y >= 0 && x < cols && x >= 0 && !cells[y][x].getBackground().equals(Color.BLACK)) {
                    cells[tempY][tempX].setBackground(new Color(196, 158, 65, 255));
                    cells[y][x].setBackground(Color.MAGENTA);
                } else {
                    y = tempY;
                    x = tempX;
                    entrance = new Pair<>(y, x);
                }
                if (exit.getKey().equals(entrance.getKey()) && exit.getValue().equals(entrance.getValue())) {
                    int result = JOptionPane.showConfirmDialog(null, "You win!\n EXIT?", "YOU WIN!", JOptionPane.YES_NO_OPTION);
                    if (result != JOptionPane.YES_OPTION) {
                        int incr = 0;
                        while (result != JOptionPane.YES_OPTION && incr <= 5) {
                            incr++;
                            switch (incr) {
                                case 1:
                                    result = JOptionPane.showConfirmDialog(null, "Maybe yes?", "Rage counter is " + incr, JOptionPane.YES_NO_OPTION);
                                    break;
                                case 2:
                                    result = JOptionPane.showConfirmDialog(null, "MAYBE YES?", "Rage counter is " + incr, JOptionPane.YES_NO_OPTION);
                                    break;
                                case 3:
                                    result = JOptionPane.showConfirmDialog(null, "Are you sure?", "Rage counter is " + incr, JOptionPane.YES_NO_OPTION);
                                    break;
                                case 4:
                                    result = JOptionPane.showConfirmDialog(null, "YOU REALLY WANNA PLAY THIS GAME?", "Rage counter is " + incr, JOptionPane.YES_NO_OPTION);
                                    break;
                                case 5:
                                    result = JOptionPane.showConfirmDialog(null, "HAHA\nVERY FUNNY", "HAHA VERY FUNNY", JOptionPane.YES_NO_OPTION);
                                    System.exit(0);
                            }
                        }
                    }
                    System.exit(0);
                }
            }
        });
        panel.setBackground(new Color(0xFFFFFF));
        this.setContentPane(panel);
        panel.requestFocus();
    }

    private void solveMaze() {
        Random random = new Random();
        Cell currentCell = field[entrance.getKey()][entrance.getValue()];
        Cell neighbourCell;
        Stack<Cell> stack = new Stack<>();
        do {
            List<Cell> neighbours = getNeighbours(currentCell);
            if (neighbours.size() != 0) {
                cells[currentCell.getX()][currentCell.getY()].setBackground(new Color(101, 163, 70));
                int randNum = random.nextInt(neighbours.size());
                neighbourCell = neighbours.get(randNum);
                stack.push(currentCell);
                currentCell = neighbourCell;
                currentCell.setVisited(true);
            } else if (stack.size() > 0) {
                cells[currentCell.getX()][currentCell.getY()].setBackground(new Color(255, 255, 255));
                //cells[currentCell.getX()][currentCell.getY()].setBackground(new Color(205, 113, 138)); show cells we`ve bypassed
                currentCell = stack.pop();
            } else {
                JOptionPane.showMessageDialog(null, "There is no exit in the maze");
                return;
            }
        }
        while (currentCell.getX() != exit.getKey() || currentCell.getY() != exit.getValue());
    }

    private List<Cell> getNeighbours(Cell cell) {
        List<Cell> neighbours = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (cell.getX() + direction.getCoords().getKey() > 0 &&
                    cell.getX() + direction.getCoords().getKey() < field.length &&
                    cell.getY() + direction.getCoords().getValue() > 0 &&
                    cell.getY() + direction.getCoords().getValue() < field.length) {
                Cell mazeCellCurrent = field[cell.getX() + direction.getCoords().getKey()][cell.getY() + direction.getCoords().getValue()];
                if ((mazeCellCurrent.toString().equals("X") || mazeCellCurrent.toString().equals(".")) && !mazeCellCurrent.isVisited()) {
                    neighbours.add(mazeCellCurrent); //add to array
                }
            }
        }
        return neighbours;
    }
}
