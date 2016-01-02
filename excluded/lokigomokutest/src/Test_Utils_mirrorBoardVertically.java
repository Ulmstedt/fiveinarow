import Pzyber.Loki.Gomoku.Utils;

/**
 * Created by pzyber on 2016-01-01.
 */
public class Test_Utils_mirrorBoardVertically {
    public static void main(String[] args) {
        boolean equal;
        int size;
        int[][] board, goal, result;

        System.out.println("TEST 1");
        //--------------------------------------------------------------------------------------------------------------
        board = new int[][] {{1, 0, 0, 0, 0},
                             {0, 1, 0, 0, 0},
                             {0, 0, 1, 0, 0},
                             {0, 1, 2, 2, 0},
                             {1, 0, 0, 0, 2}};
        goal = new int[][] {{0, 0, 0, 0, 1},
                            {0, 0, 0, 1, 0},
                            {0, 0, 1, 0, 0},
                            {0, 2, 2, 1, 0},
                            {2, 0, 0, 0, 1}};
        size = 5;
        equal = true;
        for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
                System.out.print(goal[y][x]);
            }
            System.out.print("\n");
        }
        System.out.println("?");
        result = Utils.mirrorBoardVertically(board, size);
        for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
                if(goal[y][x] != result[y][x]){
                    equal = false;
                }
                System.out.print(result[y][x]);
            }
            System.out.print("\n");
        }
        System.out.println(equal ? "PASSED" : "FAILED");
        System.out.flush();
        if (!result.equals(goal)) return;

        System.out.println("DONE");
    }
}
