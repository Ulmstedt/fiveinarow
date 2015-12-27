package fiveinarow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * The GameComponent class is a graphical representation of a Game object.
 *
 * @author Sehnsucht
 */
public class GameComponent extends JComponent implements GameListener, MouseListener {

    private final Game game;
    final private int width, height;

    /**
     *
     * @param game game that the component should display
     */
    public GameComponent(Game game) {
        this.game = game;
        this.width = Constants.SQUARE_SIZE * game.getWidth() + Constants.LINE_THICKNESS;
        this.height = Constants.SQUARE_SIZE * game.getHeight() + Constants.PADDING_TOP + Constants.LINE_THICKNESS;
        addMouseListener(this);
        addKeyBindings();
        ColorList.initColors();
    }

    private void addKeyBindings() {
        //Left
        this.getActionMap().put("leftPressed", pressedLeftAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("pressed LEFT"), "leftPressed");
        this.getActionMap().put("leftReleased", releasedLeftAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("released LEFT"), "leftReleased");
        //Right
        this.getActionMap().put("rightPressed", pressedRightAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("pressed RIGHT"), "rightPressed");
        this.getActionMap().put("rightReleased", releasedRightAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("released RIGHT"), "rightReleased");
        //Up
        this.getActionMap().put("upPressed", upAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "upPressed");
        //Down
        this.getActionMap().put("downPressed", downAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "downPressed");
        //Escape
        this.getActionMap().put("escapePressed", escapeAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "escapePressed");
        //Space
        this.getActionMap().put("spacePressed", spaceBarPressedAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("pressed SPACE"), "spacePressed");
        this.getActionMap().put("spaceReleased", spaceBarReleasedAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("released SPACE"), "spaceReleased");
        //Enter (same function as for space)
        this.getInputMap().put(KeyStroke.getKeyStroke("pressed ENTER"), "spacePressed");
        this.getInputMap().put(KeyStroke.getKeyStroke("released ENTER"), "spaceReleased");
    }

    // --------------------------------
    // All the actions for the keybinds
    // --------------------------------
    Action pressedLeftAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    Action releasedLeftAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    Action pressedRightAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    Action releasedRightAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    Action upAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    Action downAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    Action escapeAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    Action spaceBarPressedAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    Action spaceBarReleasedAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int currentPlayer = game.getCurrentPlayer().getID();

        //Draw whos turn it is
        g2d.setFont(new Font("Serif", Font.BOLD, 14));
        g2d.setColor(ColorList.colors.get(currentPlayer - 1));
        g2d.drawString("Player " + currentPlayer + "'s turn", 3, 14);
        //Draw round number
        g2d.setColor(Color.BLACK);
        g2d.drawString("Round: " + game.getRoundCount(), width/2 - 30, 14);
        //Draw scores
        //Player 1
        g2d.setColor(ColorList.colors.get(0));
        g2d.drawString("Player 1: " + game.getPlayerList().get(0).getScore(), width - 160, 14);
        //Player 2
        g2d.setColor(ColorList.colors.get(1));
        g2d.drawString("Player 1: " + game.getPlayerList().get(1).getScore(), width - 80, 14);

        //Draw square lines
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < width; i += Constants.SQUARE_SIZE) {
            g2d.fillRect(i, Constants.PADDING_TOP, Constants.LINE_THICKNESS, height - Constants.PADDING_TOP);
        }
        for (int i = 0; i < height; i += Constants.SQUARE_SIZE) {
            g2d.fillRect(0, i + Constants.PADDING_TOP, width, Constants.LINE_THICKNESS);
        }

        int[][] AIScoreGrid = game.getPlayerList().get(1).getPointGrid();

        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = 0; x < game.getWidth(); x++) {
                for (int y = 0; y < game.getHeight(); y++) {
                    if (game.getTile(x, y) == p) {
                        g2d.setColor(ColorList.colors.get(p % ColorList.colors.size() - 1));
                        g2d.setFont(new Font("Serif", Font.BOLD, 50));
                        g2d.drawString("" + p, Constants.SQUARE_SIZE / 2 + Constants.SQUARE_SIZE * x - 10, Constants.SQUARE_SIZE / 2 + Constants.SQUARE_SIZE * y + 15 + Constants.PADDING_TOP);
                    }
                    //Draw AI's  score grid (for debugging)
//                    g2d.setColor(Color.GRAY);
//                    g2d.setFont(new Font("Serif", Font.BOLD, 20));
//                    g2d.drawString("" + AIScoreGrid[x][y], Constants.SQUARE_SIZE / 2 + Constants.SQUARE_SIZE * x - 32, Constants.SQUARE_SIZE / 2 + Constants.SQUARE_SIZE * y + Constants.PADDING_TOP - 17);
                }
            }
        }

        int winner = game.getWinner();
        if (winner != 0) {
            g2d.setColor(new Color(0f, 0f, 0f, 0.7f));
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(ColorList.colors.get(winner - 1));
            g2d.setFont(new Font("Serif", Font.BOLD, 50));
            g2d.drawString("Player " + winner + " wins!", width / 2 - 130, height / 2 - 10);
        }
    }

    //Called when GameListeners are notified of a change
    @Override
    public void gameChanged() {
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!game.getPointsGiven()) {
            if (!SwingUtilities.isRightMouseButton(e)) {
                //game.setTile(e.getX() / Constants.SQUARE_SIZE, e.getY() / Constants.SQUARE_SIZE, 1);
                Player currentPlayer = game.getCurrentPlayer();
                if (!(currentPlayer instanceof AIPlayer)) {
                    currentPlayer.setCurrentChoice(e.getX() / Constants.SQUARE_SIZE, (e.getY() - Constants.PADDING_TOP) / Constants.SQUARE_SIZE);
                    //System.out.println("XX: " + e.getX() / Constants.SQUARE_SIZE + " YY: " + (e.getY() - Constants.PADDING_TOP) / Constants.SQUARE_SIZE);
                    currentPlayer.playRound();
                }
            } else {
                game.setTile(e.getX() / Constants.SQUARE_SIZE, (e.getY() - Constants.PADDING_TOP) / Constants.SQUARE_SIZE, 0);
            }
        } else {
            game.resetGame();
        }
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
