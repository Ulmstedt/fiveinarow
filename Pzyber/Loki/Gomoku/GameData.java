/** Loki Learning AI
 *
 * GameData.java
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

public class GameData {
    private int id;
    private int[][] board;
    private Point move;

    public GameData(int[][] board, Point move, int id){
        this.board = board;
        this.move = move;
        this.id = id;
    }

    public int[][] getBoard(){
        return board;
    }

    public Point getMove(){
        return move;
    }

    public int getID(){
        return id;
    }
}
