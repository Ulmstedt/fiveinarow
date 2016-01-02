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
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    private int width, height;

    private final int WIN_SETUP_SCORE = 5000;
    private final int WIN_BLOCK_SCORE = 1200;
    private final int FOUR_SETUP_SCORE = 300;
    private final int FOUR_BLOCK_SCORE = 250;
    private final int THREE_SETUP_SCORE = 40;
    private final int THREE_BLOCK_SCORE = 25;
    private final int TWO_SETUP_SCORE = 2;
    private final int TWO_BLOCK_SCORE = 1;
    private final double SIMULATION_INTENSITY = 0.5; //Lower value simulates more cases
    private final int SIMULATION_DEPTH = 5; //How many rounds in the future that is simulated

    public AIPlayer(int id, Game game) {
        super(id, game);
        width = game.getWidth();
        height = game.getHeight();
    }

    private int[][] newPointGrid() {
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
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("Setup " + (checkForSetupDiagonal1(AIPlayer.invertMatrix(AIPlayer.setupDiaAt55), 5, 5, 4, 1) ? "found" : "not found"));
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

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

        pointGrid = calculatePointGrid(gameBoard, this.ID, false);
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

            //Print board
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    System.out.print(tempBoard[y][x]);
//                }
//                System.out.println();
//            }
            //
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
//        System.out.println("------- DEBUG --------");
        pointGrid = calculatePointGrid(game.getBoard(), 1, false);
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
            //System.out.println("Current player: " + currentID);
            //
            int[][] boardCopy = Utils.cloneMatrix(gameBoard);
            int[][] pGrid = calculatePointGrid(boardCopy, currentID, false);
            Point p = findBestMove(pGrid);
            System.out.println("P" + currentID + " played (" + p.x + ", " + p.y + ") - " + (roundsLeft - 1) + " rounds left to simulate.");
            //System.out.println("-----------");
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

    public int[][] calculatePointGrid(int[][] board, int playerID, boolean defensive) {
        //resetPointGrid();
        return new int[][]{{0}};
        /*
         int[][] tempPointGrid = newPointGrid();
         int[][] tempBoard = Utils.cloneMatrix(board); //Copy board
         for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
         for (int x = 0; x < game.getWidth(); x++) {
         for (int y = 0; y < game.getHeight(); y++) {
         if (board[x][y] == 0) {
         //Debug this position:
         final int debugX = 5, debugY = 5;
         //Check if placement would give victory to any player
         tempBoard[x][y] = p;
         if (!defensive) {
         if (game.checkForWinner(tempBoard) == p) {
         tempPointGrid[x][y] += (p == playerID ? WIN_SETUP_SCORE : WIN_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "WIN_SETUP_SCORE" : "WIN_BLOCK_SCORE"));
         }
         }
         //Check setup for 4 in a row
         if (checkForFourSetupRows(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? FOUR_SETUP_SCORE : FOUR_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "FOUR_SETUP_SCORE ROW" : "FOUR_BLOCK_SCORE ROW"));
         }
         }
         if (checkForFourSetupCols(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? FOUR_SETUP_SCORE : FOUR_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "FOUR_SETUP_SCORE COL" : "FOUR_BLOCK_SCORE COL"));
         }
         }
         if (checkForFourSetupDiagonal1(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? FOUR_SETUP_SCORE : FOUR_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "FOUR_SETUP_SCORE DIA1" : "FOUR_BLOCK_SCORE DIA1"));
         }
         }
         if (checkForFourSetupDiagonal2(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? FOUR_SETUP_SCORE : FOUR_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "FOUR_SETUP_SCORE DIA2" : "FOUR_BLOCK_SCORE DIA2"));
         }
         }
         //Check setup for 3 in a row
         if (checkForThreeSetupRows(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? THREE_SETUP_SCORE : THREE_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "THREE_SETUP_SCORE ROW" : "THREE_BLOCK_SCORE ROW"));
         }
         }
         if (checkForThreeSetupCols(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? THREE_SETUP_SCORE : THREE_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "THREE_SETUP_SCORE COL" : "THREE_BLOCK_SCORE COL"));
         }
         }
         if (checkForThreeSetupDiagonal1(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? THREE_SETUP_SCORE : THREE_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "THREE_SETUP_SCORE DIA1" : "THREE_BLOCK_SCORE DIA1"));
         }
         }
         if (checkForThreeSetupDiagonal2(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? THREE_SETUP_SCORE : THREE_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "THREE_SETUP_SCORE DIA2" : "THREE_BLOCK_SCORE DIA2"));
         }
         }
         //Check setup for 2 in a row
         if (checkForTwoSetupRows(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? TWO_SETUP_SCORE : TWO_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "TWO_SETUP_SCORE ROW" : "TWO_BLOCK_SCORE ROW"));
         }
         }
         if (checkForTwoSetupCols(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? TWO_SETUP_SCORE : TWO_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "TWO_SETUP_SCORE COL" : "TWO_BLOCK_SCORE COL"));
         }
         }
         if (checkForTwoSetupDiagonal1(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? TWO_SETUP_SCORE : TWO_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "TWO_SETUP_SCORE DIA1" : "TWO_BLOCK_SCORE DIA1"));
         }
         }
         if (checkForTwoSetupDiagonal2(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? TWO_SETUP_SCORE : TWO_BLOCK_SCORE);
         if (x == debugX && y == debugY) {
         System.out.println((p == playerID ? "TWO_SETUP_SCORE DIA2" : "TWO_BLOCK_SCORE DIA2"));
         }
         }
         } else {
         if (game.checkForWinner(tempBoard) == p) {
         tempPointGrid[x][y] += (p == playerID ? WIN_SETUP_SCORE : WIN_BLOCK_SCORE);
         }
         //Check setup for 4 in a row
         if (checkForFourSetupRows(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? FOUR_BLOCK_SCORE : FOUR_SETUP_SCORE);
         }
         if (checkForFourSetupCols(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? FOUR_BLOCK_SCORE : FOUR_SETUP_SCORE);
         }
         if (checkForFourSetupDiagonal1(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? FOUR_BLOCK_SCORE : FOUR_SETUP_SCORE);
         }
         if (checkForFourSetupDiagonal2(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? FOUR_BLOCK_SCORE : FOUR_SETUP_SCORE);
         }
         //Check setup for 3 in a row
         if (checkForThreeSetupRows(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? THREE_BLOCK_SCORE : THREE_SETUP_SCORE);
         }
         if (checkForThreeSetupCols(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? THREE_BLOCK_SCORE : THREE_SETUP_SCORE);
         }
         if (checkForThreeSetupDiagonal1(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? THREE_BLOCK_SCORE : THREE_SETUP_SCORE);
         }
         if (checkForThreeSetupDiagonal2(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? THREE_BLOCK_SCORE : THREE_SETUP_SCORE);
         }
         //Check setup for 2 in a row
         if (checkForTwoSetupRows(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? TWO_BLOCK_SCORE : TWO_SETUP_SCORE);
         }
         if (checkForTwoSetupCols(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? TWO_BLOCK_SCORE : TWO_SETUP_SCORE);
         }
         if (checkForTwoSetupDiagonal1(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? TWO_BLOCK_SCORE : TWO_SETUP_SCORE);
         }
         if (checkForTwoSetupDiagonal2(tempBoard, x, y) == p) {
         tempPointGrid[x][y] += (p == playerID ? TWO_BLOCK_SCORE : TWO_SETUP_SCORE);
         }
         }
         tempBoard[x][y] = 0;

         } else {
         tempPointGrid[x][y] = 0;
         }
         }
         }
         }
         return tempPointGrid;*/
    }

    // ##################
    // ## Setup checks ##
    // ##################
    public int checkForSetupRows(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc - 3 > 0 ? xc - 3 : 0); x <= (xc < width - 4 ? xc : width - 4); x++) {
                //Check rows
                if (tiles[x][yc] == p && tiles[x + 1][yc] == p && tiles[x + 2][yc] == p && tiles[x + 3][yc] == p) {
                    return p;
                }
            }
        }
        return 0;
    }

    public int checkForSetupCols(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int y = (yc - 3 > 0 ? yc - 3 : 0); y <= (yc < height - 4 ? yc : height - 4); y++) {
                //Check columns
                if (tiles[xc][y] == p && tiles[xc][y + 1] == p && tiles[xc][y + 2] == p && tiles[xc][y + 3] == p) {
                    return p;
                }
            }
        }
        return 0;
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
        //Find loop bounds
        lowestX = (xc - length > 0 ? xc - length : 0);
        lowestY = (yc - length > 0 ? yc - length : 0);
        lowest = (lowestX > lowestY ? lowestX : lowestY); // Lowest bound = highest of the mins
        highestX = (xc < (tiles.length - (length + 1)) ? xc : (tiles.length - (length + 1)));
        highestY = (yc < (tiles[0].length - (length + 1)) ? yc : (tiles[0].length - (length + 1)));
        highest = (highestX < highestY ? highestX : highestY); // Highest bound = lowest of the maxs
        System.out.println("Lowest: " + lowest);
        System.out.println("Highest: " + highest);

        //Check diagonals \
        Outer:
        for (int xy = lowest; xy <= highest; xy++) {
            for (int i = 0; i < length; i++) {
                if (tiles[xy + i][xy + i] != ID) {
                    System.out.println("tiles[" + (xy + i) + "][" + (xy + i) + "], i: " + i);
                    continue Outer;
                }
            }
            //If this is reached, a setup has been found and true is returned
            return true;
        }
        //If this is reached, no setup has been found and false is returned
        return false;
    }

    public int checkForSetupDiagonal2(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc > 3 ? xc : 3); x <= (xc + 3 < width - 1 ? xc + 3 : width - 1); x++) {
                for (int y = (yc - 3 > 0 ? yc - 3 : 0); y <= (yc < height - 4 ? yc : height - 4); y++) {
                    //Check diagonals /
                    if (tiles[x][y] == p && tiles[x - 1][y + 1] == p && tiles[x - 2][y + 2] == p && tiles[x - 3][y + 3] == p) {
                        return p;
                    }
                }
            }
        }
        return 0;
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
