package fiveinarow;

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

    public AIPlayer(int id, Game game) {
        super(id, game);
        width = game.getWidth();
        height = game.getHeight();
    }

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
        //calculatePointGrid(game.getBoard());
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
        int[][] tempBoard = board.clone(); //Copy board
        for (int p = 1; p <= game.getNumberOfPlayers(); p++) {
            for (int x = 0; x < game.getWidth(); x++) {
                for (int y = 0; y < game.getHeight(); y++) {
                    if (game.getTile(x, y) == 0) {
                        //Check if placement would give victory to any player
                        tempBoard[x][y] = p;
                        if (game.checkForWinner(tempBoard) == p) {
                            pointGrid[x][y] += (p == ID ? WIN_SETUP_SCORE : WIN_BLOCK_SCORE);
                        }
                        //Check setup for 4 in a row
                        if (checkForFourSetupRows(tempBoard, x, y) == p) {
                            pointGrid[x][y] += (p == ID ? FOUR_SETUP_SCORE : FOUR_BLOCK_SCORE);
                        }
                        if (checkForFourSetupCols(tempBoard, x, y) == p) {
                            pointGrid[x][y] += (p == ID ? FOUR_SETUP_SCORE : FOUR_BLOCK_SCORE);
                        }
                        if (checkForFourSetupDiagonal1(tempBoard, x, y) == p) {
                            pointGrid[x][y] += (p == ID ? FOUR_SETUP_SCORE : FOUR_BLOCK_SCORE);
                        }
                        if (checkForFourSetupDiagonal2(tempBoard, x, y) == p) {
                            pointGrid[x][y] += (p == ID ? FOUR_SETUP_SCORE : FOUR_BLOCK_SCORE);
                        }
                        //Check setup for 3 in a row
                        if (checkForThreeSetupRows(tempBoard, x, y) == p) {
                            pointGrid[x][y] += (p == ID ? THREE_SETUP_SCORE : THREE_BLOCK_SCORE);
                        }
                        if (checkForThreeSetupCols(tempBoard, x, y) == p) {
                            pointGrid[x][y] += (p == ID ? THREE_SETUP_SCORE : THREE_BLOCK_SCORE);
                        }
                        if (checkForThreeSetupDiagonal1(tempBoard, x, y) == p) {
                            pointGrid[x][y] += (p == ID ? THREE_SETUP_SCORE : THREE_BLOCK_SCORE);
                        }
                        if (checkForThreeSetupDiagonal1(tempBoard, x, y) == p) {
                            pointGrid[x][y] += (p == ID ? THREE_SETUP_SCORE : THREE_BLOCK_SCORE);
                        }
                        //Check setup for 2 in a row
                        if (checkForTwoSetupRows(tempBoard, x, y) == p) {
                            pointGrid[x][y] += (p == ID ? TWO_SETUP_SCORE : TWO_BLOCK_SCORE);
                        }
                        if (checkForTwoSetupCols(tempBoard, x, y) == p) {
                            pointGrid[x][y] += (p == ID ? TWO_SETUP_SCORE : TWO_BLOCK_SCORE);
                        }
                        if (checkForTwoSetupDiagonal1(tempBoard, x, y) == p) {
                            pointGrid[x][y] += (p == ID ? TWO_SETUP_SCORE : TWO_BLOCK_SCORE);
                        }
                        if (checkForTwoSetupDiagonal2(tempBoard, x, y) == p) {
                            pointGrid[x][y] += (p == ID ? TWO_SETUP_SCORE : TWO_BLOCK_SCORE);
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
