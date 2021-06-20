package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.ImageRef;
import projectzelda.engine.RectangularGameObject;

public class SteveNpc extends RectangularGameObject {

    String[] steveNpcTexts = {"Hey Steve, how is it going?" , "There is a pumpking south of here. EAT IT!!!!",
            "Wow, thanks for the information. I will take a look!"};

    public SteveNpc(double x, double y, int width, int height, ImageRef imageref) {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;

    }

    public String[] getText() {
        return steveNpcTexts;
    }

    public int type() { return Const.TYPE_STEVE; }

}