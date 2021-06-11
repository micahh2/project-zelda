package projectzelda.game;

import projectzelda.engine.UIObject;
import projectzelda.engine.GraphicSystem;

import java.awt.*;

public class ItemSlot extends UIObject {
    public int radius;

    public ItemSlot(int x_, int y_, int radius_) {
        super(x_, y_);
        radius = radius_;
        color = Color.LIGHT_GRAY;
        outlineColor = Color.BLACK;
    }

    @Override
    public void draw(GraphicSystem gs) {
        int xCorner = (int) (x - radius);
        int yCorner = (int) (y - radius);
        int d = (radius * 2);

        gs.fillOvalScreen(xCorner, yCorner, d, d, color);
        gs.drawOvalScreen(xCorner, yCorner, d, d, Color.DARK_GRAY);
    }
}
