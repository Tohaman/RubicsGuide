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

//    Sub MoveD(cube)
//'Меняем фронт
//    Change2 cube, 52, 36
//    Change2 cube, 53, 33
//    Change2 cube, 54, 30
//    Change2 cube, 52, 3
//    Change2 cube, 53, 2
//    Change2 cube, 54, 1
//    Change2 cube, 52, 10
//    Change2 cube, 53, 13
//    Change2 cube, 54, 16
//            'Меняем бок
//    Change2 cube, 44, 40
//    Change2 cube, 44, 38
//    Change2 cube, 44, 42
//    Change2 cube, 43, 37
//    Change2 cube, 43, 39
//    Change2 cube, 43, 45
//    End Sub
//
//    Sub MoveDb(cube)
//'Меняем фронт
//    Change2 cube, 52, 10
//    Change2 cube, 53, 13
//    Change2 cube, 54, 16
//    Change2 cube, 52, 3
//    Change2 cube, 53, 2
//    Change2 cube, 54, 1
//    Change2 cube, 52, 36
//    Change2 cube, 53, 33
//    Change2 cube, 54, 30
//            'Меняем бок
//    Change2 cube, 44, 42
//    Change2 cube, 44, 38
//    Change2 cube, 44, 40
//    Change2 cube, 43, 45
//    Change2 cube, 43, 39
//    Change2 cube, 43, 37
//    End Sub
//
//    Sub MoveD2(cube)    'D2
//    MoveD cube
//    MoveD cube
//    End Sub
//
//    Sub MoveE(cube)     'E
//            'Меняем фронт
//    Change2 cube, 49, 35
//    Change2 cube, 50, 32
//    Change2 cube, 51, 29
//    Change2 cube, 49, 6
//    Change2 cube, 50, 5
//    Change2 cube, 51, 4
//    Change2 cube, 49, 11
//    Change2 cube, 50, 14
//    Change2 cube, 51, 17
//    End Sub
//
//    Sub MoveEb(cube)    'E'
//            'Меняем фронт
//    Change2 cube, 49, 11
//    Change2 cube, 50, 14
//    Change2 cube, 51, 17
//    Change2 cube, 49, 6
//    Change2 cube, 50, 5
//    Change2 cube, 51, 4
//    Change2 cube, 49, 35
//    Change2 cube, 50, 32
//    Change2 cube, 51, 29
//    End Sub
//
//    Sub MoveE2(cube)    'E2
//    MoveE cube
//    MoveE cube
//    End Sub
//
//    Sub MoveM(cube)     'M
//            'Меняем фронт
//    Change2 cube, 20, 47
//    Change2 cube, 23, 50
//    Change2 cube, 26, 53
//    Change2 cube, 20, 44
//    Change2 cube, 23, 41
//    Change2 cube, 26, 38
//    Change2 cube, 20, 2
//    Change2 cube, 23, 5
//    Change2 cube, 26, 8
//    End Sub
//
//    Sub MoveMb(cube)    'M'
//            'Меняем фронт
//    Change2 cube, 20, 2
//    Change2 cube, 23, 5
//    Change2 cube, 26, 8
//    Change2 cube, 20, 44
//    Change2 cube, 23, 41
//    Change2 cube, 26, 38
//    Change2 cube, 20, 47
//    Change2 cube, 23, 50
//    Change2 cube, 26, 53
//    End Sub
//
//    Sub MoveM2(cube)    'M2
//    MoveM cube
//    MoveM cube
//    End Sub
//
//    Sub MoveS(cube)     'S
//            'Меняем фронт
//    Change2 cube, 22, 31
//    Change2 cube, 23, 32
//    Change2 cube, 24, 33
//    Change2 cube, 22, 40
//    Change2 cube, 23, 41
//    Change2 cube, 24, 42
//    Change2 cube, 22, 13
//    Change2 cube, 23, 14
//    Change2 cube, 24, 15
//    End Sub
//
//    Sub MoveSb(cube)    'S'
//            'Меняем фронт
//    Change2 cube, 22, 13
//    Change2 cube, 23, 14
//    Change2 cube, 24, 15
//    Change2 cube, 22, 40
//    Change2 cube, 23, 41
//    Change2 cube, 24, 42
//    Change2 cube, 22, 31
//    Change2 cube, 23, 32
//    Change2 cube, 24, 33
//    End Sub
//
//    Sub MoveS2(cube)   'S2
//    MoveS cube
//    MoveS cube
//    End Sub
//
//    Sub MoveUm(cube)   'u - верхние 2 слоя
//    MoveU cube
//    MoveEb cube
//    End Sub
//
//    Sub MoveUmb(cube)  'u'
//    MoveUb cube
//    MoveE cube
//    End Sub
//
//    Sub MoveUm2(cube)   'u2
//    MoveUm cube
//    MoveUm cube
//    End Sub
//
//    Sub MoveDm(cube)   'd - нижние 2 слоя
//    MoveD cube
//    MoveE cube
//    End Sub
//
//    Sub MoveDmb(cube)  'd'
//    MoveDb cube
//    MoveEb cube
//    End Sub
//
//    Sub MoveDm2(cube)   'd2
//    MoveDm cube
//    MoveDm cube
//    End Sub


}
