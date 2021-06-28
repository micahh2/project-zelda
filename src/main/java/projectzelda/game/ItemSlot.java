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
    int width;
    int height;
    String keyCode = "1";
    WeaponState activeState;

    public ItemSlot(int x_, int y_, Avatar avatar_, String itemType_, WeaponState activeState_, String keyCode_, ImageRef image_) {
        super(x_, y_);
        color = Color.WHITE;
        avatar = avatar_;
        itemType = itemType_;
        image = image_;
        font = new Font("TEXT FONT", Font.ROMAN_BASELINE, 13);
        width = 20;
        height = 30;
        activeState = activeState_;
        keyCode = keyCode_;
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        if (avatar.containsItem(itemType)) {

            Color border = Color.GRAY;
            if(((RPGWorld)world).weaponState == activeState) {
                border = Color.BLACK;
            }
            gs.fillOvalScreen(x-4, y-4, width+8, height+8, Color.WHITE);
            gs.drawOvalScreen(x-4, y-4, width+8, height+8, 2, border);
            gs.drawImageScreen(image, x, y, x + width, y + height);
            gs.drawCenteredTextScreen(x+1, y+height+3, width, height, Color.BLACK, font, keyCode);
            gs.drawCenteredTextScreen(x, y+height+2, width, height, color, font, keyCode);
        }
    }
}
