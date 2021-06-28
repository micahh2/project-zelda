package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;

public class Chest extends RectangularGameObject {

    
    private String[] chestText = {
            "Adlez: Hmm, it's locked.",
            "Adlez: Maybe I can find the key somewhere."};

    private String[] chestQuestText = {
            "*You open the chest...*",
            "*In it, you find...*",
            "*..a rather large sword!*",
            "Adlez: Wow! This is exactly what I will need.",
            "Adlez: I'll go tell Olga!"};

    public Chest(double x, double y, int width, int height, ImageRef imageref)
    {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;
    }

    public String[] getChestText() { return chestText; }
    public String getChestText(int index) { return chestText[index]; }
    public void setChestText(String[] chestText) { this.chestText = chestText; }

    public String[] getChestQuestText() { return chestQuestText; }
    public int type() { return Const.Type.CHEST.ordinal(); }

}
