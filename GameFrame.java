package fiveinarow;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author Sehnsucht
 */
public class GameFrame extends JFrame {

    GameComponent gameComponent;
    Game game;
    //JPanel gamePanel;

    public GameFrame(Game game) {
        super("2DGamer");
        this.game = game;
        this.gameComponent = new GameComponent(game);

        this.setLayout(new BorderLayout());

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.game.addGameListener(gameComponent);

        this.add(gameComponent);
        
        this.setResizable(false);
        this.pack();
        this.setLocation(800, 400);
        this.setVisible(true);
    }

    private void createMenus() {
        final JMenu file = new JMenu("File");
        final JMenuItem quit = new JMenuItem("Quit");
        final JMenuBar bar = new JMenuBar();

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == quit) {
                    System.exit(0);
                }
            }
        };

        quit.addActionListener(al);
        file.add(quit);
        bar.add(file);

        this.setJMenuBar(bar);

    }
}
