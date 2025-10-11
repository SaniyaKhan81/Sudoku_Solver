import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class sudokusolvergui {
    private static final int SIZE = 9;
    private JFrame frame;
    private JTextField[][] cells;
    private int[][] board;
    private boolean[][] isOriginal;

    public sudokusolvergui() {
        frame = new JFrame("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE, 2, 2));
        cells = new JTextField[SIZE][SIZE];
        board = new int[SIZE][SIZE];
        isOriginal = new boolean[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new JTextField(1);
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                cells[i][j].setOpaque(true);
                cells[i][j].setBackground((i / 3 + j / 3) % 2 == 0 ? new Color(200, 220, 255) : new Color(255, 230, 200));
                
                final int row = i, col = j;
                cells[i][j].addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        int key = e.getKeyCode();
                        if (key == KeyEvent.VK_UP && row > 0) cells[row - 1][col].requestFocus();
                        else if (key == KeyEvent.VK_DOWN && row < SIZE - 1) cells[row + 1][col].requestFocus();
                        else if (key == KeyEvent.VK_LEFT && col > 0) cells[row][col - 1].requestFocus();
                        else if (key == KeyEvent.VK_RIGHT && col < SIZE - 1) cells[row][col + 1].requestFocus();
                    }
                });
                gridPanel.add(cells[i][j]);
            }
        }

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton solveButton = new JButton("Solve");
        JButton clearButton = new JButton("Clear");
        solveButton.setFont(new Font("Arial", Font.BOLD, 16));
        clearButton.setFont(new Font("Arial", Font.BOLD, 16));
        solveButton.setBackground(new Color(100, 200, 100));
        clearButton.setBackground(new Color(200, 100, 100));
        solveButton.setForeground(Color.WHITE);
        clearButton.setForeground(Color.WHITE);

        solveButton.addActionListener(e -> {
            if (readBoard()) {
                if (solveSudoku(board)) {
                    updateBoard();
                } else {
                    JOptionPane.showMessageDialog(frame, "No solution exists.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        clearButton.addActionListener(e -> clearBoard());

        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private boolean readBoard() {
        try {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    String text = cells[i][j].getText().trim();
                    if (text.isEmpty()) {
                        board[i][j] = 0;
                        isOriginal[i][j] = false;
                    } else {
                        board[i][j] = Integer.parseInt(text);
                        isOriginal[i][j] = true;
                        cells[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                    }
                }
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter only numbers between 1-9.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void updateBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j].setText(String.valueOf(board[i][j]));
                if (!isOriginal[i][j]) {
                    cells[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                }
                cells[i][j].setBackground((i / 3 + j / 3) % 2 == 0 ? new Color(200, 220, 255) : new Color(255, 230, 200));
            }
        }
    }
    
    private void clearBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j].setText("");
                cells[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                cells[i][j].setBackground((i / 3 + j / 3) % 2 == 0 ? new Color(200, 220, 255) : new Color(255, 230, 200));
            }
        }
    }

    private boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }
        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[boxRowStart + i][boxColStart + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        new sudokusolvergui();
    }
}