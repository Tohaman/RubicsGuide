package ru.tohaman.rubicsguide.blind;

/**
 * Created by anton on 22.06.17.
 */

public class BlindMain {

    public static int[] Initialize (int[] cube) {
        for (int i = 0 ; i < cube.length; i++) {
            cube[i] = (i / 9);
        }
        return cube;
    }


}
