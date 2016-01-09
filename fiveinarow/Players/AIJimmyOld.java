/**
 * Old Gomoku AI ported to fiveinarow
 *
 * AIJimyOld.java
 * Last original C version was created on 2006-03-22
 * Ported to Java on 2016-01-02
 *
 * Written by Jimmy Nordström.
 * © 2006-2016 Jimmy Nordström.
 *
 * Licenced under GNU GPLv3.
 * http://www.gnu.org/licenses/gpl-3.0.html
 *
 * If you have questions, contact me at pzyber@pzyber.net
 */

package fiveinarow.Players;

import fiveinarow.Game.Game;
import java.awt.Point;
import java.util.Random;

public class AIJimmyOld extends Player implements IAI{

    Random random = new Random();

    public AIJimmyOld(int id, Game game) {
        super(id, game);
        int width = game.getWidth();
        int height = game.getHeight();
        pointGrid = new int[width][height];
    }

    @Override
    public void playRound() {
        // Convert board to array.
        int[] Spelplan = new int[game.getHeight() * game.getWidth()];
        int[][] board = game.getBoard();
        for(int y = 0; y < game.getHeight(); y++)
        {
            for(int x = 0; x < game.getWidth(); x++){
                if(board[x][y] == 0) {
                    Spelplan[x + (y * game.getWidth())] = 2;
                }else if(board[x][y] == 1){
                    Spelplan[x + (y * game.getWidth())] = 0;
                }else if(board[x][y] == 2){
                    Spelplan[x + (y * game.getWidth())] = 1;
                }
            }
        }

        // Play move.
        Point move = CalculateMove(Spelplan, getID() - 1, random);
        try{
            game.setTile(move.x, move.y, ID);
        }catch (ArrayIndexOutOfBoundsException ignored){
            Point p;
            do{
                p = new Point(random.nextInt(game.getWidth()), random.nextInt(game.getHeight()));
            }while(board[p.x][p.y] != 0);
        }
        game.incrementRoundCount();
        game.nextPlayer();
    }

