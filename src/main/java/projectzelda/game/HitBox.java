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
    public HitBox(double x, double y, int width, int height, ImageRef imageRef, Const.Type type) {
        super(x, y, 0, 0, width, height, null);
        this.isMoving = false;
        this.type = type;
        this.imageRef = imageRef;
    }

    // Invisible
    @Override
    public void draw(GraphicSystem gs, long tick) { 
        if (imageRef != null) { gs.draw(this); }
    }

    @Override
    public GameObject clone() {
        HitBox h = new HitBox(x, y, width, height, imageRef, type);
        setClone(h);
        return h;
    }

    public int type() { return type.ordinal(); }
}
