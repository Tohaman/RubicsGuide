package ru.tohaman.rubicsguide.blind;

/**
 * Created by Toha on 27.06.2017.
 */

public class CubeAzbuka {           //свой класс для создания массива, в котором одному элементу соответствует буква и какой-то цвет
    private int color;
    private String letter;

    public CubeAzbuka (int color, String letter) {
        this.color = color;
        this.letter = letter;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
