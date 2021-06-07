package projectzelda.game;

import projectzelda.engine.UIObject;

import java.awt.*;

public class HealthBar extends UIObject {
    public int width;
    public int height;
    public Color outlineColor;
    public Color healthColor;

    public double health = 1.0;
    public boolean isHudElement = false;

    public HealthBar(int x_, int y_, int width_, int height_) {
        super(x_, y_);
        width = width_;
        height = height_;
        color = Color.LIGHT_GRAY;
        outlineColor = Color.BLACK;
        healthColor = Color.RED;
    }
}
