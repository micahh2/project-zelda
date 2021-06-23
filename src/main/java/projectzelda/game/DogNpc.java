package projectzelda.game;

import projectzelda.engine.ImageRef;

public class DogNpc extends NPC {

    static String[] dogNpcText = {
            "Adlez: Hey dog, what`s up?" ,
            "Dog: Au Au.",
            "Adlez: I wonder what you're doing out here alone..",
            "Dog: Woof."
    };
    static String[] dogNpcQuestText = {
            "Adlez: You must be Bob's dog.",
            "Dog: *Excited Woof*",
            "Adlez: Absolutely sounds like it.",
            "Adlez: Come on boy, let's go home."
    };

    public DogNpc(double x, double y, int width, int height, ImageRef imageRef) {
        super(x, y, width, height, imageRef);
    }

    @Override
    public String[] getNpcQuestText(QuestState q) {
        if (q == QuestState.BOB_IN_PROGRESS_CAT) {
            return dogNpcQuestText;
        } else if (q == QuestState.BOB_IN_PROGRESS_DOG) {
            return dogNpcQuestText;

        }
        return dogNpcText;
    }

    @Override
    public int type() { return Const.Type.ANIMAL.ordinal(); }
}
