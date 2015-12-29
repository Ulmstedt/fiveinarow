/** Loki Learning AI
 *
 * LokiResult.java
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

public class LokiResult {
    private float[][] calculatedData;
    private int width, height;
    private Point move;

    public LokiResult(Point move, float[][] calculatedData, int width, int height){
        this.move = move;
        this.calculatedData = calculatedData;
        this.width = width;
        this.height = height;
    }

    public float[][] getCalculatedData(){
        return calculatedData;
    }

    public int[][] getCalculatedDataRounded(){
        return getCalculatedDataRoundedScaled(0);
    }

    public int[][] getCalculatedDataRoundedScaled(int scale){
        int[][] calculatedDataRounded = new int[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                calculatedDataRounded[i][j] = Math.round(calculatedData[i][j] * scale);
            }
        }
        return calculatedDataRounded;
    }

    public Point getMove(){
        return move;
    }
}
