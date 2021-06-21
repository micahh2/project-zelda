package projectzelda.game;

import projectzelda.engine.ImageRef;

public class BrutusNpc extends NPC {

    static String[] brutusNpcTexts = {"Wow Brutus, you are strong!" , "I drink a lot of Whey Protein!" , "And a lot of physical exercises, right?",
            "Why would I do physical exercises?", "Ahh... nevermind."};

    public BrutusNpc(double x, double y, int width, int height, ImageRef imageRef) {
        super(x, y, width, height, imageRef, brutusNpcTexts);
    }
}
