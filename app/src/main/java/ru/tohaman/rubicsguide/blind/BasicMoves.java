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

    public static int[] MoveR2(int [] cube) {
        MoveR (cube);
        MoveR (cube);
        return cube;
    }

    public static int[] MoveF (int[] cube) {
        //Меняем фронт
        Change2 (cube, 26, 35);
        Change2 (cube, 25, 34);
        Change2 (cube, 24, 33);
        Change2 (cube, 26, 44);
        Change2 (cube, 25, 43);
        Change2 (cube, 24, 42);
        Change2 (cube, 26, 17);
        Change2 (cube, 25, 16);
        Change2 (cube, 24, 15);
        //Меняем бок
        Change2 (cube, 46, 50);
        Change2 (cube, 46, 52);
        Change2 (cube, 46, 48);
        Change2 (cube, 45, 47);
        Change2 (cube, 45, 53);
        Change2 (cube, 45, 51);
        return cube;
    }

    public static int[] MoveFb(int [] cube) {
        //Меняем фронт
        Change2 (cube, 26, 17);
        Change2 (cube, 25, 16);
        Change2 (cube, 24, 15);
        Change2 (cube, 26, 44);
        Change2 (cube, 25, 43);
        Change2 (cube, 24, 42);
        Change2 (cube, 26, 35);
        Change2 (cube, 25, 34);
        Change2 (cube, 24, 33);
        //Меняем бок
        Change2 (cube, 46, 48);
        Change2 (cube, 46, 52);
        Change2 (cube, 46, 50);
        Change2 (cube, 45, 51);
        Change2 (cube, 45, 53);
        Change2 (cube, 45, 47);
        return cube;
    }

    public static int[] MoveF2(int [] cube) {
        MoveF (cube);
        MoveF (cube);
        return cube;
    }

    public static int[] MoveU (int[] cube) {
        //Меняем фронт
        Change2 (cube, 45, 11);
        Change2 (cube, 46, 14);
        Change2 (cube, 47, 17);
        Change2 (cube, 45, 8);
        Change2 (cube, 46, 7);
        Change2 (cube, 47, 6);
        Change2 (cube, 45, 33);
        Change2 (cube, 46, 30);
        Change2 (cube, 47, 27);
        //Меняем бок
        Change2 (cube, 26, 24);
        Change2 (cube, 26, 18);
        Change2 (cube, 26, 20);
        Change2 (cube, 25, 21);
        Change2 (cube, 25, 19);
        Change2 (cube, 25, 23);
        return cube;
    }

    public static int[] MoveUb(int [] cube) {
        //Меняем фронт
        Change2 (cube, 45, 33);
        Change2 (cube, 46, 30);
        Change2 (cube, 47, 27);
        Change2 (cube, 45, 8);
        Change2 (cube, 46, 7);
        Change2 (cube, 47, 6);
        Change2 (cube, 45, 11);
        Change2 (cube, 46, 14);
        Change2 (cube, 47, 17);
        //Меняем бок
        Change2 (cube, 26, 20);
        Change2 (cube, 26, 18);
        Change2 (cube, 26, 24);
        Change2 (cube, 25, 23);
        Change2 (cube, 25, 19);
        Change2 (cube, 25, 21);
        return cube;
    }

    public static int[] MoveU2(int [] cube) {
        MoveU (cube);
        MoveU (cube);
        return cube;
    }

    public static int[] MoveL (int[] cube) {
        //Меняем фронт
        Change2 (cube, 18, 45);
        Change2 (cube, 21, 48);
        Change2 (cube, 24, 51);
        Change2 (cube, 18, 44);
        Change2 (cube, 21, 41);
        Change2 (cube, 24, 38);
        Change2 (cube, 18, 0);
        Change2 (cube, 21, 3);
        Change2 (cube, 24, 6);
        //Меняем бок
        Change2 (cube, 11, 17);
        Change2 (cube, 11, 15);
        Change2 (cube, 11, 9);
        Change2 (cube, 14, 16);
        Change2 (cube, 14, 12);
        Change2 (cube, 14, 10);
        return cube;
    }

    public static int[] MoveLb(int [] cube) {
        //Меняем фронт
        Change2 (cube, 18, 0);
        Change2 (cube, 21, 3);
        Change2 (cube, 24, 6);
        Change2 (cube, 18, 44);
        Change2 (cube, 21, 41);
        Change2 (cube, 24, 38);
        Change2 (cube, 18, 45);
        Change2 (cube, 21, 48);
        Change2 (cube, 24, 51);
        //Меняем бок
        Change2 (cube, 11, 9);
        Change2 (cube, 11, 15);
        Change2 (cube, 11, 17);
        Change2 (cube, 14, 10);
        Change2 (cube, 14, 12);
        Change2 (cube, 14, 16);
        return cube;
    }

    public static int[] MoveL2(int [] cube) {
        MoveL (cube);
        MoveL (cube);
        return cube;
    }

    public static int[] MoveB (int[] cube) {
        //Меняем фронт
        Change2 (cube, 18, 9);
        Change2 (cube, 19, 10);
        Change2 (cube, 20, 11);
        Change2 (cube, 18, 36);
        Change2 (cube, 19, 37);
        Change2 (cube, 20, 38);
        Change2 (cube, 18, 27);
        Change2 (cube, 19, 28);
        Change2 (cube, 20, 29);
        //Меняем бок
        Change2 (cube, 7, 3);
        Change2 (cube, 7, 1);
        Change2 (cube, 7, 5);
        Change2 (cube, 8, 6);
        Change2 (cube, 8, 0);
        Change2 (cube, 8, 2);
        return cube;
    }

    public static int[] MoveBb(int [] cube) {
        //Меняем фронт
        Change2 (cube, 18, 27);
        Change2 (cube, 19, 28);
        Change2 (cube, 20, 29);
        Change2 (cube, 18, 36);
        Change2 (cube, 19, 37);
        Change2 (cube, 20, 38);
        Change2 (cube, 18, 9);
        Change2 (cube, 19, 10);
        Change2 (cube, 20, 11);
        //Меняем бок
        Change2 (cube, 7, 5);
        Change2 (cube, 7, 1);
        Change2 (cube, 7, 3);
        Change2 (cube, 8, 2);
        Change2 (cube, 8, 0);
        Change2 (cube, 8, 6);
        return cube;
    }

    public static int[] MoveB2(int [] cube) {
        MoveB (cube);
        MoveB (cube);
        return cube;
    }

    public static int[] MoveD(int [] cube) {
    //'Меняем фронт
    Change2 (cube, 51, 35);
    Change2 (cube, 52, 32);
    Change2 (cube, 53, 29);
    Change2 (cube, 51, 2);
    Change2 (cube, 52, 1);
    Change2 (cube, 53, 0);
    Change2 (cube, 51, 9);
    Change2 (cube, 52, 12);
    Change2 (cube, 53, 15);
    //'Меняем бок
    Change2 (cube, 43, 39);
    Change2 (cube, 43, 37);
    Change2 (cube, 43, 41);
    Change2 (cube, 42, 36);
    Change2 (cube, 42, 38);
    Change2 (cube, 42, 44);
    return cube;
    }

    public static int[] MoveDb(int [] cube) {
        //'Меняем фронт
        Change2 (cube, 51, 9);
        Change2 (cube, 52, 12);
        Change2 (cube, 53, 15);
        Change2 (cube, 51, 2);
        Change2 (cube, 52, 1);
        Change2 (cube, 53, 0);
        Change2 (cube, 51, 35);
        Change2 (cube, 52, 32);
        Change2 (cube, 53, 29);
        //'Меняем бок
        Change2 (cube, 43, 41);
        Change2 (cube, 43, 37);
        Change2 (cube, 43, 39);
        Change2 (cube, 42, 44);
        Change2 (cube, 42, 38);
        Change2 (cube, 42, 36);
        return cube;
    }

    public static int[] MoveD2(int [] cube) {
        MoveD (cube);
        MoveD (cube);
        return cube;
    }

    public static int[] MoveE(int [] cube) {
        //'Меняем фронт
        Change2 (cube, 48, 34);
        Change2 (cube, 49, 31);
        Change2 (cube, 50, 28);
        Change2 (cube, 48, 5);
        Change2 (cube, 49, 4);
        Change2 (cube, 50, 3);
        Change2 (cube, 48, 10);
        Change2 (cube, 49, 13);
        Change2 (cube, 50, 16);
        return cube;
    }

    public static int[] MoveEb(int [] cube) {
        //'Меняем фронт
        Change2 (cube, 48, 10);
        Change2 (cube, 49, 13);
        Change2 (cube, 50, 16);
        Change2 (cube, 48, 5);
        Change2 (cube, 49, 4);
        Change2 (cube, 50, 3);
        Change2 (cube, 48, 34);
        Change2 (cube, 49, 31);
        Change2 (cube, 50, 28);
        return cube;
    }

    public static int[] MoveE2(int [] cube) {
        MoveE (cube);
        MoveE (cube);
        return cube;
    }

    public static int[] MoveM(int [] cube) {
        //'Меняем фронт
        Change2 (cube, 19, 46);
        Change2 (cube, 22, 49);
        Change2 (cube, 25, 52);
        Change2 (cube, 19, 43);
        Change2 (cube, 22, 40);
        Change2 (cube, 25, 37);
        Change2 (cube, 19, 1);
        Change2 (cube, 22, 4);
        Change2 (cube, 25, 7);
        return cube;
    }

    public static int[] MoveMb(int [] cube) {
        //'Меняем фронт
        Change2 (cube, 19, 1);
        Change2 (cube, 22, 4);
        Change2 (cube, 25, 7);
        Change2 (cube, 19, 43);
        Change2 (cube, 22, 40);
        Change2 (cube, 25, 37);
        Change2 (cube, 19, 46);
        Change2 (cube, 22, 49);
        Change2 (cube, 25, 52);
        return cube;
    }

    public static int[] MoveM2(int [] cube) {
        MoveM (cube);
        MoveM (cube);
        return cube;
    }

    public static int[] MoveS(int [] cube) {
        //'Меняем фронт
        Change2 (cube, 21, 30);
        Change2 (cube, 22, 31);
        Change2 (cube, 23, 32);
        Change2 (cube, 21, 39);
        Change2 (cube, 22, 40);
        Change2 (cube, 23, 41);
        Change2 (cube, 21, 12);
        Change2 (cube, 22, 13);
        Change2 (cube, 23, 14);
        return cube;
    }

    public static int[] MoveSb(int [] cube) {
        //'Меняем фронт
        Change2 (cube, 21, 12);
        Change2 (cube, 22, 13);
        Change2 (cube, 23, 14);
        Change2 (cube, 21, 39);
        Change2 (cube, 22, 40);
        Change2 (cube, 23, 41);
        Change2 (cube, 21, 30);
        Change2 (cube, 22, 31);
        Change2 (cube, 23, 32);
        return cube;
    }

    public static int[] MoveS2(int [] cube) {
        MoveS (cube);
        MoveS (cube);
        return cube;
    }

    public static int[] MoveRw(int [] cube) {   //Uw - верхние два слоя
        MoveR (cube);
        MoveMb (cube);
        return cube;
    }

    public static int[] MoveRwb(int [] cube) {   //Uw' - верхние два слоя
        MoveRb (cube);
        MoveM (cube);
        return cube;
    }

    public static int[] MoveRw2(int [] cube) {   //Uw2 - верхние два слоя
        MoveRw (cube);
        MoveRw (cube);
        return cube;
    }

    public static int[] MoveUw(int [] cube) {   //Uw - верхние два слоя
        MoveU (cube);
        MoveEb (cube);
        return cube;
    }

    public static int[] MoveUwb(int [] cube) {   //Uw' - верхние два слоя
        MoveUb (cube);
        MoveE (cube);
        return cube;
    }

    public static int[] MoveUw2(int [] cube) {   //Uw2 - верхние два слоя
        MoveUw (cube);
        MoveUw (cube);
        return cube;
    }

    public static int[] MoveDw(int [] cube) {   //Dw - нижние два слоя
        MoveD (cube);
        MoveE (cube);
        return cube;
    }

    public static int[] MoveDwb(int [] cube) {
        MoveDb (cube);
        MoveEb (cube);
        return cube;
    }

    public static int[] MoveDw2(int [] cube) {
        MoveDw (cube);
        MoveDw (cube);
        return cube;
    }

    public static int[] MoveLw(int [] cube) {   //Uw - верхние два слоя
        MoveL (cube);
        MoveM (cube);
        return cube;
    }

    public static int[] MoveLwb(int [] cube) {   //Uw' - верхние два слоя
        MoveLb (cube);
        MoveMb (cube);
        return cube;
    }

    public static int[] MoveLw2(int [] cube) {   //Uw2 - верхние два слоя
        MoveLw (cube);
        MoveLw (cube);
        return cube;
    }

}
