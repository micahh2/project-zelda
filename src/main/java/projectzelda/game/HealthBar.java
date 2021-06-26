package projectzelda.game;

import projectzelda.engine.UIObject;
import projectzelda.engine.GraphicSystem;

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
        color = Color.RED;
        outlineColor = Color.BLACK;
        healthColor = Color.GREEN;
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        int healthWidth = (int) (health * width);
        if (isHudElement) {
            gs.fillRoundRectScreen(x, y, width, height, 5, 5, color);
            gs.fillRoundRectScreen(x, y, healthWidth+1, height, 5, 5, healthColor);
            gs.drawRoundRectScreen(x, y, width, height, 5, 5, 2, outlineColor);
            return;
        }

        gs.fillRoundRect(x, y, width, height, 2, 2, color);
        gs.fillRoundRect(x, y, healthWidth, height, 2, 2, healthColor);
        gs.drawRoundRect(x, y, width, height, 2, 2, 1, outlineColor);
    }
}
