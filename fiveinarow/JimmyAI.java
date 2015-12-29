package fiveinarow.fiveinarow;

import Pzyber.Loki.Gomoku.Loki;
import Pzyber.Loki.Gomoku.LokiResult;

import java.awt.*;

public class JimmyAI extends Player {

    private Loki loki;

    public JimmyAI(int id, Game game) {
        super(id, game);
        int width = game.getWidth();
        int height = game.getHeight();
        pointGrid = new int[width][height];

        // Add new Loki.
        this.loki = game.getLoki();
    }

    @Override
    public void playRound(){
        // Let Loki decide what move to play.
        int[][] board = game.getBoard();
        LokiResult lr = loki.thinkOfAMove(board);
        Point move = lr.getMove();
        game.setTile(move.x, move.y, ID);
        game.incrementRoundCount();
        game.nextPlayer();
    }
}