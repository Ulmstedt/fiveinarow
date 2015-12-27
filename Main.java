/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fiveinarow;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;

/**
 *
 * @author Sehnsucht
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final Game game = new Game(11, 11);
        new GameFrame(game);

        final Action tick = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.tick();
            }
        };

        final Timer clockTimer = new Timer(10, tick);
        clockTimer.setCoalesce(true);
        clockTimer.start();
    }

}
