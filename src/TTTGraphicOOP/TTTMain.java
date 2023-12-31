package TTTGraphicOOP;

/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2023/2024
 * Group Capstone Project
 * Group #1
 * 1 - 5026221035 - Mufidhatul Nafisa
 * 2 - 5026221120 - M. Shalahuddin Arif Laksono
 * 3 - 5026221102 - Fernandio Farrel Putra S.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The TTT_Tutorial.Board and TTT_Tutorial.Cell classes are separated in their own classes.
 */
public class TTTMain extends JPanel {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the drawing graphics
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
//    public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Red #EF6950
//    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    // Define game objects
    private Board board;         // the game board
    private State currentState;  // the current state of the game
    private Seed currentPlayer;  // the current player
    private JLabel statusBar;    // for displaying status message

    /** Constructor to setup the UI and game components */
    public TTTMain() {

        // This JPanel fires MouseEvent
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        // Update cells[][] and return the new game state after the move
                        currentState = board.stepGame(currentPlayer, row, col);
                        // Switch player
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                } else {        // game over
                    newGame();  // restart the game
                }
                // Refresh the drawing canvas
                repaint();  // Callback paintComponent().
            }
        });


        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
//        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        // account for statusBar in height
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Set up Game
        initGame();
        newGame();
    }

    /** Initialize the game (run once) */
    public void initGame() {
        board = new Board();  // allocate the game-board
    }

    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED; // all cells empty
            }
        }
        currentPlayer = Seed.CROSS;    // cross plays first
        currentState = State.PLAYING;  // ready to play
    }

    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) {  // Callback via repaint()
        super.paintComponent(g);
        setBackground(COLOR_BG); // set its background color

        board.paint(g);  // ask the game board to paint itself

        // Print status-bar message
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "Player 1 Turn" : "Player 2 Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Player 1 Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("Player 2 Won! Click to play again.");
        }

        // Center align the text in the status bar
        statusBar.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void quitActionPerformed(ActionEvent evt){
        System.exit(0);
    }

    public void newGameActionPerformed(ActionEvent evt) {
        repaint();
        newGame();
    }

    public void chooseBg(ActionEvent e, int idx){
        board.loadImage(idx);
        repaint();
    }

    /** The entry "main" method */
    public static void main(String[] args) {
        // Run GUI construction codes in Event-Dispatching thread for thread safety
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //pop up menu
                JOptionPane.showMessageDialog(null, "Welcome! click OK to start game! Tic Tac Toe");

                JFrame frame = new JFrame(TITLE);

                //Create Menu Bar
                JMenuBar menubar = new JMenuBar();
                JMenu game = new JMenu("Game");
                JMenuItem newGame = new JMenuItem("New Game");
                JMenuItem quit = new JMenuItem("Quit");
                JMenu background = new JMenu("Background");
                JMenuItem bg1 = new JMenuItem("Snow");
                JMenuItem bg2 = new JMenuItem("Board");

                JMenuItem about = new JMenuItem("About");
                about.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Load the image
                        ImageIcon icon = new ImageIcon("group1.jpg"); // Ganti dengan path gambar sesuai kebutuhan

                        // Scale the image to fit the dialog
                        ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH));

                        // Create a panel to hold the image and text
                        JPanel panel = new JPanel(new BorderLayout());
                        JLabel imageLabel = new JLabel(scaledIcon);
                        imageLabel.setHorizontalAlignment(JLabel.CENTER);
                        JLabel textLabel = new JLabel("<html><center>Tic Tac Toe<br>Version 1.0<br>Created by Group 1<br>" +
                                "5026221035 - Mufidhatul Nafisa<br>" +
                                "5026221120 - M. Shalahuddin Arif Laksono<br>" +
                                "5026221102 - Fernandio Farrel Putra S.</center></html>");
                        textLabel.setHorizontalAlignment(JLabel.CENTER);
                        panel.add(imageLabel, BorderLayout.CENTER);
                        panel.add(textLabel, BorderLayout.SOUTH);

                        // Show the dialog with the image and text
                        JOptionPane.showMessageDialog(
                                null,
                                panel,
                                "About",
                                JOptionPane.PLAIN_MESSAGE
                        );
                    }
                });

                //Setup main JPanel
                TTTMain mainPanel= new TTTMain();
                Board board1 = new Board();

                // Set the content-pane of the JFrame to an instance of main JPanel
                frame.setContentPane(mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); // center the application window
                frame.setResizable(false);

                //Add to menubar
                menubar.add(game);
                game.add(newGame);
                game.add(quit);
                menubar.add(background);
                background.add(bg1);
                background.add(bg2);
                menubar.add(about);
                frame.setJMenuBar(menubar);

                newGame.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e) {
                        mainPanel.newGameActionPerformed(e);
                    }
                });

                quit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        mainPanel.quitActionPerformed(e);
                    }
                });

                bg1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainPanel.chooseBg(e, 0);
                    }
                });

                bg2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainPanel.chooseBg(e, 1);
                    }
                });
                frame.setVisible(true);            // show it
            }
        });
    }
}
