package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;

public class HitBox extends RectangularGameObject {
    Const.Type type;

    public HitBox(double x, double y, int width, int height, Const.Type type)
    {
        super(x, y, 0, 0, width, height, null);
        this.isMoving = false;
        this.type = type;
    }

    // Invisible
    @Override
    public void draw(GraphicSystem gs, long tick) { return; }

    @Override
    public GameObject clone() {
        HitBox h = new HitBox(x, y, width, height, type);
        setClone(h);
        return h;
    }

    public int type() { return type.ordinal(); }
}
