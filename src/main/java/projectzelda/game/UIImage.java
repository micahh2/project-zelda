package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;

public class UIImage extends UIObject {

    ImageRef imageRef;

    public UIImage(int x, int y, ImageRef imageRef) {
        super(x, y);
        this.imageRef = imageRef;
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        gs.drawImage(imageRef, x, y, x+imageRef.x2, y+imageRef.y2);
    }
}
