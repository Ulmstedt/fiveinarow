package fiveinarow;

/**
 *
 * @author Sehnsucht
 */
public class AIPlayer extends Player {

    private int width, height;
    //private int[][] basePointGrid;

    public AIPlayer(int id, Game game) {
        super(id, game);
        width = game.getWidth();
        height = game.getHeight();
        pointGrid = new int[width][height];
        //basePointGrid = new int[width][height];
        //initBasePointGrid();
    }

    /*private void initBasePointGrid() {
     for (int i = 0; i < width; i++) {
     for (int j = 0; j < height; j++) {
     basePointGrid[i][j] = (i <= width - i - 1 ? i : width - i - 1) + (j <= height - j - 1 ? j : height - j - 1);
     }
     }
     }*/
    private void resetPointGrid() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pointGrid[i][j] = (i <= width - i - 1 ? i : width - i - 1) + (j <= height - j - 1 ? j : height - j - 1);
            }
        }
    }

    

    @Override
    public void playRound() {
        //Start time of AI's turn
        long startTime = System.currentTimeMillis();

        pointGrid = calculatePointGrid(game.getBoard());
        findBestMove();
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
        calculatePointGrid(game.getBoard());
    }

    private void findBestMove() {
        currX = currY = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (pointGrid[i][j] > pointGrid[currX][currY]) {
                    currX = i;
                    currY = j;
                }
            }
        }
    }

    public int[][] calculatePointGrid(int[][] board) {
        resetPointGrid();
        //int[][] pointGrid = this.getPointGrid().clone();
        int[][] tempBoard = board.clone(); //Copy board
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = 0; x < game.getWidth(); x++) {
                for (int y = 0; y < game.getHeight(); y++) {
                    if (game.getTile(x, y) == 0) {
                        //Check if placement would give victory to any player
                        tempBoard[x][y] = p;
                        if (game.checkForWinner(tempBoard) == p) {
                            pointGrid[x][y] += (p == ID ? 3000 : 800);
                        }
                        //Check each direction separately to count double setups
                        for (int mode = 1; mode <= 4; mode++) {
                            if (checkForFourSetup(tempBoard, mode, x, y) == p) {
                                pointGrid[x][y] += (p == ID ? 80 : 60);
                            } else if (checkForThreeSetup(tempBoard, mode, x, y) == p) {
                                pointGrid[x][y] += (p == ID ? 20 : 10);
                            } else if (checkForTwoSetup(tempBoard, mode, x, y) == p) {
                                pointGrid[x][y] += (p == ID ? 2 : 1);
                            }
                        }
                        tempBoard[x][y] = 0;

                    } else {
                        pointGrid[x][y] = 0;
                    }
                }
            }
        }
        return pointGrid;
    }

    /*
     Checks if a player has setup to win.
     Modes: 1 = rows, 2 = columns, 3 = diagonal \, 4 = diagonal /
     Returns 0 if no setup, or the player ID if someone has a setup.
     */
    public int checkForFourSetup(int[][] tiles, int mode, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc - 3 > 0 ? xc - 3 : 0); x < (yc + 3 < width - 4 ? yc + 3 : width - 4); x++) {
                for (int y = (yc - 3 > 0 ? yc - 3 : 0); y < (yc + 3 < height - 4 ? yc + 3 : height - 4); y++) {
                    switch (mode) {
                        case 1:
                            //Check rows
                            if (x <= width - 4 && tiles[x][y] == p && tiles[x + 1][y] == p && tiles[x + 2][y] == p && tiles[x + 3][y] == p) {
                                return p;
                            }
                            break;
                        case 2:
                            //Check columns
                            if (y <= height - 4 && (tiles[x][y] == p && tiles[x][y + 1] == p && tiles[x][y + 2] == p && tiles[x][y + 3] == p)) {
                                return p;
                            }
                            break;
                        case 3:
                            //Check diagonals \
                            if (x <= width - 4 && y <= height - 4
                                    && (tiles[x][y] == p && tiles[x + 1][y + 1] == p && tiles[x + 2][y + 2] == p && tiles[x + 3][y + 3] == p)) {
                                return p;
                            }
                            break;
                        case 4:
                            //Check diagonals /
                            if (x >= 3 && y <= height - 4
                                    && (tiles[x][y] == p && tiles[x - 1][y + 1] == p && tiles[x - 2][y + 2] == p && tiles[x - 3][y + 3] == p)) {
                                return p;
                            }
                            break;
                    }
                }
            }
        }

        return 0;
    }

    public int checkForThreeSetup(int[][] tiles, int mode, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc - 2 > 0 ? xc - 2 : 0); x < (yc + 2 < width - 3 ? yc + 2 : width - 3); x++) {
                for (int y = (yc - 2 > 0 ? yc - 2 : 0); y < (yc + 2 < height - 3 ? yc + 2 : height - 3); y++) {
                    switch (mode) {
                        case 1:
                            //Check rows
                            if (x <= width - 3 && tiles[x][y] == p && tiles[x + 1][y] == p && tiles[x + 2][y] == p) {
                                System.out.println("row");
                                return p;
                            }
                            break;
                        case 2:
                            //Check columns
                            if (y <= height - 3 && (tiles[x][y] == p && tiles[x][y + 1] == p && tiles[x][y + 2] == p)) {
                                System.out.println("col");
                                return p;
                            }
                            break;
                        case 3:
                            //Check diagonals \
                            if (x <= width - 3 && y <= height - 3
                                    && (tiles[x][y] == p && tiles[x + 1][y + 1] == p && tiles[x + 2][y + 2] == p)) {
                                System.out.println("diagon 1");
                                return p;
                            }
                            break;
                        case 4:
                            //Check diagonals /
                            if (x >= 2 && y <= height - 3
                                    && (tiles[x][y] == p && tiles[x - 1][y + 1] == p && tiles[x - 2][y + 2] == p)) {
                                System.out.println("diagon 2");
                                return p;
                            }
                            break;
                    }
                }
            }
        }

        return 0;
    }

    public int checkForTwoSetup(int[][] tiles, int mode, int xc, int yc) {
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = (xc - 1 > 0 ? xc - 1 : 0); x < (yc + 1 < width - 2 ? yc + 1 : width - 2); x++) {
                for (int y = (yc - 1 > 0 ? yc - 1 : 0); y < (yc + 1 < height - 2 ? yc + 1 : height - 2); y++) {
                    switch (mode) {
                        case 1:
                            //Check rows
                            if (x <= width - 2 && tiles[x][y] == p && tiles[x + 1][y] == p) {
                                System.out.println("row");
                                return p;
                            }
                            break;
                        case 2:
                            //Check columns
                            if (y <= height - 2 && (tiles[x][y] == p && tiles[x][y + 1] == p)) {
                                System.out.println("col");
                                return p;
                            }
                            break;
                        case 3:
                            //Check diagonals \
                            if (x <= width - 2 && y <= height - 2
                                    && (tiles[x][y] == p && tiles[x + 1][y + 1] == p)) {
                                System.out.println("diagon 1");
                                return p;
                            }
                            break;
                        case 4:
                            //Check diagonals /
                            if (x >= 1 && y <= height - 2
                                    && (tiles[x][y] == p && tiles[x - 1][y + 1] == p)) {
                                System.out.println("diagon 2");
                                return p;
                            }
                            break;
                    }
                }
            }
        }

        return 0;
    }
}
