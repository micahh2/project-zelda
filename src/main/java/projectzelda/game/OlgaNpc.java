package projectzelda.game;

import projectzelda.engine.ImageRef;

public class OlgaNpc extends NPC {

    private String[] olgaNpcQuestWaiting = {
            "*She ignores you*",
            "Wow that's rude!",
            "I must find Brutus."
    };

    private String[] olgaNpcQuestStartText = {
            "Hello who are you?" ,
            "Hi I'm Olga!",
            "Hello Olga, Brutus sent me.",
            "He said you'd have something for me.",
            "To help defeat the monsters.",
            "He's right! I do",
            "My grandfather put his sword and shield..",
            ".. in a chest just east of here.",
            "Here's the key for it!",
            "Wow perfect, thank you!"
    };
    private String[] olgaNpcChestQuestInProgText = {
            "Did you find the chest?",
            "No, can you tell me where it is again?",
            "Just west of here! Hurry we don't have much time."
    };

    private String[] olgaNpcChestQuestCompleteText = {
            "Hey Olga I found the chest!",
            "Do you have the sword and shield?",
            "Yes! They're perfect!",
            "What's next?",
            "There are monsters south of us at the beach.",
            "My kids can't play there anymore.",
            "Can you get rid of them for us?",
            "Yes, of course!",
            "Thank you, don't forget to bring me..",
            ".. all 6 of their bones!"
    };

    private String[] olgaNpcMonsterQuestStartText = {
            "Did you kill the monsters yet?",
            "No, I didn't.",
            "Don't come back until you do.",
            "Remember I want to see 6 bones!"
    };




    private String[] olgaNpcMonsterQuestCompletedText = {
            "Olga, I killed all the monsters!",
            "Here are their bones",
            "Wow! Thank you so much",
            "My kids will really appreciate this!",
    };



    private String[] olgaNpcPostQuestText = {
            "Is there anything else I can do for you?",
            "No you've done more than enough! Thank you!",
            "Maybe Brutus has some more for you to do though!",
            "Ok thank you, bye!",
            "Bye adventurer!"
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
