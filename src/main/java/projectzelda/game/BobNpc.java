package projectzelda.game;

import projectzelda.engine.ImageRef;

public class BobNpc extends NPC {

    static String[] bobNpcQuestWaiting = {
            "*He ignores you*",
            "Wow that's rude!",
            "I must find Brutus."
    };

    static String[] bobNpcQuestStartText = {
            "Hi are you Bob?" ,
            "Yes I am Bob!",
            "I heard you lost your pets?",
            "Yes, sadly I was separated from them..",
            "..when the monsters attacked.",
            "That's so sad, any idea where they might be?",
            "I've looked everywhere except for down south..",
            "..past the bridge.",
            "Why haven't you looked past the bridge?",
            "I'm afraid of water...",
            "Oh wow.. ok fair enough.",
            "I'll go find your pets",
            "Thank you so much!",
    };
    static String[] bobNpcQuestInProgText = {
            "Did you find them?",
            "No, I'm still searching.",
            "Good luck and thank you."
    };

    static String[] bobNpcQuestCompleteText = {
            "Oh my god you found them!",
            "Indeed I did!",
            "Thank you so much it was freaking me out!",
            "I was so worried",
            "You're welcome!",
            "Good bye adventurer!",
    };

    static String[] bobNpcQuestPostText = {
            "Oh it's you again!",
            "Yes, it's me is there anything else I can do?",
            "No, but perhaps Brutus has something else for you.",
            "Thanks."
    };

    public BobNpc(double x, double y, int width, int height, ImageRef imageRef) {
        super(x, y, width, height, imageRef);

    }

    @Override
    public String[] getNpcQuestText(QuestState q) {
        switch(q) {
            case START:
            case OLGA:
            case OLGA_SWORD_SEARCH:
            case OLGA_SWORD_SEARCH_COMPLETED:
            case OLGA_MONSTERS:
            case OLGA_COMPLETED:
            case STEVE:
            case STEVE_START:
            case STEVE_IN_PROGRESS:
            case STEVE_COMPLETED:
            case BOB:
                return bobNpcQuestWaiting;
            case BOB_START:
                   return bobNpcQuestStartText;
            case BOB_IN_PROGRESS_CAT:
            case BOB_IN_PROGRESS_DOG:
                return bobNpcQuestInProgText;
            case BOB_PETS_FOUND:
                return bobNpcQuestCompleteText;
            case BOB_COMPLETED:

            case BOSS:
            default:  // Java's a pretty horrible language, huh? 
                return bobNpcQuestPostText;
        }
    }

    @Override
    public boolean progressFromTalk(QuestState q) {
        switch(q) {
            case START:
            case OLGA:
            case OLGA_SWORD_SEARCH:
            case OLGA_SWORD_SEARCH_COMPLETED:
            case OLGA_MONSTERS:
            case OLGA_COMPLETED:
            case STEVE:
            case STEVE_START:
            case STEVE_IN_PROGRESS:
            case STEVE_COMPLETED:
            case BOB:
                return false;
            case BOB_START:
                return true;
            case BOB_IN_PROGRESS_CAT:
            case BOB_IN_PROGRESS_DOG:
                return false;
            case BOB_PETS_FOUND:
                return true;
            case BOB_COMPLETED:
            case BOSS:
            default:  // Java's a pretty horrible language, huh? 
                return false;
        }
    }
}
