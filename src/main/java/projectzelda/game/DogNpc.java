package projectzelda.game;

import projectzelda.engine.ImageRef;

public class DogNpc extends NPC {

    static String[] dogNpcTexts = {"Hey dog, what`s up?" , "Au Au", "Wow, that is really deep!", "Woof"};

    public DogNpc(double x, double y, int width, int height, ImageRef imageRef) {
        super(x, y, width, height, imageRef, dogNpcTexts);
    }
}
