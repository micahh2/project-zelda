package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.ImageRef;
import projectzelda.engine.RectangularGameObject;

public class DogNpc extends RectangularGameObject {

    String[] dogNpcTexts = {"Hey dog, what`s up?" , "Au Au", "Wow, that is really deep!", "Woof"};

    public DogNpc(double x, double y, int width, int height, ImageRef imageref) {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;

    }

    public String[] getText() {
        return dogNpcTexts;
    }

    public int type() { return Const.TYPE_DOG; }

}