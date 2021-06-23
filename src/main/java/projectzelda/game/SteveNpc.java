package projectzelda.game;

import projectzelda.engine.ImageRef;

public class SteveNpc extends NPC {

    private String[] steveNpcQuestText;


    private String[] steveNpcQuestWaiting = {
            "Steve: *Ignores you*",
            "Adlez: Wow, that's rude!",
            "Adlez: I must find Brutus."
    };

    private String[] steveNpcQuestStartText = {
            "Adlez: Hi, are you Steve?" ,
            "Steve: Yes, I am Steve!",
            "Adlez: Hello Steve. Is there anything to do around here?",
            "Steve: There is a pumpkin south of here. EAT IT!!!!",
            "Adlez: That's great. I love pumpkin. I will take a look!"
    };
    private String[] steveNpcQuestInProgText = {
            "Steve: Did you eat the pumpkin?",
            "Adlez: No, can you tell me where it is again?",
            "Steve: Directly south of here. Leave now and go eat it!"
    };

    private String[] steveNpcQuestCompleteText = {
            "Steve: Did you eat the pumpkin?",
            "Adlez: Yes.",
            "Steve: Thank you! It was freaking me out!",
            "Adlez: You're welcome!",
            "Adlez: *What a strange guy..*"
    };

    private String[] steveNpcQuestPostText = {
            "Steve: Oh, it's you again!",
            "Adlez: Yes, it's me. Is there anything else I can do?",
            "Steve: No, but perhaps Brutus has something else for you.",
            "Adlez: Thanks."
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
