package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;

public class Pumpkin extends RectangularGameObject {

    String[] pumpkinTexts = {"You found a pumpkin...", "It's huge!", "You eat it.", "It's very tasty."};

    public Pumpkin(double x, double y, int width, int height, ImageRef imageref)
    {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;
    }

    public String[] getText() {
        return pumpkinTexts;
    }

    public int type() { return Const.TYPE_PUMPKIN; }

}
