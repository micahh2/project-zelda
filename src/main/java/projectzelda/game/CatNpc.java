package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.ImageRef;
import projectzelda.engine.RectangularGameObject;

public class CatNpc extends RectangularGameObject {

    String[] catNpcTexts = {"Hey cat, what`s up?" , "Miau!"};

    public CatNpc(double x, double y, int width, int height, ImageRef imageref) {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;

    }

    public String[] getText() {
        return catNpcTexts;
    }

    public int type() { return Const.TYPE_CAT; }

}
