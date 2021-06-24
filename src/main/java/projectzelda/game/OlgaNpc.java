package projectzelda.game;

import projectzelda.engine.ImageRef;

public class OlgaNpc extends NPC {

    private String[] olgaNpcQuestWaiting = {
            "Olga: *Ignores you*",
            "Adlez: Wow, that's rude!",
            "Adlez: I must find Brutus."
    };

    private String[] olgaNpcQuestStartText = {
            "Adlez: Hello, who are you?" ,
            "Olga: Hi. I'm Olga!",
            "Adlez: Hello Olga. Brutus sent me.",
            "Adlez: He said you'd have something for me.",
            "Adlez: To help me defeat the monsters.",
            "Olga: He's right! I do",
            "Olga: My grandfather put his sword..",
            "Olga: .. in a chest just west of here.",
            "Olga: Here's the key for it!",
            "Adlez: Wow perfect, thank you!"
    };
    private String[] olgaNpcChestQuestInProgText = {
            "Olga: Did you find the chest?",
            "Adlez: No, can you tell me where it is again?",
            "Olga: Just west of here! Hurry, we don't have much time."
    };

    private String[] olgaNpcChestQuestCompleteText = {
            "Adlez: Hey Olga. I found the chest!",
            "Olga: Do you have the sword?",
            "Adlez: Yes! It's perfect!",
            "Adlez: What's next?",
            "Olga: There are monsters south of us at the beach.",
            "Olga: My kids can't play there anymore.",
            "Olga: Can you get rid of them for us?",
            "Adlez: Yes, of course!",
            "Olga: Thank you! Don't forget to bring me..",
            "Olga: ..all 6 of their bones!"
    };

    private String[] olgaNpcMonsterQuestStartText = {
            "Olga: Have you killed the monsters yet?",
            "Adlez: No, I haven't.",
            "Olga: Don't come back until you do.",
            "Olga: Remember I want to see 6 bones!"
    };




    private String[] olgaNpcMonsterQuestCompletedText = {
            "Adlez: Olga, I killed all the monsters!",
            "Adlez: Here are their bones.",
            "Olga: Wow! Thank you so much",
            "Olga: My kids will really appreciate this!",
    };



    private String[] olgaNpcPostQuestText = {
            "Adlez: Is there anything else I can do for you?",
            "Olga: No, you've done more than enough. Thank you!",
            "Olga: Maybe Brutus has some more things for you to do though!",
            "Adlez: Ok, thank you! Goodbye!",
            "Olga: Goodbye adventurer!"
    };


    public OlgaNpc(double x, double y, int width, int height, ImageRef imageRef)
    {
        super(x, y, width, height, imageRef);
    }

    @Override
    public String[] getNpcQuestText(QuestState q) {
        switch(q) {
            case START:
                return olgaNpcQuestWaiting;
            case OLGA:
                return olgaNpcQuestStartText;
            case OLGA_SWORD_SEARCH:
                return olgaNpcChestQuestInProgText;
            case OLGA_SWORD_SEARCH_COMPLETED:
                return olgaNpcChestQuestCompleteText;
            case OLGA_MONSTERS:
                return olgaNpcMonsterQuestStartText;
            case OLGA_COMPLETED:
                return olgaNpcMonsterQuestCompletedText;
            case STEVE:
            case STEVE_START:
            case STEVE_IN_PROGRESS:
            case STEVE_COMPLETED:
            case BOB:
            case BOB_START:
            case BOB_IN_PROGRESS_CAT:
            case BOB_IN_PROGRESS_DOG:
            case BOB_PETS_FOUND:
            case BOB_COMPLETED:
            case BOSS:
            default:  // Java's a pretty horrible language, huh? 
                return olgaNpcPostQuestText;
        }
    }

    @Override
    public boolean progressFromTalk(QuestState q) {
        switch(q) {
            case OLGA:
                return true;
            case OLGA_SWORD_SEARCH:
                return false;
            case OLGA_SWORD_SEARCH_COMPLETED:
                return true;
            case OLGA_MONSTERS:
                return false;
            case OLGA_COMPLETED:
                return true;
            case STEVE:
            case STEVE_START:
            case STEVE_IN_PROGRESS:
            case STEVE_COMPLETED:
            case BOB:
            case BOB_START:
            case BOB_IN_PROGRESS_CAT:
            case BOB_IN_PROGRESS_DOG:
            case BOB_PETS_FOUND:
            case BOB_COMPLETED:
            case BOSS:
            case START:
            default:  // Java's a pretty horrible language, huh? 
                return false;
        }
    }
}
