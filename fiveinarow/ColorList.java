package fiveinarow;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Sehnsucht
 */
public final class ColorList {
    public static ArrayList<Color> colors = new ArrayList<Color>();
    
    
    public static void initColors(){
        colors.add(Color.RED);
        colors.add(new Color(0,200,0));
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.BLUE);
        colors.add(Color.ORANGE);
        colors.add(Color.PINK);
        colors.add(Color.magenta);
        colors.add(Color.yellow);
    }
}
