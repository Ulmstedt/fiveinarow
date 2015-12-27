/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.orange);
    }
}
