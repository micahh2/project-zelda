package projectzelda.game;

import projectzelda.engine.ImageRef;

public class OlgaNpc extends NPC {

    static String[] olgaNpcTexts = {"How are you doing Olga? That is an amazing name, btw!", "Horrible, there are monsters attacking us!",
            "As the main character of this game, I will defeat them!" , "Thanks, just go to the beach south of here."};

    public OlgaNpc(double x, double y, int width, int height, ImageRef imageRef)
    {
        super(x, y, width, height, imageRef, olgaNpcTexts);
    }
}
