package ru.tohaman.rubicsguide.blind;

/**
 * Created by Toha on 20.06.2017.
 */

public class BasicMoves {

    public BasicMoves(){

    }

    public static int[] Change2 (int[] cube, int one, int two) {
        int buf = cube[one];
        cube[one] = cube[two];
        cube[two] = buf;
        return cube;
    }

    public static int[] MoveR (int[] cube) {
        //Меняем фронт
        Change2 (cube, 20, 2);
        Change2 (cube, 23, 5);
        Change2 (cube, 26, 8);
        Change2 (cube, 20, 42);
        Change2 (cube, 23, 39);
        Change2 (cube, 26, 36);
        Change2 (cube, 20, 47);
        Change2 (cube, 23, 50);
        Change2 (cube, 26, 53);
        //Меняем бок
        Change2 (cube, 27, 29);
        Change2 (cube, 27, 35);
        Change2 (cube, 27, 33);
        Change2 (cube, 30, 28);
        Change2 (cube, 30, 32);
        Change2 (cube, 30, 34);
        return cube;
    }

    public static int[] MoveRb(int [] cube) {
        //Меняем фронт
        Change2 (cube, 20, 47);
        Change2 (cube, 23, 50);
        Change2 (cube, 26, 53);
        Change2 (cube, 20, 42);
        Change2 (cube, 23, 39);
        Change2 (cube, 26, 36);
        Change2 (cube, 20, 2);
        Change2 (cube, 23, 5);
        Change2 (cube, 26, 8);
        //Меняем бок
        Change2 (cube, 27, 33);
        Change2 (cube, 27, 35);
        Change2 (cube, 27, 29);
        Change2 (cube, 30, 34);
        Change2 (cube, 30, 32);
        Change2 (cube, 30, 28);
        return cube;
    }

}
