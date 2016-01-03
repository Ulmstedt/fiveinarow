package fiveinarow;

import Pzyber.Loki.Gomoku.Utils;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Sehnsucht
 */
public class AIPlayer extends Player implements IAI {

    static final int[][] knownProblem1 = new int[][]{
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    static final int[][] knownProblem2start = new int[][]{
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 1, 2, 2, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 2, 2, 1, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 2, 1, 2, 2, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 2, 1, 1, 2, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    static final int[][] knownProblem2end = new int[][]{
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 1, 2, 2, 2, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 2, 2, 1, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 2, 1, 2, 2, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 2, 1, 1, 2, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    static final int[][] knownProblem3start = new int[][]{
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 2, 1, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 2, 1, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 2, 0, 2, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    static final int[][] setupDiaAt55 = new int[][]{
        //0 1  2  3  4  5  6  7  8  9  0  1  2  3  4
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 0
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 1
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 2
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 3
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 4
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 5
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 6
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},// 7
        {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},// 8
        {0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 9
        {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},// 0
        {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0},// 1
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 2
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 3
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} // 4
    };
    static final int[][] setupDia2 = new int[][]{
        //0 1  2  3  4  5  6  7  8  9  0  1  2  3  4
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 0
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 1
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 2
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 3
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 4
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 5
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 6
        {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},// 7
        {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},// 8
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},// 9
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 0
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 1
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 2
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 3
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} // 4
    };
    static final int[][] setupRow = new int[][]{
        //0 1  2  3  4  5  6  7  8  9  0  1  2  3  4
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 0
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 1
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},// 2
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 3
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 4
        {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 5
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 6
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 7
        {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},// 8
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 9
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 0
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 1
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 2
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 3
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} // 4
    };
    static final int[][] setupCol = new int[][]{
        //0 1  2  3  4  5  6  7  8  9  0  1  2  3  4
        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 0
        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 1
        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 2
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 3
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 4
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},// 5
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},// 6
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},// 7
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},// 8
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 9
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 0
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 1
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 2
        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},// 3
        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} // 4
    };
    int checkX = 4, checkY = 13, checkLength = 2;

    private int width, height;

    //Defensive
    private final int WIN_SETUP_SCORE = 50000;
    private final int WIN_BLOCK_SCORE = 12000;
    private final int FOUR_SETUP_SCORE = 3000;
    private final int FOUR_BLOCK_SCORE = 2500;
    private final int THREE_SETUP_SCORE = 400;
    private final int THREE_BLOCK_SCORE = 250;
    private final int TWO_SETUP_SCORE = 20;
    private final int TWO_BLOCK_SCORE = 10;
    //Aggressive (bad numbers)
    /*private final int WIN_SETUP_SCORE = 5000;
     private final int WIN_BLOCK_SCORE = 1200;
     private final int FOUR_SETUP_SCORE = 400;
     private final int FOUR_BLOCK_SCORE = 250;
     private final int THREE_SETUP_SCORE = 45;
     private final int THREE_BLOCK_SCORE = 25;
     private final int TWO_SETUP_SCORE = 2;
     private final int TWO_BLOCK_SCORE = 1;*/

    private final int[] SETUP_SCORE = new int[]{0, 0, TWO_SETUP_SCORE, THREE_SETUP_SCORE, FOUR_SETUP_SCORE};
    private final int[] BLOCK_SCORE = new int[]{0, 0, TWO_BLOCK_SCORE, THREE_BLOCK_SCORE, FOUR_BLOCK_SCORE};

    private final double SIMULATION_INTENSITY = 0.5; //Lower value simulates more cases
    private final int SIMULATION_DEPTH = 9; //How many rounds in the future that is simulated

    public AIPlayer(int id, Game game) {
        super(id, game);
        width = game.getWidth();
        height = game.getHeight();
    }

    private int[][] newPointGrid(int width, int height) {
        int[][] newPointGrid = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newPointGrid[i][j] = (i <= width - i - 1 ? i : width - i - 1) + (j <= height - j - 1 ? j : height - j - 1);
            }
        }
        return newPointGrid;
    }

    @Override
    public void playRound() {
        //System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        //System.out.println("Setup " + (checkForSetupDiagonal1(AIPlayer.invertMatrix(AIPlayer.setupDiaAt55), checkX, checkY, checkLength, 1) ? "found" : "not found"));
        //System.out.println("Setup " + (checkForSetupDiagonal2(AIPlayer.invertMatrix(AIPlayer.setupDia2), checkX, checkY, checkLength, 1) ? "found" : "not found"));
        //System.out.println("Setup " + (checkForSetupRow(AIPlayer.invertMatrix(AIPlayer.setupRow), checkX, checkY, checkLength, 1) ? "found" : "not found"));
        //System.out.println("Setup " + (checkForSetupCol(AIPlayer.invertMatrix(AIPlayer.setupCol), checkX, checkY, checkLength, 1) ? "found" : "not found"));
        //System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        //System.out.println();

        System.out.println("###### START OF ROUND #######################");
        //Start time of AI's turn
        long startTime = System.currentTimeMillis();

        // -----------------------
//        System.out.println("---------");
//        System.out.println("Winner of test simulation: " + simulateGame(spinMatrix(AIPlayer.knownProblem2), ID, SIMULATION_DEPTH));
//        System.out.println("---------");
        // -----------------------
        int[][] gameBoard = Utils.cloneMatrix(game.getBoard());
        //int[][] gameBoard = spinMatrix(knownProblem1);

        pointGrid = calculatePointGrid(gameBoard, this.ID);
        Point p = findBestMove(pointGrid);
        currX = p.x;
        currY = p.y;
        //System.out.println("Heatmap says: (" + p.x + ", " + p.y + ")");
        int pointsToSimulate = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //Check if score is high enough, and if so simulate for playing (x,y), and only simulate if its atleast a 4 setup
                if (pointGrid[x][y] >= TWO_BLOCK_SCORE && pointGrid[x][y] >= (SIMULATION_INTENSITY * pointGrid[p.x][p.y])) {
                    pointsToSimulate++;
                }
            }
        }
        System.out.println("A maximum of " + pointsToSimulate + " points will be simulated.");
        for (int sim = 1; sim <= pointsToSimulate; sim++) {
            int[][] tempBoard = Utils.cloneMatrix(gameBoard);
            tempBoard[p.x][p.y] = this.ID;

            System.out.println("------ SIMULATION " + sim + ": (" + p.x + ", " + p.y + ") -----------------");
            int simulatedWinner = simulateGame(tempBoard, this.ID, SIMULATION_DEPTH);
            System.out.println("------ END OF SIMULATION --------------------");
            if (simulatedWinner == 0 || simulatedWinner == this.ID) {
                currX = p.x;
                currY = p.y;
                break;
            }
            System.out.println("Throwing away (" + p.x + ", " + p.y + ")");
            //Set score of current point to 0 since it would make us lose
            pointGrid[p.x][p.y] = 0;
            p = findBestMove(pointGrid);
        }

        System.out.print("AI played (" + currX + ", " + currY + "), taking ");
        if (game.getTile(currX, currY) == 0) {
            game.setTile(currX, currY, ID);
            game.incrementRoundCount();
            game.nextPlayer();
        }

        //End time of AI's turn
        long endTime = System.currentTimeMillis();
        long timeSpent = endTime - startTime;
        System.out.println(timeSpent + " ms.");

        //For debugging
        pointGrid = calculatePointGrid(game.getBoard(), 1);
        System.out.println("###### END OF ROUND #########################");
    }

    private int simulateGame(int[][] gameBoard, int lastID, int roundsLeft) {
        int winner = game.checkForWinner(gameBoard);
        if (winner != 0) {
            System.out.println("Simulated winner: P" + winner);
            return winner;
        } else if (roundsLeft > 0) {
            //Calculate who starts next turn
            int currentID;
            ArrayList<Player> pl = game.getPlayerList();
            if (lastID != pl.size()) {
                currentID = pl.get(lastID).getID();
            } else {
                currentID = pl.get(0).getID();
            }
            int[][] boardCopy = Utils.cloneMatrix(gameBoard);
            int[][] pGrid = calculatePointGrid(boardCopy, currentID);
            Point p = findBestMove(pGrid);
            System.out.println("P" + currentID + " played (" + p.x + ", " + p.y + ") - " + (roundsLeft - 1) + " rounds left to simulate.");
            boardCopy[p.x][p.y] = currentID;
            return simulateGame(boardCopy, currentID, roundsLeft - 1);
        } else {
            return 0;
        }
    }

    private Point findBestMove(int[][] grid) {
        int bestX = 0, bestY = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j] > grid[bestX][bestY]) {
                    bestX = i;
                    bestY = j;
                }
            }
        }
        return new Point(bestX, bestY);
    }

    private int[][] calculatePointGrid(int[][] board, int playerID) {
        int[][] tempPointGrid = newPointGrid(width, height);
        int[][] tempBoard = Utils.cloneMatrix(board); //Copy board
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = 0; x < game.getWidth(); x++) {
                for (int y = 0; y < game.getHeight(); y++) {
                    if (board[x][y] == 0) {
                        tempBoard[x][y] = p;
                        //Check if placement would give victory
                        if (game.checkForWinner(tempBoard) == p) {
                            tempPointGrid[x][y] += (p == playerID ? WIN_SETUP_SCORE : WIN_BLOCK_SCORE);
                        }
                        //Check for setups of length 2-4 and give points accordingly
                        for (int length = 2; length <= 4; length++) {
                            if (checkForSetupRow(tempBoard, x, y, length, p)) {
                                tempPointGrid[x][y] += (p == playerID ? SETUP_SCORE[length] : BLOCK_SCORE[length]);
                            }
                            if (checkForSetupCol(tempBoard, x, y, length, p)) {
                                tempPointGrid[x][y] += (p == playerID ? SETUP_SCORE[length] : BLOCK_SCORE[length]);
                            }
                            if (checkForSetupDiagonal1(tempBoard, x, y, length, p)) {
                                tempPointGrid[x][y] += (p == playerID ? SETUP_SCORE[length] : BLOCK_SCORE[length]);
                            }
                            if (checkForSetupDiagonal2(tempBoard, x, y, length, p)) {
                                tempPointGrid[x][y] += (p == playerID ? SETUP_SCORE[length] : BLOCK_SCORE[length]);
                            }
                        }
                        tempBoard[x][y] = 0;

                    } else {
                        tempPointGrid[x][y] = 0;
                    }
                }
            }
        }
        return tempPointGrid;
    }

    // ##################
    // ## Setup checks ##
    // ##################
    /**
     * Checks for setups horizontally.
     *
     * @param tiles Board to check setup on
     * @param xc X to check
     * @param yc Y to check
     * @param length Length of chain to look for
     * @param ID Player to check for
     * @return Returns true if setup is found for player [ID]
     */
    public boolean checkForSetupRow(int[][] tiles, int xc, int yc, int length, int ID) {
        int lowestX, highestX, lowest, highest;
        //Find lower loop bound
        lowestX = (xc - (length - 1) > 0 ? xc - (length - 1) : 0);
        lowest = lowestX - xc;
        //Find upper loop bound
        highestX = (xc < (tiles.length - length) ? xc : (tiles.length - length));
        highest = highestX - xc;

        Outer:
        for (int dx = lowest; dx <= highest; dx++) {
            for (int i = 0; i < length; i++) {
                if (tiles[xc + dx + i][yc] != ID) {
                    continue Outer;
                }
            }
            //If this is reached, a setup has been found and true is returned
            return true;
        }
        //If this is reached, no setup has been found and false is returned
        return false;
    }

    /**
     * Checks for setups vertically.
     *
     * @param tiles Board to check setup on
     * @param xc X to check
     * @param yc Y to check
     * @param length Length of chain to look for
     * @param ID Player to check for
     * @return Returns true if setup is found for player [ID]
     */
    public boolean checkForSetupCol(int[][] tiles, int xc, int yc, int length, int ID) {
        int lowestY, highestY, lowest, highest;
        //Find lower loop bound
        lowestY = (yc - (length - 1) > 0 ? yc - (length - 1) : 0);
        lowest = lowestY - yc;
        //Find upper loop bound
        highestY = (yc < (tiles[0].length - length) ? yc : (tiles[0].length - length));
        highest = highestY - yc;

        Outer:
        for (int dy = lowest; dy <= highest; dy++) {
            for (int i = 0; i < length; i++) {
                if (tiles[xc][yc + dy + i] != ID) {
                    continue Outer;
                }
            }
            //If this is reached, a setup has been found and true is returned
            return true;
        }
        //If this is reached, no setup has been found and false is returned
        return false;
    }

    /**
     * Checks for setups diagonally ( \ ).
     *
     * @param tiles Board to check setup on
     * @param xc X to check
     * @param yc Y to check
     * @param length Length of chain to look for
     * @param ID Player to check for
     * @return Returns true if setup is found for player [ID]
     */
    public boolean checkForSetupDiagonal1(int[][] tiles, int xc, int yc, int length, int ID) {
        int lowestX, highestX, lowestY, highestY, lowest, highest;
        //Find lower loop bound
        lowestX = (xc - (length - 1) > 0 ? xc - (length - 1) : 0);
        lowestY = (yc - (length - 1) > 0 ? yc - (length - 1) : 0);
        int lowestXOffset = lowestX - xc; //xc - lowestX
        int lowestYOffset = lowestY - yc; // yc - lowestY
        lowest = (lowestXOffset > lowestYOffset ? lowestXOffset : lowestYOffset); // Lowest bound = highest of the mins // >
        //Find upper loop bound
        highestX = (xc < (tiles.length - length) ? xc : (tiles.length - length));
        highestY = (yc < (tiles[0].length - length) ? yc : (tiles[0].length - length));
        int highestXDistance = highestX - xc;
        int highestYDistance = highestY - yc;
        highest = (highestXDistance < highestYDistance ? highestXDistance : highestYDistance); // Highest bound = lowest of the maxs

        //If lowest > highest, a setup is not possible in this position (can happen near corners)
        if (lowest > highest) {
            return false;
        }

        Outer:
        for (int dxy = lowest; dxy <= highest; dxy++) {
            for (int i = 0; i < length; i++) {
                if (tiles[xc + dxy + i][yc + dxy + i] != ID) {
                    continue Outer;
                }
            }
            //If this is reached, a setup has been found and true is returned
            return true;
        }
        //If this is reached, no setup has been found and false is returned
        return false;
    }

    /**
     * Checks for setups diagonally ( / ).
     *
     * @param tiles Board to check setup on
     * @param xc X to check
     * @param yc Y to check
     * @param length Length of chain to look for
     * @param ID Player to check for
     * @return Returns true if setup is found for player [ID]
     */
    public boolean checkForSetupDiagonal2(int[][] tiles, int xc, int yc, int length, int ID) {
        int lowestX, highestX, lowestY, highestY, lowest, highest;
        //Find lower loop bound
        lowestX = (xc + (length - 1) < (tiles.length - 1) ? xc + (length - 1) : (tiles.length - 1));
        lowestY = (yc - (length - 1) > 0 ? yc - (length - 1) : 0);
        int lowestXOffset = xc - lowestX;
        int lowestYOffset = lowestY - yc;
        lowest = (lowestXOffset > lowestYOffset ? lowestXOffset : lowestYOffset); // Lowest bound = highest of the mins
        //Find upper loop bound
        highestX = (xc > (length - 1) ? xc : (length - 1));
        highestY = (yc < (tiles[0].length - length) ? yc : (tiles[0].length - length));
        int highestXDistance = xc - highestX;
        int highestYDistance = highestY - yc;
        highest = (highestXDistance < highestYDistance ? highestXDistance : highestYDistance); // Highest bound = lowest of the maxs

        //If lowest > highest, a setup is not possible in this position (can happen near corners)
        if (lowest > highest) {
            return false;
        }

        Outer:
        for (int dxy = lowest; dxy <= highest; dxy++) {
            for (int i = 0; i < length; i++) {
                if (tiles[xc - dxy - i][yc + dxy + i] != ID) {
                    continue Outer;
                }
            }
            //If this is reached, a setup has been found and true is returned
            return true;
        }
        //If this is reached, no setup has been found and false is returned
        return false;
    }

    public static int[][] invertMatrix(int[][] matrix) {
        int[][] newMatrix = new int[matrix[0].length][matrix.length];
        for (int x = 0; x < newMatrix[0].length; x++) {
            for (int y = 0; y < newMatrix.length; y++) {
                newMatrix[x][y] = matrix[y][x];
            }
        }
        return newMatrix;
    }
}
