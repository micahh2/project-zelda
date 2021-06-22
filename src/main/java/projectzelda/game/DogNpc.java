package projectzelda.game;

import projectzelda.engine.ImageRef;

public class DogNpc extends NPC {

    static String[] dogNpcText = {"Hey dog, what`s up?" , "Au Au.", "I wonder what you're doing out here alone..", "Woof."};
    static String[] dogNpcQuestText = {"You must be Bob's dog", "*Excited Woof*", "Absolutely sounds like it","Come on boy let's go home"};

    public DogNpc(double x, double y, int width, int height, ImageRef imageRef) {
        super(x, y, width, height, imageRef);
    }

    @Override
    public String[] getNpcQuestText(QuestState q) {
        if (q == QuestState.BOB) {
            return dogNpcQuestText;
        }
        return dogNpcText;
    }

    @Override
    public int type() { return Const.Type.ANIMAL.ordinal(); }
}
