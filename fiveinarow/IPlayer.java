package fiveinarow;

import java.awt.Point;

/**
 * Created by pzyber on 2015-12-30.
 */
public interface IPlayer {
    int getID();
    int getScore();
    void givePoint();
    void moveMade(Point move);
    void playRound();
    void resetScore();
    void roundEnded(int winner);
    void setCurrentChoice(int x, int y);
}
