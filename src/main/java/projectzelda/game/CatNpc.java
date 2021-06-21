package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.ImageRef;
import projectzelda.engine.RectangularGameObject;

public class CatNpc extends RectangularGameObject {

    private String[] catNpcText = {"Hey cat, what`s up?" , "Miau!", "He's so cute..","I want to take him home with me.","But I shouldn't.",
    "I'm sure it's owner loves him."};
    private String[] catNpcQuestText ={"Well you must be Bob's cat.", "Purrrrr", "Bob misses you a lot.", "Come with me I'll bring you home."};

    public CatNpc(double x, double y, int width, int height, ImageRef imageref) {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;

    }

    public String[] getCatNpcText() {
        return catNpcText;
    }
    public String getCatNpcText(int index) { return catNpcText[index]; }
    public void setCatNpcText(String[] dogNpcText) { this.catNpcText = dogNpcText; }
    public String[] getCatNpcQuestText() { return catNpcQuestText; }
    public int type() { return Const.TYPE_CAT; }

}
