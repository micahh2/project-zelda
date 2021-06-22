package projectzelda.game;

import projectzelda.engine.ImageRef;

public class CatNpc extends NPC {

    private String[] catNpcText = {"Hey cat, what`s up?" , "Miau!", "He's so cute..","I want to take him home with me.","But I shouldn't.",
    "I'm sure it's owner loves him."};
    private String[] catNpcQuestText ={"Well you must be Bob's cat.", "Purrrrr", "Bob misses you a lot.", "Come with me I'll bring you home."};

    public CatNpc(double x, double y, int width, int height, ImageRef imageRef) {
        super(x, y, width, height, imageRef);
    }

    @Override
    public String[] getNpcQuestText(QuestState q) {
        if (q == QuestState.BOB) {
            return catNpcQuestText;
        }
        return catNpcText;
    }

    @Override
    public int type() { return Const.Type.ANIMAL.ordinal(); }
}
