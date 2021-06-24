package projectzelda.game;

import projectzelda.engine.ImageRef;

public class CatNpc extends NPC {

    private String[] catNpcText = {
            "Adlez: Hey cat, what`s up?",
            "Cat: Miau!", "Adlez: It's so cute..",
            "Adlez: I want to take it home with me.",
            "Adlez: But I shouldn't.",
            "I'm sure its owner loves it."
    };
    private String[] catNpcQuestText = {
            "Adlez: Well, you must be Bob's cat.",
            "Cat: Purrrrr",
            "Adlez: Bob misses you a lot.",
            "Adlez: Come with me. I'll take you home.",
            "Cat: *follows you*"
    };

    public CatNpc(double x, double y, int width, int height, ImageRef imageRef) {
        super(x, y, width, height, imageRef);
    }

    @Override
    public String[] getNpcQuestText(QuestState q) {
        if (q == QuestState.BOB_IN_PROGRESS_CAT || q == QuestState.BOB_IN_PROGRESS_DOG) {
            return catNpcQuestText;
        }
        return catNpcText;
    }

    @Override
    public int type() {
        return Const.Type.ANIMAL.ordinal();
    }
}
