package ru.skillbox;

public class Dimensions {

    private final int width;
    private final int height;
    private final int length;

    public Dimensions(int width, int height, int length) {
        this.width = width;
        this.height = height;
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public int volumeCalculation() {
        return width * height * length;
    }

    @Override
    public String toString() {
        return "width=" + width +
                ", height=" + height +
                ", length=" + length;
    }
}
