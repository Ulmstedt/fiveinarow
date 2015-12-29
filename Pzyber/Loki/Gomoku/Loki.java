/** Loki Learning AI
 *
 * Loki.java
 * Version Alpha
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Loki {
    private final boolean DEBUG = true;

    private int maxID;
    private int width, height;
    private List<GameData> gameData = new ArrayList<>();
    private Random random = new Random();
    private String path;

    //public Loki(String path, int width, int height, int maxID) {
    public Loki(String path, int size, int maxID) {
        this.path = path;
        //this.width = width;
        //this.height = height;
        this.width = size;
        this.height = size ;
        this.maxID = maxID;
    }

    private String calculateHash(int[][] board, int flippidwith, int startX, int startY, int endX, int endY) {
        String stringboard = getBoardAsString(board, flippidwith, startX, startY, endX, endY);

        return stringboard;

        /*try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(stringboard.getBytes("UTF-16"));
            byte[] digest = md.digest();
            System.out.println(new String(digest, "UTF-16"));
            return new String(digest, "UTF-16");
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }*/
    }

    protected int[][] cloneMatrix(int[][] matrix)
    {
        int[][] newMatrix = new int[matrix.length][];

        for(int i = 0; i < matrix.length; i++) {
            newMatrix[i] = matrix[i].clone();
        }

        return newMatrix;
    }

    private String getBoardAsString(int[][] board, int flippidby, int startX, int startY, int endX, int endY){
        String result = "";

        for(int i = startX; i <= endX; i++)
        {
            for(int j = startY; j <= endY; j++)
            {
                int value = board[i][j];

                // Flipp id.
                if(flippidby > 0)
                {
                    value += flippidby;
                    if(value > maxID)
                    {
                        value = value - maxID;
                    }
                }

                result += Integer.toString(value);
            }
        }

        return result;
    }

    private ArrayList<MoveData> getDataFromDB(int[][] board, int id, int searchWidth, int searchHeight) {
        ArrayList<MoveData> data = new ArrayList<MoveData>();

        // Search for patterns in DB.
        for(int i = 0; i < 4; i++) { // Flip board loop.
            // Rotate board.
            for(int j = 4 - i; j < 4; j++)
            {
                board = rotateBoardClockwise(board);
            }

            for(int j = 0; j < maxID; j++) { // Flip id loop.
                int startX = 0;
                int endX = searchWidth - 1;
                int startY = 0;
                int endY = searchHeight - 1;
                while (endY < height - 1) {
                    while (endX < width - 1) {
                        // Calculate hash.
                        String hash = calculateHash(board, j, startX, startY, endX, endY);

                        String baseHashPath = path + "/" + hash;
                        if(Files.exists(Paths.get(baseHashPath))) {
                            // Get list of available moves from Loki DB.
                            File lokiDB = new File(baseHashPath);
                            String[] availableMoves = lokiDB.list();
                            if (availableMoves != null) {
                                for (String m : availableMoves) {
                                    // Get inner move.
                                    String[] posDot = m.split("\\.");
                                    String[] pos = posDot[0].split("_");
                                    Point innerMove = new Point(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));

                                    // Rotate move.
                                    if(i != 1) {
                                        for (int k = 4 - i + 1; k < 4; k++) {
                                            innerMove = rotateMoveAntiClockwise(innerMove);
                                        }
                                    }
                                    else
                                    {
                                        for (int k = 4 - i; k < 4; k++) {
                                            innerMove = rotateMoveClockwise(innerMove);
                                        }
                                    }

                                    // Scale move to board.
                                    Point move = new Point(startX + innerMove.x, startY + innerMove.y);

                                    // Get draws, losses and wins.
                                    int[] dbData = readDataFromDBFile(baseHashPath + "/" + m);
                                    int draws = dbData[0];
                                    int losses = dbData[1];
                                    int wins = dbData[2];

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

    private int[] readDataFromDBFile(String path){
        // Get draws, losses and wins.
        int draws = 0;
        int losses = 0;
        int wins = 0;
        try {
            List<String> rows = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            draws = Integer.parseInt(rows.get(0));
            losses = Integer.parseInt(rows.get(1));
            wins = Integer.parseInt(rows.get(2));
        } catch (IOException e) {
        }

        int[] ret = {draws, losses, wins};
        return ret;
    }

    public void registerMoveInDB(int[][] board, Point move)
    {
        System.out.println("Loki: Registering move in DB: " + move.x + ":" + move.y + " ...");

        // Clone board.
        int[][] clonedBoard = cloneMatrix(board);

        // Get player ID.
        int id = clonedBoard[move.x][move.y];

        // Reverse recent move.
        clonedBoard[move.x][move.y] = 0;

        // Store previous board and the new move.
        gameData.add(new GameData(clonedBoard, move, id));

        System.out.println("Loki: Move registered.");
    }

    // TODO: Fix support for rectangular board rotation.
    private int[][] rotateBoardClockwise(int[][] board){
        int size = width - 1;
        int[][] rotated = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rotated[i][j] = board[size - j - 1][i];
            }
        }

        return rotated;
    }

    // TODO: Fix support for rectangular move rotation.
    // x = y, y = (size - 1) - x
    private Point rotateMoveAntiClockwise(Point move){
        return new Point(move.y, height - 2 - move.x);
    }

    // TODO: Fix support for rectangular move rotation.
    // x = (size - 1) - y, y = x
    private Point rotateMoveClockwise(Point move){
        return new Point(width - 2 - move.y, move.x);
    }

    private void storeDataInDB(GameData gd, int winnerID, int searchWidth, int searchHeight) {
        int[][] board = gd.getBoard();
        Point move = gd.getMove();
        int id = gd.getID();

        // TODO: Fix broken rotation.
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
            while (endY < height - 1) {
                while (endX < width - 1) {
                    if(move.x >= startX && move.x <= endX  && move.y >= startY && move.y <= endY) {
                        System.out.println("startX="+startX+" endX="+endX+" move.x="+move.x+" startY="+startY+" move.y="+move.y+" endY="+endY);
                        // Calculate hash.
                        String hash = calculateHash(board, 0, startX, startY, endX, endY);

                        // Descale move to board.
                        move = new Point(move.x - startX, move.y - startY);

                        // Store data to database.
                        String filePath = path + "/" + hash + "/" + move.x + "_" + move.y + ".txt";
                        if(Files.exists(Paths.get(path + "/" + hash))) {
                            if(Files.exists(Paths.get(filePath))){
                                // Get draws, losses and wins.
                                int[] dbData = readDataFromDBFile(filePath);
                                int draws = dbData[0];
                                int losses = dbData[1];
                                int wins = dbData[2];

                                // Write data to file.
                                System.out.println("Loki DEBUG store filePath: " + filePath); // TODO: REMOVE DEBUG
                                writeDataToDBFile(filePath, winnerID, id, draws, losses, wins);
                            }
                            else
                            {
                                // Write data to file.
                                System.out.println("Loki DEBUG store filePath: " + filePath); // TODO: REMOVE DEBUG
                                writeDataToDBFile(filePath, winnerID, id, 0, 0, 0);
                            }
                        }
                        else
                        {
                            // Create hashed folder.
                            File hashedFolder = new File(path + "/" + hash);
                            hashedFolder.mkdirs();

                            // Write data to file.
                            System.out.println("Loki DEBUG store filePath: " + filePath); // TODO: REMOVE DEBUG
                            writeDataToDBFile(filePath, winnerID, id, 0, 0, 0);
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
    //}

    // The actual learning mechanism. Requires that all moves are registered with registerMoveInDB.
    public void storeGameDataInDB(int winnerID){
        // Start time of start of storing process.
        System.out.println("Loki: Storing game data in DB...");
        long startTime = System.currentTimeMillis();

        for(GameData gd : gameData){
            //int searchWidth = width;
            //int searchHeight = height;
            int searchWidth = 15;
            int searchHeight = 15;
            //while(searchWidth > 1 && searchHeight > 1)
            //{
                storeDataInDB(gd, winnerID, searchWidth, searchHeight);

                //searchWidth--;
                //searchHeight--;
            //}
        }

        gameData.clear();

        // End time of storing process.
        long endTime = System.currentTimeMillis();
        long timeSpent = endTime - startTime;
        System.out.println("Loki: Game data stored in " + timeSpent + " ms.");
    }

    public LokiResult thinkOfAMove(int[][] board){
        // Start time of AI's turn.
        System.out.println("Loki: Thinking out a move...");
        long startTime = System.currentTimeMillis();

        int[][] clonedBoard = cloneMatrix(board);
        LokiResult ret = thinkOfAMove(clonedBoard, 1);

        // End time of AI's turn.
        long endTime = System.currentTimeMillis();
        long timeSpent = endTime - startTime;
        Point move = ret.getMove();
        System.out.println("Loki: Move " + move.x + ":" + move.y + " chosen in " + timeSpent + " ms.");

        return ret;
    }

    private LokiResult thinkOfAMove(int[][] board, int id) {
        Map<Point, MoveData> data = new HashMap<>();

        // Search for patterns and add statistics to data.
        // TODO: Add rectangular pattern search.
        // TODO: Add search limits.
        //int searchWidth = width;
        //int searchHeight = height;
        int searchWidth = 15;
        int searchHeight = 15;
        ArrayList<MoveData> resultData;
        //while(searchWidth > 1 && searchHeight > 1)
        //{
            int[][] clonedBoard = cloneMatrix(board);
            resultData = getDataFromDB(clonedBoard, id, searchWidth, searchHeight);

            if(resultData != null) {
                for (MoveData md : resultData) {
                    Point move = md.getMove();
                    if (data.containsKey(move)) {
                        MoveData existingMoveData = data.get(move);

                        existingMoveData.addDraws(md.getDraws());
                        existingMoveData.addLosses(md.getLosses());
                        existingMoveData.addWins(md.getWins());
                    } else {
                        data.put(move, md);
                    }
                }
            }

            //searchWidth--;
            //searchHeight--;
       // }

        // Build board of conceived data.
        float bestThoughtResult = 0;
        float[][] thoughtData = new float[width][height];
        ArrayList<Point> bestMoves = new ArrayList<>();
        for(int i = 0; i < width - 1; i++)
        {
            for(int j = 0; j < height - 1; j++)
            {
                // Get result for given point.
                Point coord = new Point(i, j);
                float thoughtResult = 0;
                if(data.containsKey(coord)){
                    thoughtResult = data.get(coord).thoughtResult();
                }
                thoughtData[i][j] = thoughtResult;
                if(thoughtResult > 0) {
                    System.out.println("Loki DEBUG thoghtResult " + i + " : " + j + " = " + thoughtResult);
                }

                // Check if better move.
                if(board[i][j] == 0) {
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

        System.out.println("Loki DEBUG Result percentage: " + bestThoughtResult);

        // Select the best move and return a LokiResult, if more then one then randomly select one of them.
        Point bestMove = bestMoves.get(random.nextInt(bestMoves.size()));
        return new LokiResult(bestMove, thoughtData, width, height);
    }

    private void writeDataToDBFile(String path, int winnerID, int id, int previousDraws, int previousLosses, int previousWins)
    {
        // Write data to file.
        try {
            FileWriter fw = new FileWriter(path, false);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(Integer.toString((winnerID == 0 ? 1 : 0) + previousDraws));    // Draw.
            bw.newLine();
            bw.write(Integer.toString((winnerID == id ? 0 : 1) + previousLosses));   // Losses.
            bw.newLine();
            bw.write(Integer.toString((winnerID == id ? 1 : 0) + previousWins));   // Wins.
            bw.flush();

            bw.close();
            fw.close();
        } catch (IOException e) {
        }
    }
}