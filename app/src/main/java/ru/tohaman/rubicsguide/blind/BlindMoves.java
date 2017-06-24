package ru.tohaman.rubicsguide.blind;

import android.widget.Toast;

/**
 * Created by anton on 23.06.17.
 */

public class BlindMoves {

    public BlindMoves(){

    }

    public static int[] Scram(int[] cube, String scrm) {
        scrm = scrm.replace("'","1");
        scrm = scrm.replace("r","Rw");
        scrm = scrm.replace("l","Lw");
        scrm = scrm.replace("u","Uw");
        scrm = scrm.replace("d","Dw");
        scrm = scrm.replace("f","Fw");
        scrm = scrm.replace("b","Bw");
        String hod = "";
        int j = 0;
        String [] ArScrm = scrm.split(" ");
        for (int i = 0; i<ArScrm.length; i++) {
            hod = ArScrm[i];
            switch (hod) {
                case "R":
                    BasicMoves.MoveR (cube);
                    break;
                case "R1":
                    BasicMoves.MoveRb (cube);
                    break;
                case "R2":
                    BasicMoves.MoveR2 (cube);
                    break;
                case "F":
                    BasicMoves.MoveF (cube);
                    break;
                case "F1":
                    BasicMoves.MoveFb (cube);
                    break;
                case "F2":
                    BasicMoves.MoveF2 (cube);
                    break;
                case "U":
                    BasicMoves.MoveU (cube);
                    break;
                case "U1":
                    BasicMoves.MoveUb (cube);
                    break;
                case "U2":
                    BasicMoves.MoveU2 (cube);
                    break;
                case "L":
                    BasicMoves.MoveL (cube);
                    break;
                case "L1":
                    BasicMoves.MoveLb (cube);
                    break;
                case "L2":
                    BasicMoves.MoveL2 (cube);
                    break;
                case "B":
                    BasicMoves.MoveB (cube);
                    break;
                case "B1":
                    BasicMoves.MoveBb (cube);
                    break;
                case "B2":
                    BasicMoves.MoveB2 (cube);
                    break;
                case "D":
                    BasicMoves.MoveD (cube);
                    break;
                case "D1":
                    BasicMoves.MoveDb (cube);
                    break;
                case "D2":
                    BasicMoves.MoveD2 (cube);
                    break;
                case "E":
                    BasicMoves.MoveE (cube);
                    break;
                case "E1":
                    BasicMoves.MoveEb (cube);
                    break;
                case "E2":
                    BasicMoves.MoveE2 (cube);
                    break;
                case "M":
                    BasicMoves.MoveM (cube);
                    break;
                case "M1":
                    BasicMoves.MoveMb (cube);
                    break;
                case "M2":
                    BasicMoves.MoveM2 (cube);
                    break;
                case "S":
                    BasicMoves.MoveS (cube);
                    break;
                case "S1":
                    BasicMoves.MoveSb (cube);
                    break;
                case "S2":
                    BasicMoves.MoveS2 (cube);
                    break;
                case "Rw":
                    BasicMoves.MoveRw (cube);
                    break;
                case "Rw1":
                    BasicMoves.MoveRwb (cube);
                    break;
                case "Rw2":
                    BasicMoves.MoveRw2 (cube);
                    break;
                case "Uw":
                    BasicMoves.MoveUw (cube);
                    break;
                case "Uw1":
                    BasicMoves.MoveUwb (cube);
                    break;
                case "Uw2":
                    BasicMoves.MoveUw2 (cube);
                    break;
                case "Dw":
                    BasicMoves.MoveDw (cube);
                    break;
                case "Dw1":
                    BasicMoves.MoveDwb (cube);
                    break;
                case "Dw2":
                    BasicMoves.MoveDw2 (cube);
                    break;
                case "Lw":
                    BasicMoves.MoveLw (cube);
                    break;
                case "Lw1":
                    BasicMoves.MoveLwb (cube);
                    break;
                case "Lw2":
                    BasicMoves.MoveLw2 (cube);
                    break;
                case "Fw":
                    BasicMoves.MoveFw (cube);
                    break;
                case "Fw1":
                    BasicMoves.MoveFwb (cube);
                    break;
                case "Fw2":
                    BasicMoves.MoveFw2 (cube);
                    break;
                case "Bw":
                    BasicMoves.MoveBw (cube);
                    break;
                case "Bw1":
                    BasicMoves.MoveBwb (cube);
                    break;
                case "Bw2":
                    BasicMoves.MoveBw2 (cube);
                    break;
            }
        }
        return cube;
    }

