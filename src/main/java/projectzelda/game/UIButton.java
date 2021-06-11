package projectzelda.game;

import projectzelda.engine.UIObject;
import projectzelda.engine.GraphicSystem;
import java.awt.*;

public class UIButton extends UIObject {
    public int width;
    public int height;
    public String text;
    public Font textFont;
    public Color textColor;

    public UIButton(int x_, int y_, int width_, int height_, String text_) {
        super(x_, y_);
        width = width_;
        height = height_;
        text = text_;
        textFont = new Font("BUTTON FONT", Font.PLAIN, 23);
        color = Color.DARK_GRAY;
        outlineColor = Color.BLACK;
        textColor = Color.WHITE;
    }

    public int getMaxX(){
        return x + width;
    }

    public int getMaxY(){
        return y + height;
    }

    @Override
    public void draw(GraphicSystem gs) {
        gs.fillRectScreen(x, y, width, height, color);
        gs.drawRectScreen(x, y, width, height, outlineColor);

        gs.drawCenteredTextScreen(x, y, width, height, textColor, textFont, text);
    }
}
