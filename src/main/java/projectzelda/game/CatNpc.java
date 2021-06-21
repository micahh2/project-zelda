package projectzelda.game;

import projectzelda.engine.ImageRef;

public class CatNpc extends NPC {

    static String[] catNpcTexts = {"Hey cat, what`s up?" , "Miau!"};

    public CatNpc(double x, double y, int width, int height, ImageRef imageRef) {
        super(x, y, width, height, imageRef, catNpcTexts);
    }
}
