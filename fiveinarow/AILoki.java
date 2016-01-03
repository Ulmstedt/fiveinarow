/**
 * Loki AI fiveinarow implementation
 *
 * AILoki.java
 * Created on 2015-12-28
 * Version 0.4.0 Beta
 *
 * Written by Jimmy Nordström.
 * © 2015-2016 Jimmy Nordström.
 *
 * Licenced under GNU GPLv3.
 * http://www.gnu.org/licenses/gpl-3.0.html
 *
 * If you have questions, contact me at pzyber@pzyber.net
 */

package fiveinarow;

import Pzyber.Loki.Gomoku.Loki;
import Pzyber.Loki.Gomoku.LokiResult;
import Pzyber.Loki.Gomoku.Utils;

import java.awt.Point;

public class AILoki extends Player implements IAI {
    private Loki loki;

    // Memory DB
    public AILoki(int id, Game game, boolean aggressive) {
        super(id, game);

        loki = new Loki(game.getWidth(), game.getHeight(), aggressive);
    }

    // Folder DB
    public AILoki(int id, Game game, boolean aggressive, String path) {
        super(id, game);

        loki = new Loki(path, game.getWidth(), game.getHeight(), aggressive);
    }

    @Override
    public void moveMade(Point move) {
        // Register move.
        loki.registerMoveInDB(Utils.changeToYXBoard(game.getBoard()));
    }

    @Override
    public void playRound() {
        // Get move.
        LokiResult lr = loki.thinkOfAMove(Utils.changeToYXBoard(game.getBoard()));

        // Get pointGrid.
        pointGrid = Utils.cloneMatrix(Utils.changeToYXBoard(lr.getCalculatedDataRoundedScaled(999999)));

        // Play move.
        Point move = lr.getMove();
        game.setTile(move.x, move.y, ID);
        game.incrementRoundCount();
        game.nextPlayer();
    }

    @Override
    public void roundEnded(int winner) {
        // Store registered moves to Loki DB.
        loki.storeGameDataInDB(winner);
    }
}