package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.ImageRef;
import projectzelda.engine.RectangularGameObject;

public class DogNpc extends RectangularGameObject {

    String[] dogNpcText = {"Hey dog, what`s up?" , "Au Au.", "I wonder what you're doing out here alone..", "Woof."};
    String[] dogNpcQuestText = {"You must be Bob's dog", "*Excited Woof*", "Absolutely sounds like it","Come on boy let's go home"};



    public DogNpc(double x, double y, int width, int height, ImageRef imageref) {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;

    }

    public String[] getDogNpcText() {
        return dogNpcText;
    }
    public String getDogNpcText(int index) { return dogNpcText[index]; }
    public void setDogNpcText(String[] dogNpcText) { this.dogNpcText = dogNpcText; }
    public String[] getDogNpcQuestText() { return dogNpcQuestText;
    }
    public int type() { return Const.TYPE_DOG; }

}