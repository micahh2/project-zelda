package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;

public class Chest extends RectangularGameObject {

    String[] chestTexts = {"You found loot...", "It's a big sword!"};

    public Chest(double x, double y, int width, int height, ImageRef imageref)
    {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;


    }

    public String[] getText() {
        return chestTexts;
    }

    public int type() { return Const.TYPE_CHEST; }

}
