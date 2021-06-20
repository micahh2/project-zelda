package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.ImageRef;
import projectzelda.engine.RectangularGameObject;

public class BobNpc extends RectangularGameObject {

    String[] bobNpcTexts = {"What are you doing Bob?" , "Just thinking about life."};

    public BobNpc(double x, double y, int width, int height, ImageRef imageref) {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;

    }

    public String[] getText() {
        return bobNpcTexts;
    }

    public int type() { return Const.TYPE_BOB; }

}