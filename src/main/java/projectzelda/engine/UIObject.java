package projectzelda.engine;

import java.awt.*;

public abstract class UIObject {
    public static World world;

    public int x;
    public int y;
    public Color color;

    public UIObject(int x_, int y_) {
        x = x_;
        y = y_;
        color = Color.DARK_GRAY;
    }

    public static void setWorld(World w) {
        world = w;
    }
}
