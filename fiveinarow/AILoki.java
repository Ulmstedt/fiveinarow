package fiveinarow;

import Pzyber.Loki.Gomoku.Loki;
import Pzyber.Loki.Gomoku.LokiResult;

import java.awt.*;

public class AILoki extends Player implements IAI{
    private Loki loki;

    public AILoki(int id, Game game) {
        super(id, game);
        int width = game.getWidth();
        int height = game.getHeight();
        pointGrid = new int[width][height];

        // Add new Loki.
        loki = new Loki(width, game.getNumberOfPlayers());
    }

    @Override
    public void moveMade(Point move) {
        loki.registerMoveInDB(game.getBoard(), move);
    }

    @Override
    public void playRound(){
        // Let Loki decide what move to play.
        int[][] board = game.getBoard();
        LokiResult lr = loki.thinkOfAMove(board);
        pointGrid = lr.getCalculatedDataRoundedScaled(999999);
        Point move = lr.getMove();
        game.setTile(move.x, move.y, ID);
        game.incrementRoundCount();
        game.nextPlayer();
    }

    @Override
    public void roundEnded(int winner){
        loki.storeGameDataInDB(winner);
    }
}