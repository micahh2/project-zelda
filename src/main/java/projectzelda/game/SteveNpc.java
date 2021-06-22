package projectzelda.game;

import projectzelda.engine.ImageRef;

public class SteveNpc extends NPC {

    private String[] steveNpcQuestText;


    private String[] steveNpcQuestWaiting = {
            "*He ignores you*",
            "Wow that's rude!",
            "I must find Brutus."
    };

    private String[] steveNpcQuestStartText = {
            "Hi are you Steve?" ,
            "Yes I am Steve!",
            "Hello Steve is there anything to do around here?",
            "There is a pumpkin south of here. EAT IT!!!!",
            "That's great I love pumpkin. I will take a look!"
    };
    private String[] steveNpcQuestInProgText = {
            "Did you eat the pumpkin?",
            "No, can you tell me where it is again?",
            "Directly south of here, leave now and go eat it!"
    };

    private String[] steveNpcQuestCompleteText = {
            "Did you eat the pumpkin?",
            "Yes.",
            "Thank you it was freaking me out!",
            "You're welcome!",
            "*What a strange guy..*"
    };

    private String[] steveNpcQuestPostText = {
            "Oh it's you again!",
            "Yes, it's me is there anything else I can do?",
            "No, but perhaps Brutus has something else for you.",
            "Thanks."
    };

    public SteveNpc(double x, double y, int width, int height, ImageRef imageRef) {
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
                return steveNpcQuestWaiting;
            case STEVE_START:
                return steveNpcQuestStartText;
            case STEVE_IN_PROGRESS:
                return steveNpcQuestInProgText;
            case STEVE_COMPLETED:
                return steveNpcQuestCompleteText;
            case BOB:
            case BOB_START:
            case BOB_IN_PROGRESS_CAT:
            case BOB_IN_PROGRESS_DOG:
            case BOB_PETS_FOUND:
            case BOB_COMPLETED:
            case BOSS:
            default:  // Java's a pretty horrible language, huh? 
                return steveNpcQuestPostText;
        }
    }

    @Override
    public boolean progressFromTalk(QuestState q) {
        switch(q) {
            case OLGA:
            case OLGA_SWORD_SEARCH:
            case OLGA_SWORD_SEARCH_COMPLETED:
            case OLGA_MONSTERS:
            case OLGA_COMPLETED:
            case STEVE:
                return false;
            case STEVE_START:
                return true;
            case STEVE_IN_PROGRESS:
                return false;
            case STEVE_COMPLETED:
                return true;
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
