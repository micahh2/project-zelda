package projectzelda.game;

import projectzelda.engine.ImageRef;
import projectzelda.engine.TextObject;
import projectzelda.engine.UIObject;
import projectzelda.engine.GraphicSystem;

import java.awt.*;

public class ItemSlot extends UIObject {
    public Avatar avatar;
    public String itemType;
    public ImageRef image;
    public Font font;

    public ItemSlot(int x_, int y_, Avatar avatar_, String itemType_, ImageRef image_) {
        super(x_, y_);
        color = Color.BLACK;
        avatar = avatar_;
        itemType = itemType_;
        image = image_;
        font = new Font("TEXT FONT", Font.ROMAN_BASELINE, 23);

    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        if (avatar.containsItem(itemType)) {
            int width = (int) Math.round((image.x2 - image.x1) * 1.2);
            int height = (int) Math.round((image.y2 - image.y1) * 1.2);

            gs.drawImageScreen(image, x, y, x + width, y + height);
        }
    }
}
