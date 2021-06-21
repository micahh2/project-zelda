package projectzelda.game;

import projectzelda.engine.GraphicSystem;
import projectzelda.engine.RectangularGameObject;

import java.awt.*;

public class Rock extends RectangularGameObject
{
    public Rock(double x, double y)
        {
            super(x, y, 0, 0, 128, 46, null);
            this.isMoving = false;
        }

    // Invisible
    @Override
    public void draw(GraphicSystem gs, long tick) { return; }

    public int type() { return Const.Type.TREE.ordinal(); }

}
