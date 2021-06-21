package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;

public class Lava extends RectangularGameObject {

    public Lava(double x, double y, int width, int height)
    {
        super(x, y, 0, 0, width, height, null);
        this.isMoving = false;
    }

    // Invisible
    @Override
    public void draw(GraphicSystem gs) { return; }

    public int type() { return Const.TYPE_LAVA; }


}
