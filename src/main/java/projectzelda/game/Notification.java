package projectzelda.game;

import projectzelda.engine.GameObject;
import projectzelda.engine.UIObject;
import projectzelda.engine.GraphicSystem;
import java.awt.*;

public class Notification extends ChatBoxButton {

    public double life = 1;

    public Notification(int x_, int y_, int width_, int height_, String text_) {
        super(x_, y_, width_,  height_, text_, (GameObject)null);
        isBlocking = false;
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        gs.drawCenteredTextScreen(x+1, y+1, width, height, outlineColor, textFont, text);
        gs.drawCenteredTextScreen(x, y, width, height, color, textFont, text);
    }
}
