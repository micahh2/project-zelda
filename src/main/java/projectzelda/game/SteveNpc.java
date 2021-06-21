package projectzelda.game;

import projectzelda.engine.ImageRef;

public class SteveNpc extends NPC {

    static String[] steveNpcTexts = {"Hey Steve, how is it going?" , "There is a pumpking south of here. EAT IT!!!!",
            "Wow, thanks for the information. I will take a look!"};

    public SteveNpc(double x, double y, int width, int height, ImageRef imageRef) {
        super(x, y, width, height, imageRef, steveNpcTexts);
    }
}
