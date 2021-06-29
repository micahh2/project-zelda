package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;

public class Pumpkin extends RectangularGameObject {

    private String[] pumpkinText = {
            "Adlez: Whose pumpkin is this?",
            "Adlez: Can I eat it?",
            "Adlez: It looks very tasty...",
            "Adlez: Perhaps I can find out."
    };
    private String[] pumpkinQuestText = {
            "You found a pumpkin...",
            "It's huge!",
            "You eat it.",
            "It's very tasty.",
            "Adlez: God, I love pumpkin.",
            "Adlez: I'd better tell Steve."};

    public Pumpkin(double x, double y, int width, int height, ImageRef imageref)
    {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;
    }

    public String[] getPumpkinText() { return pumpkinText; }

    public String getPumpkinText(int index) { return pumpkinText[index]; }

    public void setPumpkinText(String[] pumpkinText) { this.pumpkinText = pumpkinText; }

    public String[] getPumpkinQuestText() { return pumpkinQuestText; }

    public GameObject clone() {
        Pumpkin c = new Pumpkin(x, y, width, height, imageRef);
        c.setPumpkinText(getPumpkinText());

        return c;
    }

    public int type() { return Const.Type.PUMPKIN.ordinal(); }
}
