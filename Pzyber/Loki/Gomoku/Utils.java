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