    public static int[] Zapad(int[] cube) {  //Алгоритм Запад
        Scram (cube,"R U R' U' R' F R2 U' R' U' R U R' F'");
        return cube;
    }

    public static int[] Yug(int[] cube) {  //Алгоритм Юг
        Scram (cube,"R U R' F' R U R' U' R' F R2 U' R' U'");
        return cube;
    }

    public static int[] PifPaf(int[] cube) {  //Алгоритм Пиф-паф
        Scram (cube,"R U R' U'");
        return cube;
    }

    public static int[] Ekvator(int[] cube) {  //Алгоритм Экватор
        Scram (cube,"R U R' F' R U2 R' U2 R' F R U R U2 R' U'");
        return cube;
    }

    public static int[] Australia(int[] cube) {  //Алгоритм Австралия
        Scram (cube,"F R U' R' U' R U R' F' R U R' U' R' F R F'");
        return cube;
    }

    public static int[] Blinde19(int[] cube) {  //белосинее ребро
        Scram (cube,"M2 D' L2");
        Zapad (cube);
        Scram (cube,"L2 D M2");
        return cube;
    }

    public static int[] Blinde25(int[] cube) {  //белозеленое
        Yug (cube);
        return cube;
    }

    public static int[] Blinde21(int[] cube) {  //белооранжевое
        Zapad (cube);
        return cube;
    }

    public static int[] Blinde46(int[] cube) {  //зеленобелое
        Scram (cube,"M D' L2");
        Zapad (cube);
        Scram (cube,"L2 D M'");
        return cube;
    }

    public static int[] Blinde50(int[] cube) {  //зеленокрасное
        Scram (cube,"Dw2 L");
        Zapad (cube);
        Scram (cube,"L' Dw2");
        return cube;
    }

    public static int[] Blinde52(int[] cube) {  //зеленожелтое
        Scram (cube,"M'");
        Yug (cube);
        Scram (cube,"M");
        return cube;
    }

    public static int[] Blinde48(int[] cube) {  //зеленооранжевое
        Scram (cube,"L'");
        Zapad (cube);
        Scram (cube,"L");
        return cube;
    }

    public static int[] Blinde7(int[] cube) {  //синебелое
        Scram (cube,"M");
        Yug (cube);
        Scram (cube,"M'");
        return cube;
    }

    public static int[] Blinde5(int[] cube) {  //синекрасное
        Scram (cube,"Dw2 L'");
        Zapad (cube);
        Scram (cube,"L Dw2");
        return cube;
    }

    public static int[] Blinde1(int[] cube) {  //синежелтое
        Scram (cube,"Dw2 L'");
        Zapad (cube);
        Scram (cube,"L Dw2");
        return cube;
    }

    public static int[] Blinde3(int[] cube) {  //синеоранжевое
        Scram (cube,"L");
        Zapad (cube);
        Scram (cube,"L'");
        return cube;
    }

    public static int[] Blinde14(int[] cube) {  //оранжевобелое
        Scram (cube,"L2 D M'");
        Yug (cube);
        Scram (cube,"M D' L2");
        return cube;
    }

    public static int[] Blinde16(int[] cube) {  //оранжевозеленое
        Scram (cube,"Dw' L");
        Zapad (cube);
        Scram (cube,"L' Dw");
        return cube;
    }

    public static int[] Blinde12(int[] cube) {  //оранжевожелтое
        Scram (cube,"D M'");
        Yug (cube);
        Scram (cube,"M D'");
        return cube;
    }

    public static int[] Blinde10(int[] cube) {  //оранжевосинее
        Scram (cube,"Dw L'");
        Zapad (cube);
        Scram (cube,"L Dw'");
        return cube;
    }

    public static int[] Blinde34(int[] cube) {  //краснозеленое
        Scram (cube,"Dw' L'");
        Zapad (cube);
        Scram (cube,"L Dw");
        return cube;
    }

    public static int[] Blinde32(int[] cube) {  //красножелтое
        Scram (cube,"D' M'");
        Yug (cube);
        Scram (cube,"M D");
        return cube;
    }

    public static int[] Blinde28(int[] cube) {  //красносинее
        Scram (cube,"Dw L");
        Zapad (cube);
        Scram (cube,"L' Dw'");
        return cube;
    }

    public static int[] Blinde37(int[] cube) {  //желтосинее
        Scram (cube,"D L2");
        Zapad (cube);
        Scram (cube,"L2 D'");
        return cube;
    }

    public static int[] Blinde39(int[] cube) {  //желтокрасное
        Scram (cube,"D2 L2");
        Zapad (cube);
        Scram (cube,"L2 D2");
        return cube;
    }

