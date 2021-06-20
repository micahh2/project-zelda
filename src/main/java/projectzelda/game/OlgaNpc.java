package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.ImageRef;
import projectzelda.engine.RectangularGameObject;

public class OlgaNpc extends RectangularGameObject {

    String[] olgaNpcTexts = {"How are you doing Olga? That is an amazing name, btw!", "Horrible, there are monsters attacking us!",
            "As the main character of this game, I will defeat them!" , "Thanks, just go to the beach south of here."};

    public OlgaNpc(double x, double y, int width, int height, ImageRef imageref)
    {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;
    }

    public String[] getText() {
        return olgaNpcTexts;
    }

    public int type() { return Const.TYPE_OLGA; }

}