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
import java.io.*;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Loki {
    private int dbType;
    private int maxID;
    private int width, height;
    private List<GameData> gameData = new ArrayList<>();
    private Random random = new Random();

    // Filesystem DB.
    private String path;

    // SQL DB.
    private Statement stmt;

    //------------------------------------------------------------------------------------------------------------------

    // Memory DB.
    public Loki(int size, int maxID) {
        this.width = size;
        this.height = size;
        this.maxID = maxID;

        this.dbType = DBType.MEMORY;
    }

    // Filesystem DB.
    public Loki(String path, int size, int maxID) {
        this.width = size;
        this.height = size;
        this.maxID = maxID;

        this.dbType = DBType.FILE;

        this.path = path;
    }

    // My-SQL DB.
    public Loki(InetAddress address, short port, String database, String username, String password, int size,
                int maxID) {
        this.width = size;
        this.height = size;
        this.maxID = maxID;

        this.dbType = DBType.SQL;

        // Connect to SQL-server.
        String url = "jdbc:mysql://" + address.getHostAddress() + ":" + port + "/" + database;
        Connection con;
        try {
            con = DriverManager.getConnection(url, username, password);
            stmt = con.createStatement();
        } catch (SQLException e) {
            // If error, fall back to memory db.
            this.dbType = DBType.MEMORY;
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private String calculateHash(int[][] board, int flippidwith, int startX, int startY, int endX, int endY) {
        return getBoardAsString(board, flippidwith, startX, startY, endX, endY);

        /*String stringboard = getBoardAsString(board, flippidwith, startX, startY, endX, endY);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(stringboard.getBytes("UTF-8"));
            byte[] digest = md.digest();
            System.out.println(new String(digest, "UTF-8"));
            return new String(digest, "UTF-8");
        }
        catch (NoSuchAlgorithmException e) {
            return stringboard;
        }
        catch (UnsupportedEncodingException e) {
            return stringboard;
        }

        return stringboard;*/
    }

    protected int[][] cloneMatrix(int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][];

        for (int i = 0; i < matrix.length; i++) {
            newMatrix[i] = matrix[i].clone();
        }

        return newMatrix;
    }

    private String getBoardAsString(int[][] board, int flippidby, int startX, int startY, int endX, int endY) {
        String result = "";

        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                int value = board[i][j];

                // Flipp id.
                if (flippidby > 0) {
                    value += flippidby;
                    if (value > maxID) {
                        value = value - maxID;
                    }
                }

                result += Integer.toString(value);
            }
        }

        return result;
    }

    private ArrayList<MoveData> getDataFromDB(int[][] board, int searchWidth, int searchHeight) {
        ArrayList<MoveData> data = new ArrayList<>();

        // Search for patterns in DB.
        for (int i = 0; i < 4; i++) { // Flip board loop.
            // Rotate board.
            for (int j = 4 - i; j < 4; j++) {
                board = rotateBoardClockwise(board);
            }

            for (int j = 0; j < maxID; j++) { // Flip id loop.
                int startX = 0;
                int endX = searchWidth - 1;
                int startY = 0;
                int endY = searchHeight - 1;
                while (endY < height) {
                    while (endX < width) {
                        // Calculate hash.
                        String hash = calculateHash(board, j, startX, startY, endX, endY);

                        // TODO: Add SQL and Memory DB code.
                        String baseHashPath = path + "/" + hash;
                        if (Files.exists(Paths.get(baseHashPath))) {
                            // Get list of available moves from Loki DB.
                            File lokiDB = new File(baseHashPath);
                            String[] availableMoves = lokiDB.list();
                            if (availableMoves != null) {
                                for (String m : availableMoves) {
                                    // Get inner move.
                                    String[] posDot = m.split("\\.");
                                    String[] pos = posDot[0].split("_");
                                    Point move = new Point(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));

                                    // Scale move to board.
                                    move = new Point(startX + move.x, startY + move.y);

                                    // Rotate move.
                                    if (i != 1) {
                                        for (int k = 4 - i + 1; k < 4; k++) {
                                            move = rotateMoveAntiClockwise(move);
                                        }
                                    } else {
                                        for (int k = 4 - i; k < 4; k++) {
                                            move = rotateMoveClockwise(move);
                                        }
                                    }

                                    // Get draws, losses and wins.
                                    long[] dbData = readDataFromDBFile(baseHashPath + "/" + m);
                                    long draws = dbData[0];
                                    long losses = dbData[1];
                                    long wins = dbData[2];

                                    // Add MoveData to data list.
                                    data.add(new MoveData(move, draws, losses, wins));
                                }
                            }
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

    private boolean hasAdjecentMoveOrFullSize(int[][] board, int startX, int startY, int endX, int endY) {
        if (startX == 0 && startY == 0 && endX == width - 1 && endY == height - 1) {
            return true;
        }

        int value = 0;
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                value += board[i][j];
            }
        }
        return value > 0;
    }

    private long[] readDataFromDBFile(String path) {
        // Get draws, losses and wins.
        long draws = 0;
        long losses = 0;
        long wins = 0;
        try {
            List<String> rows = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            draws = Long.parseLong(rows.get(0));
            losses = Long.parseLong(rows.get(1));
            wins = Long.parseLong(rows.get(2));

        } catch (IOException ignored) {
        }

        return new long[]{draws, losses, wins};
    }

    private int[] readDataFromDBMemory() {
        // TODO: Fill with content.
        return null;
    }

    private int[] readDataFromDBSQL() {
        // TODO: Fill with content.
        return null;
    }

    public void registerMoveInDB(int[][] board, Point move) {
        // Clone board.
        int[][] clonedBoard = cloneMatrix(board);

        // Get player ID.
        int id = clonedBoard[move.x][move.y];

        // Reverse recent move.
        clonedBoard[move.x][move.y] = 0;

        // Store previous board and the new move.
        gameData.add(new GameData(clonedBoard, move, id));
    }

    private int[][] rotateBoardClockwise(int[][] board) {
        int size = width;
        int[][] rotated = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rotated[i][j] = board[size - j - 1][i];
            }
        }

        return rotated;
    }

    // x = y, y = (size - 1) - x
    @SuppressWarnings("SuspiciousNameCombination")
    private Point rotateMoveAntiClockwise(Point move) {
        return new Point(move.y, height - 1 - move.x);
    }

    // x = (size - 1) - y, y = x
    @SuppressWarnings("SuspiciousNameCombination")
    private Point rotateMoveClockwise(Point move) {
        return new Point(width - 1 - move.y, move.x);
    }

    private void storeDataInDB(GameData gd, int winnerID, int searchWidth, int searchHeight) {
        int[][] board = gd.getBoard();
        Point move = gd.getMove();
        int id = gd.getID();

        // TODO: Rewrite broken rotation to save storage space.
        //for(int i = 0; i < 4; i++) { // Flip board loop.
        // Rotate board.
            /*for(int j = 4 - i; j < 4; j++)
            {
                rotateBoardClockwise(board);
                move = rotateMoveClockwise(move);
            }*/

        int startX = 0;
        int endX = searchWidth - 1;
        int startY = 0;
        int endY = searchHeight - 1;
        while (endY < height) {
            while (endX < width) {
                if (hasAdjecentMoveOrFullSize(board, startX, startY, endX, endY) && move.x >= startX &&
                        move.x <= endX && move.y >= startY && move.y <= endY) {
                    // Calculate hash.
                    String hash = calculateHash(board, 0, startX, startY, endX, endY);

                    // Descale move to board.
                    Point descaledMove = new Point(move.x - startX, move.y - startY);

                    // Store data to database.
                    // TODO: Add SQL and Memory DB code.
                    if(dbType == DBType.MEMORY)
                    {

                    }
                    else if(dbType == DBType.FILE) {
                        String filePath = path + "/" + hash + "/" + descaledMove.x + "_" + descaledMove.y + ".txt";
                        if (Files.exists(Paths.get(path + "/" + hash))) {
                            if (Files.exists(Paths.get(filePath))) {
                                // Get draws, losses and wins.
                                long[] dbData = readDataFromDBFile(filePath);
                                long draws = dbData[0];
                                long losses = dbData[1];
                                long wins = dbData[2];

                                // Write data to file.
                                writeDataToDBFile(filePath, winnerID, id, draws, losses, wins);
                            } else {
                                // Write data to file.
                                writeDataToDBFile(filePath, winnerID, id, 0, 0, 0);
                            }
                        } else {
                            // Create hashed folder and write data to file..
                            File hashedFolder = new File(path + "/" + hash);
                            if (hashedFolder.mkdirs()) {
                                writeDataToDBFile(filePath, winnerID, id, 0, 0, 0);
                            }
                        }
                    }
                    else if(dbType == DBType.SQL){

                    }
                }

                startX++;
                endX++;
            }

            startX = 0;
            endX = searchWidth - 1;
            startY++;
            endY++;
        }
        //}
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

        int[][] clonedBoard = cloneMatrix(board);
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
            int[][] clonedBoard = cloneMatrix(board);
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

    // Write data to file.
    private void writeDataToDBFile(String path, int winnerID, int id, long previousDraws, long previousLosses,
                                   long previousWins) {
        try {
            FileWriter fw = new FileWriter(path, false);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(Long.toString((winnerID == 0 ? 1 : 0) + previousDraws));    // Draw.
            bw.newLine();
            bw.write(Long.toString((winnerID == id ? 0 : 1) + previousLosses));   // Losses.
            bw.newLine();
            bw.write(Long.toString((winnerID == id ? 1 : 0) + previousWins));   // Wins.
            bw.flush();

            bw.close();
            fw.close();
        } catch (IOException ignored) {
        }
    }

    private void writeDataToDBMemory(int winnerID, int id, int previousDraws, int previousLosses, int previousWins) {
        // TODO: Fill with content.
    }

    private void writeDataToDBSQL(int winnerID, int id, int previousDraws, int previousLosses, int previousWins) {
        // TODO: Fill with content.
    }
}