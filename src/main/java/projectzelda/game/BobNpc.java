package projectzelda.game;

import projectzelda.engine.ImageRef;

public class BobNpc extends NPC {

    static String[] bobNpcTexts = {"What are you doing Bob?" , "Just thinking about life."};

    public BobNpc(double x, double y, int width, int height, ImageRef imageRef) {
        super(x, y, width, height, imageRef, bobNpcTexts);
    }
}
