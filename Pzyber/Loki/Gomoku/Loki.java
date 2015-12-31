/**
 * Loki Learning AI
 *
 * Loki.java
 * Created on 2015-12-28
 * Version 0.2.0 Beta
 *
 * Written by Jimmy Nordström.
 * © 2015 Jimmy Nordström.
 *
 * Licenced under GNU GPLv3.
 * http://www.gnu.org/licenses/gpl-3.0.html
 *
 * If you have questions, contact me at pzyber@pzyber.net
 */

package Pzyber.Loki.Gomoku;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Loki {
    private int maxID;
    private int width, height;
    private ILokiDB lokiDB;
    private List<GameData> gameData = new ArrayList<>();
    private Random random = new Random();

    // Memory DB.
    public Loki(int size, int maxID, boolean aggressive) {
        this.width = size;
        this.height = size;
        this.maxID = maxID;
        MoveData.aggressive = aggressive;

        lokiDB = new MemoryDB();
    }

    // Folder DB.
    public Loki(String path, int size, int maxID, boolean aggressive) {
        this.width = size;
        this.height = size;
        this.maxID = maxID;
        MoveData.aggressive = aggressive;

        lokiDB = new FolderDB(path);
    }

    // My-SQL DB.
   /* public Loki(InetAddress address, short port, String database, String username, String password, int size,
                int maxID) {
        this.width = size;
        this.height = size;
        this.maxID = maxID;

        // Connect to SQL-server.
        String url = "jdbc:mysql://" + address.getHostAddress() + ":" + port + "/" + database;
        Connection con;
        try {
            con = DriverManager.getConnection(url, username, password);
            stmt = con.createStatement();
        } catch (SQLException e) {
            // If error, fall back to memory db.
            lokiDB = new MemoryDB();
        }
    }*/

    private ArrayList<MoveData> getDataFromDB(int[][] board, int searchWidth, int searchHeight) {
        ArrayList<MoveData> data = new ArrayList<>();

        // Search for patterns in DB.
        for (int i = 0; i < 4; i++) { // Flip board loop.
            // Rotate board.
            for (int j = 4 - i; j < 4; j++) {
                board = Utils.rotateBoardClockwise(board, width);
            }

            for (int j = 0; j < maxID; j++) { // Flip id loop.
                int startX = 0;
                int endX = searchWidth - 1;
                int startY = 0;
                int endY = searchHeight - 1;
                while (endY < height) {
                    while (endX < width) {
                        // Calculate hash.
                        String hash = Utils.calculateHash(board, j, startX, startY, endX, endY, maxID);

                        // Get available moves for current hash from loki db and add do data.
                        ArrayList<MoveData> availableMoves = lokiDB.getAvailableMovesFromDB(hash, startX, startY, i, width);
                        for (MoveData m : availableMoves) {
                            // Add MoveData to data list.
                            data.add(m);
                        }

                        startX++;
                        endX++;
                    }

                    startX = 0;
                    endX = searchWidth - 1;
                    startY++;
                    endY++;
                }
            }
        }

        return data;
    }

    public void registerMoveInDB(int[][] board, Point move) {
        // Clone board.
        int[][] clonedBoard = Utils.cloneMatrix(board);

        // Get player ID.
        int id = clonedBoard[move.x][move.y];

        // Reverse recent move.
        clonedBoard[move.x][move.y] = 0;

        // Store previous board and the new move.
        gameData.add(new GameData(clonedBoard, move, id));
    }

    private void storeDataInDB(GameData gd, int winnerID, int searchWidth, int searchHeight) {
        int[][] board = gd.getBoard();
        Point move = gd.getMove();
        int id = gd.getID();

        int startX = 0;
        int endX = searchWidth - 1;
        int startY = 0;
        int endY = searchHeight - 1;
        while (endY < height) {
            while (endX < width) {
                if (Utils.hasAdjecentMoveOrFullSize(board, startX, startY, endX, endY, width, height) &&
                        move.x >= startX && move.x <= endX && move.y >= startY && move.y <= endY) {
                    // Calculate hash.
                    String hash = Utils.calculateHash(board, 0, startX, startY, endX, endY, maxID);

                    // Descale move to board.
                    Point descaledMove = new Point(move.x - startX, move.y - startY);

                    // Store data to database.
                    lokiDB.addToDB(hash, descaledMove, winnerID == 0 ? MemoryDB.DRAW : (winnerID == id ? MemoryDB.WIN :
                            MemoryDB.LOSS));
                }

                startX++;
                endX++;
            }

            startX = 0;
            endX = searchWidth - 1;
            startY++;
            endY++;
        }
    }

    // The actual learning mechanism. Requires that all moves are registered with registerMoveInDB.
    public void storeGameDataInDB(int winnerID) {
        // Start time of start of storing process.
        System.out.println("Loki: Storing game data in DB...");
        long startTime = System.currentTimeMillis();

        for (GameData gd : gameData) {
            int searchWidth = width;
            int searchHeight = height;
            while (searchWidth > 1 && searchHeight > 1) {
                storeDataInDB(gd, winnerID, searchWidth, searchHeight);

                searchWidth--;
                searchHeight--;
            }
        }

        gameData.clear();

        // End time of storing process.
        long endTime = System.currentTimeMillis();
        long timeSpent = endTime - startTime;
        System.out.println("Loki: Game data stored in " + timeSpent + " ms.");
    }

    public LokiResult thinkOfAMove(int[][] board) {
        // Start time of AI's turn.
        System.out.println("Loki: Thinking out a move...");
        long startTime = System.currentTimeMillis();

        int[][] clonedBoard = Utils.cloneMatrix(board);
        LokiResult ret = thinkOfAMoveInner(clonedBoard);

        // End time of AI's turn.
        long endTime = System.currentTimeMillis();
        long timeSpent = endTime - startTime;
        Point move = ret.getMove();
        System.out.println("Loki: Move " + move.x + ":" + move.y + " chosen in " + timeSpent + " ms.");

        return ret;
    }

    private LokiResult thinkOfAMoveInner(int[][] board) {
        Map<Point, MoveData> data = new HashMap<>();

        // Search for patterns and add statistics to data.
        int searchWidth = width;
        int searchHeight = height;
        int searchLevelOfResult = searchWidth;
        ArrayList<MoveData> resultData;
        boolean positiveSearchResultFound = false;
        while (!positiveSearchResultFound && searchWidth > 1 && searchHeight > 1) {
            int[][] clonedBoard = Utils.cloneMatrix(board);
            resultData = getDataFromDB(clonedBoard, searchWidth, searchHeight);

            if (resultData != null) {
                for (MoveData md : resultData) {
                    Point move = md.getMove();
                    if (data.containsKey(move)) {
                        MoveData existingMoveData = data.get(move);

                        existingMoveData.addDraws(md.getDraws());
                        existingMoveData.addLosses(md.getLosses());
                        existingMoveData.addWins(md.getWins());
                    } else {
                        data.put(move, md);


                        // Check if any win chance.
                        if (md.thoughtResult() > 0) {
                            searchLevelOfResult = searchWidth;
                            positiveSearchResultFound = true;
                        }
                    }
                }
            }

            searchWidth--;
            searchHeight--;
        }

        // Build board of conceived data.
        float bestThoughtResult = 0;
        float[][] thoughtData = new float[width][height];
        ArrayList<Point> bestMoves = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Get result for given point.
                Point coord = new Point(i, j);
                float thoughtResult = 0;
                if (data.containsKey(coord)) {
                    thoughtResult = data.get(coord).thoughtResult();
                }
                thoughtData[i][j] = thoughtResult;
                if (thoughtResult > 0) {
                    System.out.println("Loki thoghtResult " + i + " : " + j + " = " + thoughtResult);
                }

                // Check if better move.
                if (board[i][j] == 0) {
                    if (thoughtResult > bestThoughtResult) {
                        bestThoughtResult = thoughtResult;
                        bestMoves.clear();
                        bestMoves.add(coord);
                    } else if (thoughtResult == bestThoughtResult) {
                        bestMoves.add(coord);
                    }
                }
            }
        }

        System.out.println("Loki search level of results: " + searchLevelOfResult);
        System.out.println("Loki Result percentage: " + bestThoughtResult);

        // Select the best move and return a LokiResult, if more then one then randomly select one of them.
        Point bestMove = bestMoves.get(random.nextInt(bestMoves.size()));
        return new LokiResult(bestMove, thoughtData, width, height);
    }
}