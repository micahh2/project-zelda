package projectzelda.game;

import projectzelda.engine.ImageRef;
import projectzelda.engine.UIObject;
import projectzelda.engine.GraphicSystem;

import java.awt.*;

public class ItemSlot extends UIObject {
    public int radius;
    public Avatar avatar;
    public String itemType;
    public ImageRef image;

    public ItemSlot(int x_, int y_, int radius_, Avatar avatar_, String itemType_, ImageRef image_) {
        super(x_, y_);
        radius = radius_;
        color = Color.LIGHT_GRAY;
        outlineColor = Color.BLACK;
        avatar = avatar_;
        itemType = itemType_;
        image = image_;
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        int xCorner = x - radius;
        int yCorner = y - radius;
        int d = (radius * 2);

        gs.fillOvalScreen(xCorner, yCorner, d, d, color);
        gs.drawOvalScreen(xCorner, yCorner, d, d, Color.DARK_GRAY);

        if (avatar.containsItem(itemType)) {
            int swordx = x - radius / 2;
            int swordy = y - radius;
            int width = (int) Math.round((image.x2 - image.x1) * 1.8);
            int height = (int) Math.round((image.y2 - image.y1) * 1.8);

            gs.drawHudImage(image, swordx, swordy, swordx + width, swordy + height);

        }
    }
}
