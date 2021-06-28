package projectzelda.game;

import projectzelda.engine.ImageRef;

public class BobNpc extends NPC {

    static String[] bobNpcQuestWaiting = {
            "Bob: *Ignores you*",
            "Adlez: Wow, that's rude!",
            "Adlez: I must find Brutus."
    };

    static String[] bobNpcQuestStartText = {
            "Adlez: Hi, are you Bob?" ,
            "Bob: Yes, I am Bob!",
            "Adlez: I heard you lost your pets?",
            "Bob: Yes, sadly I was separated from them..",
            "Bob: ..when the monsters attacked.",
            "Adlez: That's so sad. Any idea where they might be?",
            "Bob: I've looked everywhere except for down south..",
            "Bob: ..past the bridge.",
            "Adlez: Why haven't you looked past the bridge?",
            "Bob: I'm afraid of water...",
            "Adlez: Oh, wow.. Ok, fair enough.",
            "Adlez: I'll go find your pets",
            "Bob: Thank you so much!",
    };
    static String[] bobNpcQuestInProgText = {
            "Bob: Did you find them?",
            "Adlez: No, I'm still searching.",
            "Bob: Good luck and thank you."
    };

    static String[] bobNpcQuestCompleteText = {
            "Bob: Oh my god you found them!",
            "Adlez: Indeed I did!",
            "Bob: Thank you so much it was freaking me out!",
            "Bob: I was so worried",
            "Adlez: You're welcome!",
            "Bob: Goodbye adventurer!",
    };

    static String[] bobNpcQuestPostText = {
            "Bob: Oh it's you again!",
            "Adlez: Yes. Is there anything else I can help you with?",
            "Bob: No, but perhaps Brutus has something else for you.",
            "Adlez: Thanks."
    };

    CatNpc cat;
    DogNpc dog;
    boolean hasDog = false;
    boolean hasCat = false;
    int foundDistance = 128;

    public BobNpc(double x, double y, int width, int height, ImageRef imageRef, CatNpc cat, DogNpc dog) {
        super(x, y, width, height, imageRef);
        this.dog = dog;
        this.cat = cat;
    }

    @Override
    public boolean hasQuestProgress(QuestState q) { 
            if (q == QuestState.BOB_IN_PROGRESS_CAT || q == QuestState.BOB_IN_PROGRESS_DOG) {
                return minDist() < foundDistance;
            }
            return false;
    }

    public double minDist() {
        double distCat = world.getPhysicsSystem().distance(x, y, cat.x, cat.y);
        double distDog = world.getPhysicsSystem().distance(x, y, dog.x, dog.y);
        if (hasCat && !hasDog) { return distDog; }
        if (!hasCat && hasDog) { return distCat; }
        return Math.min(distCat, distDog);
    }

    @Override
    public void makeQuestProgress(QuestState q) { 
            double distDog = world.getPhysicsSystem().distance(x, y, dog.x, dog.y);
            if (!hasDog && distDog < foundDistance) {
                hasDog = true;
                dog.setFollow(null);
                dog.setOrigin(x, y);
                return;
            }
            double distCat = world.getPhysicsSystem().distance(x, y, cat.x, cat.y);
            if (!hasCat && distCat < foundDistance) {
                hasCat = true;
                cat.setFollow(null);
                cat.setOrigin(x, y);
                return;
            }
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
            case BOB_START:
            case BOB_PETS_FOUND:
                return true;
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
            case BOB_IN_PROGRESS_CAT:
            case BOB_IN_PROGRESS_DOG:
            case BOB_COMPLETED:
            case BOSS:
            default:  // Java's a pretty horrible language, huh? 
                return false;
        }
    }
}
