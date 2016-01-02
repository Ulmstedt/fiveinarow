package fiveinarow;

import Pzyber.Loki.Gomoku.Utils;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Sehnsucht
 */
public class AIPlayer extends Player implements IAI {

    private int width, height;

    private final int WIN_SETUP_SCORE = 3000;
    private final int WIN_BLOCK_SCORE = 800;
    private final int FOUR_SETUP_SCORE = 100;
    private final int FOUR_BLOCK_SCORE = 80;
    private final int THREE_SETUP_SCORE = 30;
    private final int THREE_BLOCK_SCORE = 20;
    private final int TWO_SETUP_SCORE = 2;
    private final int TWO_BLOCK_SCORE = 1;
    private final double SIMULATION_INTENSITY = 0.5; //Lower value simulates more cases
    private final int SIMULATION_DEPTH = 3; //How many rounds in the future that is simulated

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

    /*
     for (int x = 0; x < width; x++) {
     for (int y = 0; y < height; y++) {
     //Check if score is high enough, and if so simulate for playing (x,y), and only simulate if its atleast a 4 setup
     if (pointGrid[x][y] >= FOUR_BLOCK_SCORE && pointGrid[x][y] >= (SIMULATION_INTENSITY * pointGrid[p.x][p.y])) { // && !(x == p.x && y == p.y)
     System.out.println("Simulating new point (" + ++pointsSimulated + ")");
     int[][] tempBoard = Utils.cloneMatrix(game.getBoard());
     tempBoard[x][y] = this.ID;
     int simulatedWinner = simulateGame(tempBoard, this.ID, SIMULATION_DEPTH);
     if (simulatedWinner != 0 && simulatedWinner != this.ID) {
     System.out.println("Simulated winner found");
     currX = x;
     currY = y;
     }
     }
     }
     }
     */
    @Override
    public void playRound() {
        //Start time of AI's turn
        long startTime = System.currentTimeMillis();

        // -----------------------
//        System.out.println("---------");
        int[][] knownProblem1 = new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 2, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
//        System.out.println("Winner: " + simulateGame(simulationTestBoard, ID, SIMULATION_DEPTH));
//        System.out.println("---------");
// -----------------------
        //pointGrid = calculatePointGrid(simulationTestBoard);
        pointGrid = calculatePointGrid(game.getBoard(), true);
        Point p = findBestMove(pointGrid);
        currX = p.x;
        currY = p.y;
        //System.out.println("Heatmap says: (" + p.x + ", " + p.y + ")");
        int pointsToSimulate = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //Check if score is high enough, and if so simulate for playing (x,y), and only simulate if its atleast a 4 setup
                if (pointGrid[x][y] >= THREE_BLOCK_SCORE && pointGrid[x][y] >= (SIMULATION_INTENSITY * pointGrid[p.x][p.y])) {
                    pointsToSimulate++;
                }
            }
        }
        System.out.println("Points to simulate: " + pointsToSimulate);
        System.out.println("-----");
        while (pointsToSimulate > 0) {
            System.out.println("Best point: (" + p.x + ", " + p.y + ")");
            System.out.println("Score: " + pointGrid[p.x][p.y]);
            int[][] tempBoard = Utils.cloneMatrix(game.getBoard());
            tempBoard[p.x][p.y] = this.ID;

            //Print board
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    System.out.print(tempBoard[y][x]);
//                }
//                System.out.println();
//            }
            //
            if (simulateGame(tempBoard, this.ID, SIMULATION_DEPTH) == 0 || simulateGame(tempBoard, this.ID, SIMULATION_DEPTH) == this.ID) {
                currX = p.x;
                currY = p.y;
                break;
            }
            System.out.println("Point thrown away");
            //Set score of current point to 0 since it would make us lose
            pointGrid[p.x][p.y] = 0;
            p = findBestMove(pointGrid);

            pointsToSimulate--;
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
        //calculatePointGrid(game.getBoard(), false);
    }

    private int simulateGame(int[][] gameBoard, int lastID, int roundsLeft) {
        //System.out.println("Rounds left: " + roundsLeft);
        int winner = game.checkForWinner(gameBoard);
        if (winner != 0) {
            //System.out.println("Winner found");
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
            int[][] pGrid = calculatePointGrid(boardCopy, true);
            Point p = findBestMove(pGrid);
            //System.out.println("Simulated p: (" + p.x + ", " + p.y + ")");
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

    /*
     expludeID = 0 -> check setups for all players
     expludeID = X -> Dont check for setups for player X
     */
    public int[][] calculatePointGrid(int[][] board, boolean defensive) {
        //resetPointGrid();
        int[][] tempPointGrid = newPointGrid();
        int[][] tempBoard = Utils.cloneMatrix(board); //Copy board
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = 0; x < game.getWidth(); x++) {
                for (int y = 0; y < game.getHeight(); y++) {
                    if (board[x][y] == 0) {
                        //Check if placement would give victory to any player
                        tempBoard[x][y] = p;
                        if (!defensive) {
                            if (game.checkForWinner(tempBoard) == p) {
                                tempPointGrid[x][y] += (p == ID ? WIN_SETUP_SCORE : WIN_BLOCK_SCORE);
                            }
                            //Check setup for 4 in a row
                            if (checkForFourSetupRows(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? FOUR_SETUP_SCORE : FOUR_BLOCK_SCORE);
                            }
                            if (checkForFourSetupCols(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? FOUR_SETUP_SCORE : FOUR_BLOCK_SCORE);
                            }
                            if (checkForFourSetupDiagonal1(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? FOUR_SETUP_SCORE : FOUR_BLOCK_SCORE);
                            }
                            if (checkForFourSetupDiagonal2(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? FOUR_SETUP_SCORE : FOUR_BLOCK_SCORE);
                            }
                            //Check setup for 3 in a row
                            if (checkForThreeSetupRows(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? THREE_SETUP_SCORE : THREE_BLOCK_SCORE);
                            }
                            if (checkForThreeSetupCols(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? THREE_SETUP_SCORE : THREE_BLOCK_SCORE);
                            }
                            if (checkForThreeSetupDiagonal1(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? THREE_SETUP_SCORE : THREE_BLOCK_SCORE);
                            }
                            if (checkForThreeSetupDiagonal1(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? THREE_SETUP_SCORE : THREE_BLOCK_SCORE);
                            }
                            //Check setup for 2 in a row
                            if (checkForTwoSetupRows(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? TWO_SETUP_SCORE : TWO_BLOCK_SCORE);
                            }
                            if (checkForTwoSetupCols(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? TWO_SETUP_SCORE : TWO_BLOCK_SCORE);
                            }
                            if (checkForTwoSetupDiagonal1(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? TWO_SETUP_SCORE : TWO_BLOCK_SCORE);
                            }
                            if (checkForTwoSetupDiagonal2(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? TWO_SETUP_SCORE : TWO_BLOCK_SCORE);
                            }
                        } else {
                            if (game.checkForWinner(tempBoard) == p) {
                                tempPointGrid[x][y] += (p == ID ? WIN_BLOCK_SCORE : WIN_SETUP_SCORE);
                            }
                            //Check setup for 4 in a row
                            if (checkForFourSetupRows(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? FOUR_BLOCK_SCORE : FOUR_SETUP_SCORE);
                            }
                            if (checkForFourSetupCols(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? FOUR_BLOCK_SCORE : FOUR_SETUP_SCORE);
                            }
                            if (checkForFourSetupDiagonal1(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? FOUR_BLOCK_SCORE : FOUR_SETUP_SCORE);
                            }
                            if (checkForFourSetupDiagonal2(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? FOUR_BLOCK_SCORE : FOUR_SETUP_SCORE);
                            }
                            //Check setup for 3 in a row
                            if (checkForThreeSetupRows(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? THREE_BLOCK_SCORE : THREE_SETUP_SCORE);
                            }
                            if (checkForThreeSetupCols(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? THREE_BLOCK_SCORE : THREE_SETUP_SCORE);
                            }
                            if (checkForThreeSetupDiagonal1(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? THREE_BLOCK_SCORE : THREE_SETUP_SCORE);
                            }
                            if (checkForThreeSetupDiagonal1(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? THREE_BLOCK_SCORE : THREE_SETUP_SCORE);
                            }
                            //Check setup for 2 in a row
                            if (checkForTwoSetupRows(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? TWO_BLOCK_SCORE : TWO_SETUP_SCORE);
                            }
                            if (checkForTwoSetupCols(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? TWO_BLOCK_SCORE : TWO_SETUP_SCORE);
                            }
                            if (checkForTwoSetupDiagonal1(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? TWO_BLOCK_SCORE : TWO_SETUP_SCORE);
                            }
                            if (checkForTwoSetupDiagonal2(tempBoard, x, y) == p) {
                                tempPointGrid[x][y] += (p == ID ? TWO_BLOCK_SCORE : TWO_SETUP_SCORE);
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

    // #################
    // ## Four setups ##
    // #################
    public int checkForFourSetupRows(int[][] tiles, int xc, int yc) {
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

    public int checkForFourSetupCols(int[][] tiles, int xc, int yc) {
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

    public int checkForFourSetupDiagonal1(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc - 3 > 0 ? xc - 3 : 0); x <= (xc < width - 4 ? xc : width - 4); x++) {
                for (int y = (yc - 3 > 0 ? yc - 3 : 0); y <= (yc < height - 4 ? yc : height - 4); y++) {
                    //Check diagonals \
                    if (tiles[x][y] == p && tiles[x + 1][y + 1] == p && tiles[x + 2][y + 2] == p && tiles[x + 3][y + 3] == p) {
                        return p;
                    }
                }
            }
        }
        return 0;
    }

    public int checkForFourSetupDiagonal2(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc > 3 ? xc : 3); x <= (xc + 3 < width - 1 ? xc + 3 : width - 1); x++) {
                for (int y = (yc - 3 > 0 ? yc - 3 : 0); y <= (yc < height - 4 ? yc : height - 4); y++) {
                    //Check diagonals \
                    if (tiles[x][y] == p && tiles[x - 1][y + 1] == p && tiles[x - 2][y + 2] == p && tiles[x - 3][y + 3] == p) {
                        return p;
                    }
                }
            }
        }
        return 0;
    }

    // ##################
    // ## Three setups ##
    // ##################
    public int checkForThreeSetupRows(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc - 2 > 0 ? xc - 2 : 0); x <= (xc < width - 3 ? xc : width - 3); x++) {
                //Check rows
                if (tiles[x][yc] == p && tiles[x + 1][yc] == p && tiles[x + 2][yc] == p) {
                    return p;
                }
            }
        }
        return 0;
    }

    public int checkForThreeSetupCols(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int y = (yc - 2 > 0 ? yc - 2 : 0); y <= (yc < height - 3 ? yc : height - 3); y++) {
                //Check columns
                if (tiles[xc][y] == p && tiles[xc][y + 1] == p && tiles[xc][y + 2] == p) {
                    return p;
                }
            }
        }
        return 0;
    }

    public int checkForThreeSetupDiagonal1(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc - 2 > 0 ? xc - 2 : 0); x <= (xc < width - 3 ? xc : width - 3); x++) {
                for (int y = (yc - 2 > 0 ? yc - 2 : 0); y <= (yc < height - 3 ? yc : height - 3); y++) {
                    //Check diagonals \
                    if (tiles[x][y] == p && tiles[x + 1][y + 1] == p && tiles[x + 2][y + 2] == p) {
                        return p;
                    }
                }
            }
        }
        return 0;
    }

    public int checkForThreeSetupDiagonal2(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc > 2 ? xc : 2); x <= (xc + 2 < width - 1 ? xc + 2 : width - 1); x++) {
                for (int y = (yc - 2 > 0 ? yc - 2 : 0); y <= (yc < height - 3 ? yc : height - 3); y++) {
                    //Check diagonals \
                    if (tiles[x][y] == p && tiles[x - 1][y + 1] == p && tiles[x - 2][y + 2] == p) {
                        return p;
                    }
                }
            }
        }
        return 0;
    }

    // ################
    // ## Two setups ##
    // ################
    public int checkForTwoSetupRows(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc - 1 > 0 ? xc - 1 : 0); x <= (xc < width - 2 ? xc : width - 2); x++) {
                //Check rows
                if (tiles[x][yc] == p && tiles[x + 1][yc] == p) {
                    return p;
                }
            }
        }
        return 0;
    }

    public int checkForTwoSetupCols(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int y = (yc - 1 > 0 ? yc - 1 : 0); y <= (yc < height - 2 ? yc : height - 2); y++) {
                //Check columns
                if ((tiles[xc][y] == p && tiles[xc][y + 1] == p)) {
                    return p;
                }
            }
        }
        return 0;
    }

    public int checkForTwoSetupDiagonal1(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc - 1 > 0 ? xc - 1 : 0); x <= (xc < width - 2 ? xc : width - 2); x++) {
                for (int y = (yc - 1 > 0 ? yc - 1 : 0); y <= (yc < height - 2 ? yc : height - 2); y++) {
                    //Check diagonals \
                    if (tiles[x][y] == p && tiles[x + 1][y + 1] == p) {
                        return p;
                    }
                }
            }
        }
        return 0;
    }

    public int checkForTwoSetupDiagonal2(int[][] tiles, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc > 1 ? xc : 1); x <= (xc + 1 < width - 1 ? xc + 1 : width - 1); x++) {
                for (int y = (yc - 1 > 0 ? yc - 1 : 0); y <= (yc < height - 2 ? yc : height - 2); y++) {
                    //Check diagonals \
                    if (tiles[x][y] == p && tiles[x - 1][y + 1] == p) {
                        return p;
                    }
                }
            }
        }
        return 0;
    }
}
