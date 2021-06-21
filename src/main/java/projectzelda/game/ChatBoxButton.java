package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.GameObject;
import projectzelda.engine.UIObject;
import projectzelda.engine.GraphicSystem;
import java.awt.*;

public class ChatBoxButton extends UIObject {
    public int width;
    public int height;
    public String helpText;
    public String text;
    public Font textFont;
    public Font helpFont;
    public Color textColor;
    public GameObject obj;
    public int objID;

    public ChatBoxButton(int x_, int y_, int width_, int height_, String text_, GameObject obj_) {
        super(x_, y_);
        width = width_;
        height = height_;
        text = text_;
        obj = obj_;
        helpText = "(spacebar to continue)";
        textFont = new Font("BUTTON FONT", Font.ROMAN_BASELINE, 23);
        helpFont = new Font("BUTTON FONT", Font.ROMAN_BASELINE, 14);
        color = Color.WHITE;
        outlineColor = Color.BLACK;
        textColor = Color.BLACK;


    }

    // perhaps superfluous
    public ChatBoxButton(int x_, int y_, int width_, int height_, String text_, int objID_) {
        super(x_, y_);
        width = width_;
        height = height_;
        text = text_;
        objID = objID_;
        helpText = "(spacebar to continue)";
        textFont = new Font("BUTTON FONT", Font.ROMAN_BASELINE, 23);
        helpFont = new Font("BUTTON FONT", Font.ROMAN_BASELINE, 14);
        color = Color.WHITE;
        outlineColor = Color.BLACK;
        textColor = Color.BLACK;


    }



    public void setText(String text) {
        this.text = text;
    }

    public int getMaxX(){
        return x + width;
    }

    public int getMaxY(){
        return y + height;
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        gs.fillRectScreen(x, y, width, height, color);
        gs.drawRectScreen(x, y, width, height, outlineColor);

        gs.drawCenteredTextScreenWithSub(x, y, width, height, textColor, textFont, helpFont, text, helpText);
    }
}
