package projectzelda.game;

import projectzelda.Const;
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
    public void draw(GraphicSystem gs) { return; }

    public int type() { return Const.TYPE_TREE; }

}
