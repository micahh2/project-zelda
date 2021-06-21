package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;

public class Pumpkin extends RectangularGameObject {




    private String[] pumpkinText = {"Whos pumpkin is this?", "Can I eat it?", "It looks very tasty...", "Perhaps I can find out."};
    private String[] pumpkinQuestText = {"You found a pumpkin...", "It's huge!", "You eat it.", "It's very tasty.",
            "God I love pumpkin.", "I'd better tell Steve."};

    public Pumpkin(double x, double y, int width, int height, ImageRef imageref)
    {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;
    }

    public String[] getPumpkinText() {
        return pumpkinText;
    }
    public String getPumpkinText(int index) { return pumpkinText[index]; }
    public void setPumpkinText(String[] pumpkinText) { this.pumpkinText = pumpkinText; }
    public String[] getPumpkinQuestText() { return pumpkinQuestText;
    }
    public int type() { return Const.TYPE_PUMPKIN; }

}