    public static int[] Blinde43(int[] cube) {  //желтозеленое
        Scram (cube,"D' L2");
        Zapad (cube);
        Scram (cube,"L2 D'");
        return cube;
    }

    public static int[] Blinde41(int[] cube) {  //желтооранжевое
        Scram (cube,"L2");
        Zapad (cube);
        Scram (cube,"D2");
        return cube;
    }

//--------------------------------------------------------------------------------------------------

    public static int[] Blinde20(int[] cube) {  //белосинекрасный угол
        Scram (cube,"R D' F'");
        Australia (cube);
        Scram (cube,"F D R'");
        return cube;
    }

    public static int[] Blinde26(int[] cube) {  //белокраснозеленый угол
        Australia (cube);
        return cube;
    }

    public static int[] Blinde24(int[] cube) {  //белозеленооранжевый угол
        Scram (cube,"F' D R");
        Australia (cube);
        Scram (cube,"R' D' F");
        return cube;
    }

    public static int[] Blinde45(int[] cube) {  //зеленооранжевобелый
        Scram (cube,"F' D F'");
        Australia (cube);
        Scram (cube,"F D' F");
        return cube;
    }

    public static int[] Blinde47(int[] cube) {  //зеленобелосиний
        Scram (cube,"F R");
        Australia (cube);
        Scram (cube,"R' F'");
        return cube;
    }

    public static int[] Blinde53(int[] cube) {  //зеленокрасножелтый
        Scram (cube,"R");
        Australia (cube);
        Scram (cube,"R'");
        return cube;
    }

    public static int[] Blinde51(int[] cube) {  //зеленожелтооранжевый
        Scram (cube,"D F'");
        Australia (cube);
        Scram (cube,"F D'");
        return cube;
    }

    public static int[] Blinde8(int[] cube) {  //синекраснобелый
        Scram (cube,"R'");
        Australia (cube);
        Scram (cube,"R");
        return cube;
    }

    public static int[] Blinde2(int[] cube) {  //синежелтокрасный
        Scram (cube,"D' F'");
        Australia (cube);
        Scram (cube,"F D");
        return cube;
    }

    public static int[] Blinde0(int[] cube) {  //синеоранжевожелтый
        Scram (cube,"D2 R");
        Australia (cube);
        Scram (cube,"R' D2");
        return cube;
    }

    public static int[] Blinde17(int[] cube) {  //оранжевобелозеленый
        Scram (cube,"F");
        Australia (cube);
        Scram (cube,"F'");
        return cube;
    }

    public static int[] Blinde15(int[] cube) {  //оранжевозеленожелтый
        Scram (cube,"D R");
        Australia (cube);
        Scram (cube,"R' D'");
        return cube;
    }

    public static int[] Blinde9(int[] cube) {  //оранжевожелтосиний
        Scram (cube,"D2 F'");
        Australia (cube);
        Scram (cube,"F D2");
        return cube;
    }

    public static int[] Blinde27(int[] cube) {  //краснобелосиний
        Scram (cube,"R2 F'");
        Australia (cube);
        Scram (cube,"F R2");
        return cube;
    }

    public static int[] Blinde33(int[] cube) {  //краснозеленобелый
        Scram (cube,"R' F'");
        Australia (cube);
        Scram (cube,"F R");
        return cube;
    }

    public static int[] Blinde35(int[] cube) {  //красножелтозеленый
        Scram (cube,"F'");
        Australia (cube);
        Scram (cube,"F");
        return cube;
    }

    public static int[] Blinde29(int[] cube) {  //красносинежелтый
        Scram (cube,"R F'");
        Australia (cube);
        Scram (cube,"F R'");
        return cube;
    }

    public static int[] Blinde38(int[] cube) {  //желтосинеоранжевый
        Scram (cube,"D' R2");
        Australia (cube);
        Scram (cube,"R2 D");
        return cube;
    }

    public static int[] Blinde36(int[] cube) {  //желтокрасносиний
        Scram (cube,"R2");
        Australia (cube);
        Scram (cube,"R2");
        return cube;
    }

    public static int[] Blinde42(int[] cube) {  //желтозеленокрасный
        Scram (cube,"D R2");
        Australia (cube);
        Scram (cube,"R2 D'");
        return cube;
    }

    public static int[] Blinde44(int[] cube) {  //желтооранжевозеленый
        Scram (cube,"D2 R2");
        Australia (cube);
        Scram (cube,"R2 D2");
        return cube;
    }

}
