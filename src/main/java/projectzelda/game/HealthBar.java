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
            gs.fillRectScreen(x, y, width, height, color);
            gs.drawRectScreen(x, y, width, height, color);
            gs.fillRectScreen(x, y, healthWidth+1,height+1, healthColor);
            return;
        }

        gs.fillRect(x, y, width, height, color);
        gs.drawRect(x, y, width, height, outlineColor);
        gs.fillRect(x, y, healthWidth, height, healthColor);
    }
}