    public static Point CalculateMove(int[] Spelplan, int id, Random random) {
        boolean Krav;
        int i, j, k, l, x, y;
        int Slumptal;
        int[] Sorterade = new int[16];
        int Temp;

        // Om inga möjligheter skulle finnas.
        do{
            x = random.nextInt(15);
            y = random.nextInt(15);
        }while(Spelplan[x + 15 * y] != 2);

        // Möjligheter (Nivå 1).
        try {
            for (i = 1; i >= 0; i--) {
                for (j = 0; j < 4; j++)
                    Sorterade[j] = 0;
                for (j = 0; j < 4; j++) {
                    do {
                        Krav = true;
                        Slumptal = random.nextInt(4) + 1;
                        for (k = 0; k < 4; k++) {
                            if (Slumptal == Sorterade[k])
                                Krav = false;
                        }
                    } while (!Krav);
                    Sorterade[j] = Slumptal;
                }
                for (j = 0; j < 4; j++) {
                    if (Sorterade[j] == 1) {
                        for (k = 2; k < 13; k++) {
                            for (l = 2; l < 13; l++) {
                                if (Spelplan[k + 15 * l] == i) {
                                    if (k == 0 && Spelplan[k + 1 + 15 * l] == 2) {
                                        x = k + 1;
                                        y = l;
                                    } else if (k == 14 && Spelplan[k - 1 + 15 * l] == 2) {
                                        x = k - 1;
                                        y = l;
                                    } else if (k != 0 && k != 14) {
                                        if (Spelplan[k - 1 + 15 * l] == 2 && Spelplan[k + 1 + 15 * l] == 2) {
                                            Slumptal = random.nextInt(2);
                                            if (Slumptal == 0) {
                                                x = k - 1;
                                                y = l;
                                            } else if (Slumptal == 1) {
                                                x = k + 1;
                                                y = l;
                                            }
                                        } else if (Spelplan[k - 1 + 15 * l] == 2 && Spelplan[k + 1 + 15 * l] != 2) {
                                            x = k - 1;
                                            y = l;
                                        } else if (Spelplan[k - 1 + 15 * l] != 2 && Spelplan[k + 1 + 15 * l] == 2) {
                                            x = k + 1;
                                            y = l;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 2) {
                        for (k = 2; k < 13; k++) {
                            for (l = 2; l < 13; l++) {
                                if (Spelplan[k + 15 * l] == i) {
                                    if (l == 0 && Spelplan[k + 15 * (l + 1)] == 2) {
                                        x = k;
                                        y = l + 1;
                                    } else if (l == 14 && Spelplan[k + 15 * (l - 1)] == 2) {
                                        x = k;
                                        y = l - 1;
                                    } else if (l != 0 && l != 14) {
                                        if (Spelplan[k + 15 * (l - 1)] == 2 && Spelplan[k + 15 * (l + 1)] == 2) {
                                            Slumptal = random.nextInt(2);
                                            if (Slumptal == 0) {
                                                x = k;
                                                y = l - 1;
                                            } else if (Slumptal == 1) {
                                                x = k;
                                                y = l + 1;
                                            }
                                        } else if (Spelplan[k + 15 * (l - 1)] == 2 && Spelplan[k + 15 * (l + 1)] != 2) {
                                            x = k;
                                            y = l - 1;
                                        } else if (Spelplan[k + 15 * (l - 1)] != 2 && Spelplan[k + 15 * (l + 1)] == 2) {
                                            x = k;
                                            y = l + 1;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 3) {
                        for (k = 2; k < 13; k++) {
                            for (l = 2; l < 13; l++) {
                                if (Spelplan[k + 15 * l] == i) {
                                    if ((k == 0 || l == 0) && Spelplan[k + 1 + 15 * (l + 1)] == 2) {
                                        x = k + 1;
                                        y = l + 1;
                                    } else if ((k == 14 || l == 14) && Spelplan[k - 1 + 15 * (l - 1)] == 2) {
                                        x = k - 1;
                                        y = l - 1;
                                    } else if ((k != 0 && k != 14) || (l != 0 && l != 14)) {
                                        if (Spelplan[k - 1 + 15 * (l - 1)] == 2 && Spelplan[k + 1 + 15 * (l + 1)] == 2) {
                                            Slumptal = random.nextInt(2);
                                            if (Slumptal == 0) {
                                                x = k - 1;
                                                y = l - 1;
                                            } else if (Slumptal == 1) {
                                                x = k + 1;
                                                y = l + 1;
                                            }
                                        } else if (Spelplan[k - 1 + 15 * (l - 1)] == 2 && Spelplan[k + 1 + 15 * (l + 1)] != 2) {
                                            x = k - 1;
                                            y = l - 1;
                                        } else if (Spelplan[k - 1 + 15 * (l - 1)] != 2 && Spelplan[k + 1 + 15 * (l + 1)] == 2) {
                                            x = k + 1;
                                            y = l + 1;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 4) {
                        for (k = 2; k < 13; k++) {
                            for (l = 2; l < 13; l++) {
                                if (Spelplan[k + 1 + 15 * l] == i) {
                                    if ((k == 0 || l == 14) && Spelplan[k + 1 + 15 * (l - 1)] == 2) {
                                        x = k + 1;
                                        y = l - 1;
                                    } else if ((k == 14 || l == 0) && Spelplan[k - 1 + 15 * (l + 1)] == 2) {
                                        x = k - 1;
                                        y = l + 1;
                                    } else if ((k != 0 && k != 14) && (l != 0 && l != 14)) {
                                        if (Spelplan[k - 1 + 15 * (l + 1)] == 2 && Spelplan[k + 1 + 15 * (l - 1)] == 2) {
                                            Slumptal = random.nextInt(2);
                                            if (Slumptal == 0) {
                                                x = k - 1;
                                                y = l + 1;
                                            } else if (Slumptal == 1) {
                                                x = k + 1;
                                                y = l - 1;
                                            }
                                        } else if (Spelplan[k - 1 + 15 * (l + 1)] == 2 && Spelplan[k + 1 + 15 * (l - 1)] != 2) {
                                            x = k - 1;
                                            y = l + 1;
                                        } else if (Spelplan[k - 1 + 15 * (l + 1)] != 2 && Spelplan[k + 1 + 15 * (l - 1)] == 2) {
                                            x = k + 1;
                                            y = l - 1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch(ArrayIndexOutOfBoundsException e){

        }

        // Möjligheter (Nivå 2).

        // Möjligheter (Nivå 3-2).
        try {
            for (i = 1; i >= 0; i--) {
                if (i == 1)
                    Temp = 0;
                else
                    Temp = 1;
                for (j = 0; j < 6; j++)
                    Sorterade[j] = 0;
                for (j = 0; j < 6; j++) {
                    do {
                        Krav = true;
                        Slumptal = random.nextInt(6) + 1;
                        for (k = 0; k < 6; k++) {
                            if (Slumptal == Sorterade[k])
                                Krav = false;
                        }
                    } while (!Krav);
                    Sorterade[j] = Slumptal;
                }
                for (j = 0; j < 6; j++) {
                    if (Sorterade[j] == 1) {
                        for (k = 1; k < 12; k++) {
                            for (l = 1; l < 12; l++) {
                                if ((Spelplan[k + 15 * l] == i && Spelplan[k + 2 + 15 * l] == i && Spelplan[k + 15 * (l + 2)] == i && Spelplan[k + 2 + 15 * (l + 2)] == i && Spelplan[k + 1 + 15 * (l + 1)] == 2) && (Spelplan[k - 1 + 15 * (l - 1)] != Temp && Spelplan[k + 3 + 15 * (l - 1)] != Temp && Spelplan[k - 1 + 15 * (l + 3)] != Temp && Spelplan[k + 3 + 15 * (l + 3)] != Temp)) {
                                    x = k + 1;
                                    y = l + 1;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 2) {
                        for (k = 1; k < 12; k++) {
                            for (l = 1; l < 12; l++) {
                                if ((Spelplan[k + 1 + 15 * l] == i && Spelplan[k + 15 * (l + 1)] == i && Spelplan[k + 2 + 15 * (l + 1)] == i && Spelplan[k + 1 + 15 * (l + 2)] == i && Spelplan[k + 1 + 15 * (l + 1)] == 2) && (Spelplan[k + 1 + 15 * (l - 1)] != Temp && Spelplan[k - 1 + 15 * (l + 2)] != Temp && Spelplan[k + 3 + 15 * (l + 1)] != Temp && Spelplan[k + 1 + 15 * (l + 3)] != Temp)) {
                                    x = k + 1;
                                    y = l + 1;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 3) {
                        for (k = 1; k < 12; k++) {
                            for (l = 1; l < 12; l++) {
                                if ((Spelplan[k + 1 + 15 * l] == i && Spelplan[k + 2 + 15 * l] == i && Spelplan[k + 15 * (l + 1)] == i && Spelplan[k + 15 * (l + 2)] == i && Spelplan[k + 15 * l] == 2) && (Spelplan[k + 3 + 15 * l] != Temp && Spelplan[k + 15 * (l + 3)] != Temp && Spelplan[k + 15 * (l - 1)] != Temp && Spelplan[k - 1 + 15 * l] != Temp)) {
                                    x = k;
                                    y = l;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 4) {
                        for (k = 1; k < 12; k++) {
                            for (l = 1; l < 12; l++) {
                                if ((Spelplan[k + 15 * l] == i && Spelplan[k + 1 + 15 * l] == i && Spelplan[k + 2 + 15 * (l + 1)] == i && Spelplan[k + 2 + 15 * (l + 2)] == i && Spelplan[k + 2 + 15 * l] == 2) && (Spelplan[k - 1 + 15 * l] != Temp && Spelplan[k + 2 + 15 * (l + 3)] != Temp && Spelplan[k + 2 + 15 * (l - 1)] != Temp && Spelplan[k + 3 + 15 * l] != Temp)) {
                                    x = k + 2;
                                    y = l;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 5) {
                        for (k = 1; k < 12; k++) {
                            for (l = 1; l < 12; l++) {
                                if ((Spelplan[k + 15 * l] == i && Spelplan[k + 15 * (l + 1)] == i && Spelplan[k + 1 + 15 * (l + 2)] == i && Spelplan[k + 2 + 15 * (l + 2)] == i && Spelplan[k + 15 * (l + 2)] == 2) && (Spelplan[k + 15 * (l - 1)] != Temp && Spelplan[k + 3 + 15 * (l + 2)] != Temp && Spelplan[k - 1 + 15 * (l + 2)] != Temp && Spelplan[k + 15 * (l + 3)] != Temp)) {
                                    x = k;
                                    y = l + 2;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 6) {
                        for (k = 1; k < 12; k++) {
                            for (l = 1; l < 12; l++) {
                                if ((Spelplan[k + 2 + 15 * l] == i && Spelplan[k + 2 + 15 * (l + 1)] == i && Spelplan[k + 15 * (l + 2)] == i && Spelplan[k + 1 + 15 * (l + 2)] == i && Spelplan[k + 2 + 15 * (l + 2)] == 2) && (Spelplan[k + 2 + 15 * (l - 1)] != Temp && Spelplan[k - 1 + 15 * (l + 2)] != Temp && Spelplan[k + 3 + 15 * (l + 2)] != Temp && Spelplan[k + 2 + 15 * (l + 3)] != Temp)) {
                                    x = k + 2;
                                    y = l + 2;
                                }
                            }
                        }
                    }
                }
            }
        }catch(ArrayIndexOutOfBoundsException e){

        }

        // Möjligter (Nivå 3-4).

        // Möjligheter (Nivå 3).
        try {
            for (i = 1; i >= 0; i--) {
                if (i == 1)
                    Temp = 0;
                else
                    Temp = 1;
                for (j = 0; j < 12; j++)
                    Sorterade[j] = 0;
                for (j = 0; j < 12; j++) {
                    do {
                        Krav = true;
                        Slumptal = random.nextInt(12) + 1;
                        for (k = 0; k < 12; k++) {
                            if (Slumptal == Sorterade[k])
                                Krav = false;
                        }
                    } while (!Krav);
                    Sorterade[j] = Slumptal;
                }
                for (j = 0; j < 12; j++) {
                    if (Sorterade[j] == 1) {
                        for (k = 0; k < 12; k++) {
                            for (l = 0; l < 15; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 1 + 15 * l] == i && Spelplan[k + 2 + 15 * l] == i) {
                                    if (k == 0 && Spelplan[k + 3 + 15 * l] == 2 && (Spelplan[k + 4 + 15 * l] == 2 || Spelplan[k + 4 + 15 * l] == Temp) && i != 1) {
                                        x = k + 3;
                                        y = l;
                                    } else if (k == 11 && Spelplan[k - 1 + 15 * l] == 2 && (Spelplan[k - 2 + 15 * l] == 2 || Spelplan[k - 2 + 15 * l] == Temp) && i != 1) {
                                        x = k - 1;
                                        y = l;
                                    } else if (k != 0 && k != 11) {
                                        if (Spelplan[k - 1 + 15 * l] == 2 && Spelplan[k + 3 + 15 * l] == 2) {
                                            Slumptal = random.nextInt(2);
                                            if (Slumptal == 0) {
                                                x = k - 1;
                                                y = l;
                                            } else if (Slumptal == 1) {
                                                x = k + 3;
                                                y = l;
                                            }
                                        } else if (Spelplan[k - 1 + 15 * l] == 2 && Spelplan[k - 2 + 15 * l] == 2 && (Spelplan[k + 3 + 15 * l] != 2 && Spelplan[k + 3 + 15 * l] != Temp)) {
                                            x = k - 1;
                                            y = l;
                                        } else if ((Spelplan[k - 1 + 15 * l] != 2 && Spelplan[k - 1 + 15 * l] != Temp) && Spelplan[k + 3 + 15 * l] == 2 && Spelplan[k + 4 + 15 * l] == 2) {
                                            x = k + 3;
                                            y = l;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 2) {
                        for (k = 0; k < 15; k++) {
                            for (l = 0; l < 12; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 15 * (l + 1)] == i && Spelplan[k + 15 * (l + 2)] == i) {
                                    if (j == 0 && Spelplan[k + 15 * (l + 3)] == 2 && (Spelplan[k + 15 * (l + 4)] == 2 || Spelplan[k + 15 * (l + 4)] == Temp) && i != 1) {
                                        x = k;
                                        y = l + 3;
                                    } else if (j == 11 && Spelplan[k + 15 * (l - 1)] == 2 && (Spelplan[k + 15 * (l - 2)] == 2 || Spelplan[k + 15 * (l - 2)] == Temp) && i != 1) {
                                        x = k;
                                        y = l - 1;
                                    } else if (j != 0 && j != 11) {
                                        if (Spelplan[k + 15 * (l - 1)] == 2 && Spelplan[k + 15 * (l + 3)] == 2) {
                                            Slumptal = random.nextInt(2);
                                            if (Slumptal == 0) {
                                                x = k;
                                                y = l - 1;
                                            } else if (Slumptal == 1) {
                                                x = k;
                                                y = l + 3;
                                            }
                                        } else if (Spelplan[k + 15 * (l - 1)] == 2 && Spelplan[k + 15 * (l - 2)] == 2 && (Spelplan[k + 15 * (l + 3)] != 2 && Spelplan[k + 15 * (l + 3)] != Temp)) {
                                            x = k;
                                            y = l - 1;
                                        } else if ((Spelplan[k + 15 * (l - 1)] != 2 && Spelplan[k + 15 * (l - 1)] != Temp) && Spelplan[k + 15 * (l + 3)] == 2 && Spelplan[k + 15 * (l + 4)] == 2) {
                                            x = k;
                                            y = l + 3;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 3) {
                        for (k = 0; k < 12; k++) {
                            for (l = 0; l < 12; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 1 + 15 * (l + 1)] == i && Spelplan[k + 2 + 15 * (l + 2)] == i) {
                                    if ((k == 0 && l == 0) && Spelplan[k + 3 + 15 * (l + 3)] == 2 && (Spelplan[k + 4 + 15 * (l + 4)] == 2 || Spelplan[k + 4 + 15 * (l + 4)] == Temp) && i != 1) {
                                        x = k + 3;
                                        y = l + 3;
                                    } else if ((k == 11 && k == 11) && Spelplan[k - 1 + 15 * (l - 1)] == 2 && (Spelplan[k - 2 + 15 * (l - 2)] == 2 || Spelplan[k - 2 + 15 * (l - 2)] == Temp) && i != 1) {
                                        x = k - 1;
                                        y = l - 1;
                                    } else if ((k != 0 && k != 11) || (l != 0 && l != 11)) {
                                        if (Spelplan[k - 1 + 15 * (l - 1)] == 2 && Spelplan[k + 3 + 15 * (l + 3)] == 2) {
                                            Slumptal = random.nextInt(2);
                                            if (Slumptal == 0) {
                                                x = k - 1;
                                                y = l - 1;
                                            } else if (Slumptal == 1) {
                                                x = k + 3;
                                                y = l + 3;
                                            }
                                        } else if (Spelplan[k - 1 + 15 * (l - 1)] == 2 && Spelplan[k - 2 + 15 * (l - 2)] == 2 && (Spelplan[k + 3 + 15 * (l + 3)] != 2 && Spelplan[k + 3 + 15 * (l + 3)] != Temp)) {
                                            x = k - 1;
                                            y = l - 1;
                                        } else if ((Spelplan[k - 1 + 15 * (l - 1)] != 2 && Spelplan[k - 2 + 15 * (l - 1)] != Temp) && Spelplan[k + 3 + 15 * (l + 3)] == 2 && Spelplan[k + 4 + 15 * (l + 4)] == 2) {
                                            x = k + 3;
                                            y = l + 3;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 4) {
                        for (k = 0; k < 12; k++) {
                            for (l = 0; l < 12; l++) {
                                if (Spelplan[k + 2 + 15 * l] == i && Spelplan[k + 1 + 15 * (l + 1)] == i && Spelplan[k + 15 * (l + 2)] == i) {
                                    if ((k == 0 && l == 11) && Spelplan[k + 3 + 15 * (l - 1)] == 2 && (Spelplan[k + 4 + 15 * (l - 2)] == 2 || Spelplan[k + 4 + 15 * (l - 2)] == Temp) && i != 1) {
                                        x = k + 3;
                                        y = l - 1;
                                    } else if ((k == 11 && k == 0) && Spelplan[k - 1 + 3 + 15 * (l + 3)] == 2 && (Spelplan[k - 2 + 15 * (l + 4)] == 2 || Spelplan[k - 2 + 15 * (l + 4)] == Temp) && i != 1) {
                                        x = k - 1;
                                        y = l + 3;
                                    } else if ((k != 0 && k != 11) || (l != 0 && l != 11)) {
                                        if (Spelplan[k + 3 + 15 * (l - 1)] == 2 && Spelplan[k - 1 + 15 * (l + 3)] == 2) {
                                            Slumptal = random.nextInt(2);
                                            if (Slumptal == 0) {
                                                x = k + 3;
                                                y = l - 1;
                                            } else if (Slumptal == 1) {
                                                x = k - 1;
                                                y = l + 3;
                                            }
                                        } else if (Spelplan[k + 3 + 15 * (l - 1)] == 2 && Spelplan[k + 4 + 15 * (l - 2)] == 2 && (Spelplan[k + 3 + 15 * (l + 3)] != 2 && Spelplan[k + 3 + 15 * (l + 3)] != Temp)) {
                                            x = k + 3;
                                            y = l - 1;
                                        } else if ((Spelplan[k + 3 + 15 * (l - 1)] != 2 && Spelplan[k + 3 + 15 * (l - 1)] != Temp) && Spelplan[k - 1 + 15 * (l + 3)] == 2 && Spelplan[k - 2 + 15 * (l + 4)] == 2) {
                                            x = k - 1;
                                            y = l + 3;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 5) {
                        for (k = 1; k < 12; k++) {
                            for (l = 1; l < 15; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 2 + 15 * l] == i && Spelplan[k + 3 + 15 * l] == i && Spelplan[k + 1 + 15 * l] == 2) {
                                    if (((Spelplan[k - 1 + 15 * l] == 2 || Spelplan[k + 4 + 15 * l] == 2) && i == 0) || ((Spelplan[k - 1 + 15 * l] == 2 && Spelplan[k + 4 + 15 * l] == 2) && i == 1)) {
                                        x = k + 1;
                                        y = l;
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 6) {
                        for (k = 1; k < 12; k++) {
                            for (l = 1; l < 15; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 1 + 15 * l] == i && Spelplan[k + 3 + 15 * l] == i && Spelplan[k + 2 + 15 * l] == 2) {
                                    if (((Spelplan[k - 1 + 15 * l] == 2 || Spelplan[k + 4 + 15 * l] == 2) && i == 0) || ((Spelplan[k - 1 + 15 * l] == 2 && Spelplan[k + 4 + 15 * l] == 2) && i == 1)) {
                                        x = k + 2;
                                        y = l;
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 7) {
                        for (k = 1; k < 15; k++) {
                            for (l = 1; l < 12; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 15 * (l + 2)] == i && Spelplan[k + 15 * (l + 3)] == i && Spelplan[k + 15 * (l + 1)] == 2) {
                                    if (((Spelplan[k + 15 * (l - 1)] == 2 || Spelplan[k + 15 * (l + 4)] == 2) && i == 0) || ((Spelplan[k + 15 * (l - 1)] == 2 && Spelplan[k + 15 * (l + 4)] == 2) && i == 1)) {
                                        x = k;
                                        y = l + 1;
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 8) {
                        for (k = 1; k < 15; k++) {
                            for (l = 1; l < 12; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 15 * (l + 1)] == i && Spelplan[k + 15 * (l + 3)] == i && Spelplan[k + 15 * (l + 2)] == 2) {
                                    if (((Spelplan[k + 15 * (l - 1)] == 2 || Spelplan[k + 15 * (l + 4)] == 2) && i == 0) || ((Spelplan[k + 15 * (l - 1)] == 2 && Spelplan[k + 15 * (l + 4)] == 2) && i == 1)) {
                                        x = k;
                                        y = l + 2;
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 9) {
                        for (k = 1; k < 12; k++) {
                            for (l = 1; l < 12; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 2 + 15 * (l + 2)] == i && Spelplan[k + 3 + 15 * (l + 3)] == i && Spelplan[k + 1 + 15 * (l + 1)] == 2) {
                                    if (((Spelplan[k - 1 + 15 * (l - 1)] == 2 || Spelplan[k + 4 + 15 * (l + 4)] == 2) && i == 0) || ((Spelplan[k - 1 + 15 * (l - 1)] == 2 && Spelplan[k + 4 + 15 * (l + 4)] == 2) && i == 1)) {
                                        x = k + 1;
                                        y = l + 1;
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 10) {
                        for (k = 1; k < 12; k++) {
                            for (l = 1; l < 12; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 1 + 15 * (l + 1)] == i && Spelplan[k + 3 + 15 * (l + 3)] == i && Spelplan[k + 2 + 15 * (l + 2)] == 2) {
                                    if (((Spelplan[k - 1 + 15 * (l - 1)] == 2 || Spelplan[k + 4 + 15 * (l + 4)] == 2) && i == 0) || ((Spelplan[k - 1 + 15 * (l - 1)] == 2 && Spelplan[k + 4 + 15 * (l + 4)] == 2) && i == 1)) {
                                        x = k + 2;
                                        y = l + 2;
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 11) {
                        for (k = 1; k < 12; k++) {
                            for (l = 1; l < 12; l++) {
                                if (Spelplan[k + 3 + 15 * l] == i && Spelplan[k + 1 + 15 * (l + 2)] == i && Spelplan[k + 15 * (l + 3)] == i && Spelplan[k + 2 + 15 * (l + 1)] == 2) {
                                    if (((Spelplan[k + 4 + 15 * (l - 1)] == 2 || Spelplan[k - 1 + 15 * (l + 4)] == 2) && i == 0) || ((Spelplan[k + 4 + 15 * (l - 1)] == 2 && Spelplan[k - 1 + 15 * (l + 4)] == 2) && i == 1)) {
                                        x = k + 2;
                                        y = l + 1;
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 12) {
                        for (k = 1; k < 12; k++) {
                            for (l = 1; l < 12; l++) {
                                if (Spelplan[k + 3 + 15 * l] == i && Spelplan[k + 2 + 15 * (l + 1)] == i && Spelplan[k + 15 * (l + 3)] == i && Spelplan[k + 1 + 15 * (l + 2)] == 2) {
                                    if (((Spelplan[k + 4 + 15 * (l - 1)] == 2 || Spelplan[k - 1 + 15 * (l + 4)] == 2) && i == 0) || ((Spelplan[k + 4 + 15 * (l - 1)] == 2 && Spelplan[k - 1 + 15 * (l + 4)] == 2) && i == 1)) {
                                        x = k + 1;
                                        y = l + 2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch(ArrayIndexOutOfBoundsException e){

        }

        // Möjligheter (Nivå 4).
        try {
            for (i = 1; i > -1; i--) {
                for (j = 0; j < 16; j++)
                    Sorterade[j] = 0;
                for (j = 0; j < 16; j++) {
                    do {
                        Krav = true;
                        Slumptal = random.nextInt(16) + 1;
                        for (k = 0; k < 16; k++) {
                            if (Slumptal == Sorterade[k])
                                Krav = false;
                        }
                    } while (!Krav);
                    Sorterade[j] = Slumptal;
                }
                for (j = 0; j < 16; j++) {
                    if (Sorterade[j] == 1) {
                        for (k = 0; k < 11; k++) {
                            for (l = 0; l < 15; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 1 + 15 * l] == i && Spelplan[k + 2 + 15 * l] == i && Spelplan[k + 3 + 15 * l] == i) {
                                    if (k == 0 && Spelplan[k + 4 + 15 * l] == 2) {
                                        x = k + 4;
                                        y = l;
                                    } else if (k == 10 && Spelplan[k - 1 + 15 * l] == 2) {
                                        x = k - 1;
                                        y = l;
                                    } else if (k != 0 && k != 10) {
                                        if (Spelplan[k - 1 + 15 * l] == 2 && Spelplan[k + 4 + 15 * l] == 2) {
                                            Slumptal = random.nextInt(2);
                                            if (Slumptal == 0) {
                                                x = k - 1;
                                                y = l;
                                            } else if (Slumptal == 1) {
                                                x = k + 4;
                                                y = l;
                                            }
                                        } else if (Spelplan[k - 1 + 15 * l] == 2 && Spelplan[k + 4 + 15 * l] != 2) {
                                            x = k - 1;
                                            y = l;
                                        } else if (Spelplan[k - 1 + 15 * l] != 2 && Spelplan[k + 4 + 15 * l] == 2) {
                                            x = k + 4;
                                            y = l;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 2) {
                        for (k = 0; k < 15; k++) {
                            for (l = 0; l < 11; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 15 * (l + 1)] == i && Spelplan[k + 15 * (l + 2)] == i && Spelplan[k + 15 * (l + 3)] == i) {
                                    if (l == 0 && Spelplan[k + 15 * (l + 4)] == 2) {
                                        x = k;
                                        y = l + 4;
                                    } else if (l == 10 && Spelplan[k + 15 * (l - 1)] == 2) {
                                        x = k;
                                        y = l - 1;
                                    } else if (l != 0 && l != 10) {
                                        if (Spelplan[k + 15 * (l - 1)] == 2 && Spelplan[k + 15 * (l + 4)] == 2) {
                                            Slumptal = random.nextInt(2);
                                            if (Slumptal == 0) {
                                                x = k;
                                                y = l - 1;
                                            } else if (Slumptal == 1) {
                                                x = k;
                                                y = l + 4;
                                            }
                                        } else if (Spelplan[k + 15 * (l - 1)] == 2 && Spelplan[k + 15 * (l + 4)] != 2) {
                                            x = k;
                                            y = l - 1;
                                        } else if (Spelplan[k + 15 * (l - 1)] != 2 && Spelplan[k + 15 * (l + 4)] == 2) {
                                            x = k;
                                            y = l + 4;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 3) {
                        for (k = 0; k < 11; k++) {
                            for (l = 0; l < 11; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 1 + 15 * (l + 1)] == i && Spelplan[k + 2 + 15 * (l + 2)] == i && Spelplan[k + 3 + 15 * (l + 3)] == i) {
                                    if ((k == 0 || l == 0) && Spelplan[k + 4 + 15 * (l + 4)] == 2) {
                                        x = k + 4;
                                        y = l + 4;
                                    } else if ((k == 10 || l == 10) && Spelplan[k - 1 + 15 * (l - 1)] == 2) {
                                        x = k - 1;
                                        y = l - 1;
                                    } else if ((k != 0 && k != 10) && (l != 0 && l != 10)) {
                                        if (Spelplan[k - 1 + 15 * (l - 1)] == 2 && Spelplan[k + 4 + 15 * (l + 4)] == 2) {
                                            Slumptal = random.nextInt(2);
                                            if (Slumptal == 0) {
                                                x = k - 1;
                                                y = l - 1;
                                            } else if (Slumptal == 1) {
                                                x = k + 4;
                                                y = l + 4;
                                            }
                                        } else if (Spelplan[k - 1 + 15 * (l - 1)] == 2 && Spelplan[k + 4 + 15 * (l + 4)] != 2) {
                                            x = k - 1;
                                            y = l - 1;
                                        } else if (Spelplan[k - 1 + 15 * (l - 1)] != 2 && Spelplan[k + 4 + 15 * (l + 4)] == 2) {
                                            x = k + 4;
                                            y = l + 4;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 4) {
                        for (k = 0; k < 11; k++) {
                            for (l = 0; l < 11; l++) {
                                if (Spelplan[k + 3 + 15 * l] == i && Spelplan[k + 2 + 15 * (l + 1)] == i && Spelplan[k + 1 + 15 * (l + 2)] == i && Spelplan[k + 15 * (l + 3)] == i) {
                                    if ((k == 0 || l == 10) && Spelplan[k + 4 + 15 * (l - 1)] == 2) {
                                        x = k + 4;
                                        y = l - 1;
                                    } else if ((k == 10 || l == 0) && Spelplan[k - 1 + 15 * (l + 4)] == 2) {
                                        x = k - 1;
                                        y = l + 4;
                                    } else if ((k != 0 && k != 10) && (l != 0 && l != 10)) {
                                        if (Spelplan[k + 4 + 15 * (l - 1)] == 2 && Spelplan[k - 1 + 15 * (l + 4)] == 2) {
                                            Slumptal = random.nextInt(2);
                                            if (Slumptal == 0) {
                                                x = k + 4;
                                                y = l - 1;
                                            } else if (Slumptal == 1) {
                                                x = k - 1;
                                                y = l + 4;
                                            }
                                        } else if (Spelplan[k + 4 + 15 * (l - 1)] == 2 && Spelplan[k - 1 + 15 * (l + 4)] != 2) {
                                            x = k + 4;
                                            y = l - 1;
                                        } else if (Spelplan[k + 4 + 15 * (l - 1)] != 2 && Spelplan[k - 1 + 15 * (l + 4)] == 2) {
                                            x = k - 1;
                                            y = l + 4;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (Sorterade[j] == 5) {
                        for (k = 0; k < 11; k++) {
                            for (l = 0; l < 15; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 2 + 15 * l] == i && Spelplan[k + 3 + 15 * l] == i && Spelplan[k + 4 + 15 * l] == i && Spelplan[k + 1 + 15 * l] == 2) {
                                    x = k + 1;
                                    y = l;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 6) {
                        for (k = 0; k < 11; k++) {
                            for (l = 0; l < 15; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 1 + 15 * l] == i && Spelplan[k + 3 + 15 * l] == i && Spelplan[k + 4 + 15 * l] == i && Spelplan[k + 2 + 15 * l] == 2) {
                                    x = k + 2;
                                    y = l;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 7) {
                        for (k = 0; k < 11; k++) {
                            for (l = 0; l < 15; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 1 + 15 * l] == i && Spelplan[k + 2 + 15 * l] == i && Spelplan[k + 4 + 15 * l] == i && Spelplan[k + 3 + 15 * l] == 2) {
                                    x = k + 3;
                                    y = l;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 8) {
                        for (k = 0; k < 15; k++) {
                            for (l = 0; l < 11; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 15 * (l + 2)] == i && Spelplan[k + 15 * (l + 3)] == i && Spelplan[k + 15 * (l + 4)] == i && Spelplan[k + 15 * (l + 1)] == 2) {
                                    x = k;
                                    y = l + 1;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 9) {
                        for (k = 0; k < 15; k++) {
                            for (l = 0; l < 11; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 15 * (l + 1)] == i && Spelplan[k + 15 * (l + 3)] == i && Spelplan[k + 15 * (l + 4)] == i && Spelplan[k + 15 * (l + 2)] == 2) {
                                    x = k;
                                    y = l + 2;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 10) {
                        for (k = 0; k < 15; k++) {
                            for (l = 0; l < 11; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 15 * (l + 1)] == i && Spelplan[k + 15 * (l + 2)] == i && Spelplan[k + 15 * (l + 4)] == i && Spelplan[k + 15 * (l + 3)] == 2) {
                                    x = k;
                                    y = l + 3;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 11) {
                        for (k = 0; k < 11; k++) {
                            for (l = 0; l < 11; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 2 + 15 * (l + 2)] == i && Spelplan[k + 3 + 15 * (l + 3)] == i && Spelplan[k + 4 + 15 * (l + 4)] == i && Spelplan[k + 1 + 15 * (l + 1)] == 2) {
                                    x = k + 1;
                                    y = l + 1;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 12) {
                        for (k = 0; k < 11; k++) {
                            for (l = 0; l < 11; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 1 + 15 * (l + 1)] == i && Spelplan[k + 3 + 15 * (l + 3)] == i && Spelplan[k + 4 + 15 * (l + 4)] == i && Spelplan[k + 2 + 15 * (l + 2)] == 2) {
                                    x = k + 2;
                                    y = l + 2;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 13) {
                        for (k = 0; k < 11; k++) {
                            for (l = 0; l < 11; l++) {
                                if (Spelplan[k + 15 * l] == i && Spelplan[k + 1 + 15 * (l + 1)] == i && Spelplan[k + 2 + 15 * (l + 2)] == i && Spelplan[k + 4 + 15 * (l + 4)] == i && Spelplan[k + 3 + 15 * (l + 3)] == 2) {
                                    x = k + 3;
                                    y = l + 3;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 14) {
                        for (k = 0; k < 11; k++) {
                            for (l = 0; l < 11; l++) {
                                if (Spelplan[k + 4 + 15 * l] == i && Spelplan[k + 2 + 15 * (l + 2)] == i && Spelplan[k + 1 + 15 * (l + 3)] == i && Spelplan[k + 15 * (l + 4)] == i && Spelplan[k + 3 + 15 * (l + 1)] == 2) {
                                    x = k + 3;
                                    y = l + 1;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 15) {
                        for (k = 0; k < 11; k++) {
                            for (l = 0; l < 11; l++) {
                                if (Spelplan[k + 4 + 15 * l] == i && Spelplan[k + 3 + 15 * (l + 1)] == i && Spelplan[k + 1 + 15 * (l + 3)] == i && Spelplan[k + 15 * (l + 4)] == i && Spelplan[k + 2 + 15 * (l + 2)] == 2) {
                                    x = k + 2;
                                    y = l + 2;
                                }
                            }
                        }
                    } else if (Sorterade[j] == 16) {
                        for (k = 0; k < 11; k++) {
                            for (l = 0; l < 11; l++) {
                                if (Spelplan[k + 4 + 15 * l] == i && Spelplan[k + 3 + 15 * (l + 1)] == i && Spelplan[k + 2 + 15 * (l + 2)] == i && Spelplan[k + 15 * (l + 4)] == i && Spelplan[k + 1 + 15 * (l + 3)] == 2) {
                                    x = k + 1;
                                    y = l + 3;
                                }
                            }
                        }
                    }
                }
            }
        }catch(ArrayIndexOutOfBoundsException e){

        }

        // Möjligheter (Nivå 0).
        try {
            if (Spelplan[6 + 15 * 6] == 2 && Spelplan[8 + 15 * 6] == 2 && Spelplan[6 + 15 * 8] == 2 && Spelplan[8 + 15 * 8] == 2) {
                Slumptal = random.nextInt(4);
                switch (Slumptal) {
                    case 0:
                        x = 6;
                        y = 6;
                        break;
                    case 1:
                        x = 8;
                        y = 6;
                        break;
                    case 2:
                        x = 6;
                        y = 8;
                        break;
                    case 3:
                        x = 8;
                        y = 8;
                        break;
                }
            }
        }catch(ArrayIndexOutOfBoundsException e){

        }

        // Möjligheter mitten.
        if (Spelplan[7 + 15 * 7] == 2) {
            x = 7;
            y = 7;
        }

        return new Point(x, y);
    }
}