package fiveinarow;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Sehnsucht
 */
public class Game {

    private final ArrayList<GameListener> gameListeners;
    private final ArrayList<Player> playerList;
    private Player currentPlayer;
    private WinnerHistory winnerHistory;

    private int width, height;
    private int winner, roundCount;
    private boolean pointsGiven;
    private int playerStarted = 0;

    //Used for random colors on the board
    private Color[][] colors;

    private int[][] board;

    public final int DEBUG_LEVEL = 2; // 0 = off, 1 = show heatmap, 2 = show heatmap + scores

    public Game(int width, int height) {
        this.gameListeners = new ArrayList<>();
        this.playerList = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.winner = 0;
        this.winnerHistory = new WinnerHistory(50);
        initGame();
    }

    //Update game by 1 tick
    public void tick() {
        if (winner == 0) {
            if (isBoardFull()) {
                // Notify all players that the round has ended before resetting the game.
                for (IPlayer p : playerList) {
                    p.roundEnded(winner);
                }

                //resetGame(); //automatically start new game after someone wins (for fast ai vs ai games)
            }
            winner = checkForWinner(board);
            if (currentPlayer instanceof IAI && winner == 0) {
                currentPlayer.playRound();
            }
            notifyListeners();
        } else {
            if (!pointsGiven) {
                playerList.get(winner - 1).givePoint();
                winnerHistory.saveWinner(winner);
                pointsGiven = true;

                // Notify all players that the round has ended before resetting the game.
                for (IPlayer p : playerList) {
                    p.roundEnded(winner);
                }

                resetGame();
            }
            winner = 0;

        }
    }

    private void initGame() {
        this.board = new int[width][height];
        //this.board = AIPlayer.invertMatrix(AIPlayer.setupDiaAt55);

        playerList.add(new Player(1, this));
        //playerList.add(new Player(2, this));
        //playerList.add(new AIPlayer(1, this));
        //playerList.add(new AILoki(1, this));
        playerList.add(new AIPlayer(2, this));
        //playerList.add(new AILoki(2, this));
        //playerList.add(new AIJohan(2, this));
        //playerList.add(new AIPlayer(3, this));

        playerStarted = 0;
        currentPlayer = playerList.get(playerStarted);
        colors = generateRandomColors(); //Random colors
    }

    public void resetGame() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board[i][j] = 0;
            }
        }
        winner = 0;
        pointsGiven = false;
        roundCount = 0;
        colors = generateRandomColors(); //Random colors

        // Switch players.
        playerStarted++;
        if (playerStarted == playerList.size()) {
            playerStarted = 0;
        }
        currentPlayer = playerList.get(playerStarted);
    }

    private Color[][] generateRandomColors() {
        Color[][] color = new Color[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //color[i][j] = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)); //Random colors
                color[i][j] = (Math.random() >= 0.5 ? Color.BLUE : Color.RED); //Random red/blue
            }
        }
        return color;
    }

    public void nextPlayer() {
        int currID = currentPlayer.getID();
        if (currID != playerList.size()) {
            currentPlayer = playerList.get(currID);
        } else {
            currentPlayer = playerList.get(0);
        }
    }

    public boolean isBoardFull() {
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addGameListener(GameListener gl) {
        gameListeners.add(gl);
    }

    /*
     Checks if a player has won.
     Returns 0 if no winner, or the player ID if someone has won.
     */
    public int checkForWinner(int[][] tiles) {
        for (int p = 1; p <= getNumberOfPlayers(); p++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    //Check rows
                    if (x <= width - 5 && (tiles[x][y] == p && tiles[x + 1][y] == p && tiles[x + 2][y] == p && tiles[x + 3][y] == p && tiles[x + 4][y] == p)) {
                        return p;
                    }
                    //Check columns
                    if (y <= height - 5 && (tiles[x][y] == p && tiles[x][y + 1] == p && tiles[x][y + 2] == p && tiles[x][y + 3] == p && tiles[x][y + 4] == p)) {
                        return p;
                    }
                    //Check diagonals
                    if (x >= 2 && x <= width - 3 && y >= 2 && y <= height - 3
                            && ((tiles[x - 2][y - 2] == p && tiles[x - 1][y - 1] == p && tiles[x][y] == p && tiles[x + 1][y + 1] == p && tiles[x + 2][y + 2] == p)
                            || (tiles[x + 2][y - 2] == p && tiles[x + 1][y - 1] == p && tiles[x][y] == p && tiles[x - 1][y + 1] == p && tiles[x - 2][y + 2] == p))) {
                        return p;
                    }
                }
            }
        }
        return 0;
    }

    //Notify all listeners that the game has changed
    private void notifyListeners() {
        for (GameListener gl : gameListeners) {
            gl.gameChanged();
        }
    }

    // #############
    // ## Getters ##
    // #############
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getNumberOfPlayers() {
        return playerList.size();
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getWinner() {
        return winner;
    }

    public int getTile(int x, int y) {
        return board[x][y];
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean getPointsGiven() {
        return pointsGiven;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public Color[][] getColors() {
        return colors;
    }

    public WinnerHistory getWinnerHistory() {
        return winnerHistory;
    }

    // #############
    // ## Setters ##
    // #############
    public void setTile(int x, int y, int value) {
        board[x][y] = value;

        // Notify all players that a move has been made.
        for (IPlayer p : playerList) {
            p.moveMade(new Point(x, y));
        }
    }

    public void incrementRoundCount() {
        roundCount++;
    }
}
