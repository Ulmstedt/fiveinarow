/**
 * Loki Learning AI
 *
 * Utils.java
 * Created on 2015-12-30
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

public class Utils {
    public static String calculateHash(int[][] board, int flippidwith, int startX, int startY, int endX, int endY, int maxID) {
        return getBoardAsString(board, flippidwith, startX, startY, endX, endY, maxID);

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

    public static int[][] cloneMatrix(int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][];

        for (int i = 0; i < matrix.length; i++) {
            newMatrix[i] = matrix[i].clone();
        }

        return newMatrix;
    }

    private static String getBoardAsString(int[][] board, int flippidby, int startX, int startY, int endX, int endY, int maxID) {
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

    public static boolean hasAdjecentMoveOrFullSize(int[][] board, int startX, int startY, int endX, int endY, int width, int height) {
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

    public static int[][] rotateBoardClockwise(int[][] board, int size) {
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
    public static Point rotateMoveAntiClockwise(Point move, int size) {
        return new Point(move.y, size - 1 - move.x);
    }

    // x = (size - 1) - y, y = x
    @SuppressWarnings("SuspiciousNameCombination")
    public static Point rotateMoveClockwise(Point move, int size) {
        return new Point(size - 1 - move.y, move.x);
    }

    public static Point scaleAndRotate(Point move, int startX, int startY, int rotations, int size) {
        // Scale move to board.
        move = new Point(startX + move.x, startY + move.y);

        // Rotate move.
        if (rotations != 1) {
            for (int k = 4 - rotations + 1; k < 4; k++) {
                move = Utils.rotateMoveAntiClockwise(move, size);
            }
        } else {
            for (int k = 4 - rotations; k < 4; k++) {
                move = Utils.rotateMoveClockwise(move, size);
            }
        }

        return move;
    }
}
