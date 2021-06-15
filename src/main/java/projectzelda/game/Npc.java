package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;

public class Npc extends RectangularGameObject {

    public Npc(double x, double y, int width, int height, ImageRef imageref)
    {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;
    }

    public int type() { return Const.TYPE_TREE; }

}
