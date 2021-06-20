package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.ImageRef;
import projectzelda.engine.RectangularGameObject;

public class BrutusNpc extends RectangularGameObject {

    String[] brutusNpcTexts = {"Wow Brutus, you are strong!" , "I drink a lot of Whey Protein!" , "And a lot of physical exercises, right?",
            "Why would I do physical exercises?", "Ahh... nevermind."};

    public BrutusNpc(double x, double y, int width, int height, ImageRef imageref) {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;

    }

    public String[] getText() {
        return brutusNpcTexts;
    }

    public int type() { return Const.TYPE_BRUTUS; }

}