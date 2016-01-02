import Pzyber.Loki.Gomoku.Utils;

import java.awt.Point;

/**
 * Created by pzyber on 2015-12-31.
 */
public class Test_Utils_rotateMoveClockwise {
    public static void main(String[] args) {
        Point move, result;
        String test;

        System.out.println("TEST 1");
        move = new Point(0,0);
        //--------------------------------------------------------------------------------------------------------------
        result = new Point(4,0);
        test = move.x + "," + move.y + " -> " + result.x + "," + result.y;
        move = Utils.rotateMoveClockwise(move, 5);
        System.out.println(test + " " +(move.x == result.x && move.y == result.y ?  "PASSED" : "FAILED"));
        System.out.flush();
        if(!(move.x == result.x && move.y == result.y)) return;
        //--------------------------------------------------------------------------------------------------------------
        result = new Point(4,4);
        test = move.x + "," + move.y + " -> " + result.x + "," + result.y;
        move = Utils.rotateMoveClockwise(move, 5);
        System.out.println(test + " " +(move.x == result.x && move.y == result.y ?  "PASSED" : "FAILED"));
        System.out.flush();
        if(!(move.x == result.x && move.y == result.y)) return;
        //--------------------------------------------------------------------------------------------------------------
        result = new Point(0,4);
        test = move.x + "," + move.y + " -> " + result.x + "," + result.y;
        move = Utils.rotateMoveClockwise(move, 5);
        System.out.println(test + " " +(move.x == result.x && move.y == result.y ?  "PASSED" : "FAILED"));
        System.out.flush();
        if(!(move.x == result.x && move.y == result.y)) return;
        //--------------------------------------------------------------------------------------------------------------
        result = new Point(0,0);
        test = move.x + "," + move.y + " -> " + result.x + "," + result.y;
        move = Utils.rotateMoveClockwise(move, 5);
        System.out.println(test + " " +(move.x == result.x && move.y == result.y ?  "PASSED" : "FAILED"));
        System.out.flush();
        if(!(move.x == result.x && move.y == result.y)) return;
        //--------------------------------------------------------------------------------------------------------------

        System.out.println("TEST 2");
        move = new Point(1,1);
        //--------------------------------------------------------------------------------------------------------------
        result = new Point(3,1);
        test = move.x + "," + move.y + " -> " + result.x + "," + result.y;
        move = Utils.rotateMoveClockwise(move, 5);
        System.out.println(test + " " +(move.x == result.x && move.y == result.y ?  "PASSED" : "FAILED"));
        System.out.flush();
        if(!(move.x == result.x && move.y == result.y)) return;
        //--------------------------------------------------------------------------------------------------------------
        result = new Point(3,3);
        test = move.x + "," + move.y + " -> " + result.x + "," + result.y;
        move = Utils.rotateMoveClockwise(move, 5);
        System.out.println(test + " " +(move.x == result.x && move.y == result.y ?  "PASSED" : "FAILED"));
        System.out.flush();
        if(!(move.x == result.x && move.y == result.y)) return;
        //--------------------------------------------------------------------------------------------------------------
        result = new Point(1,3);
        test = move.x + "," + move.y + " -> " + result.x + "," + result.y;
        move = Utils.rotateMoveClockwise(move, 5);
        System.out.println(test + " " +(move.x == result.x && move.y == result.y ?  "PASSED" : "FAILED"));
        System.out.flush();
        if(!(move.x == result.x && move.y == result.y)) return;
        //--------------------------------------------------------------------------------------------------------------
        result = new Point(1,1);
        test = move.x + "," + move.y + " -> " + result.x + "," + result.y;
        move = Utils.rotateMoveClockwise(move, 5);
        System.out.println(test + " " +(move.x == result.x && move.y == result.y ?  "PASSED" : "FAILED"));
        System.out.flush();
        if(!(move.x == result.x && move.y == result.y)) return;
        //--------------------------------------------------------------------------------------------------------------

        System.out.println("TEST 3");
        move = new Point(2,0);
        //--------------------------------------------------------------------------------------------------------------
        result = new Point(4,2);
        test = move.x + "," + move.y + " -> " + result.x + "," + result.y;
        move = Utils.rotateMoveClockwise(move, 5);
        System.out.println(test + " " +(move.x == result.x && move.y == result.y ?  "PASSED" : "FAILED"));
        System.out.flush();
        if(!(move.x == result.x && move.y == result.y)) return;
        //--------------------------------------------------------------------------------------------------------------
        result = new Point(2,4);
        test = move.x + "," + move.y + " -> " + result.x + "," + result.y;
        move = Utils.rotateMoveClockwise(move, 5);
        System.out.println(test + " " +(move.x == result.x && move.y == result.y ?  "PASSED" : "FAILED"));
        System.out.flush();
        if(!(move.x == result.x && move.y == result.y)) return;
        //--------------------------------------------------------------------------------------------------------------
        result = new Point(0,2);
        test = move.x + "," + move.y + " -> " + result.x + "," + result.y;
        move = Utils.rotateMoveClockwise(move, 5);
        System.out.println(test + " " +(move.x == result.x && move.y == result.y ?  "PASSED" : "FAILED"));
        System.out.flush();
        if(!(move.x == result.x && move.y == result.y)) return;
        //--------------------------------------------------------------------------------------------------------------
        result = new Point(2,0);
        test = move.x + "," + move.y + " -> " + result.x + "," + result.y;
        move = Utils.rotateMoveClockwise(move, 5);
        System.out.println(test + " " +(move.x == result.x && move.y == result.y ?  "PASSED" : "FAILED"));
        System.out.flush();
        if(!(move.x == result.x && move.y == result.y)) return;
        //--------------------------------------------------------------------------------------------------------------

        System.out.println("DONE");
    }
}
