package projectzelda.game;

import projectzelda.engine.UIObject;

import java.awt.*;

public class ItemSlot extends UIObject {
    public int radius;

    public ItemSlot(int x_, int y_, int radius_) {
        super(x_, y_);
        radius = radius_;
        color = Color.LIGHT_GRAY;
        outlineColor = Color.BLACK;
    }
}
