package projectzelda.game;

import projectzelda.engine.ImageRef;

public class BrutusNpc extends NPC {

    static String[] brutusNpcQuestStartText = {
            "Adlez: Hello, who are you?" ,
            "Brutus: Hello. I'm Brutus, the mayor of Kratzerburg!",
            "Adlez: Hi Brutus!" ,
            "Adlez: I'm a traveller looking for an adventure.",
            "Adlez: Is there anything to do around here?",
            "Brutus: Funny you say that.",
            "Brutus: I do need some help. Could you help me?",
            "Adlez: Of course, what do you need?",
            "Brutus: Our town used to be a fun happy place.",
            "Brutus: However that changed when..",
            "..some monsters settled nearby.",
            "Brutus: They raid our houses and occupy the beach.",
            "Brutus: We are scared to live our daily lives.",
            "Please, help us! Defeat the monsters and their nest.",
            "Adlez: I believe I can help.",
            "Adlez: What should I do first?",
            "Brutus: Go find Olga. She might have something for you.",
            "Adlez: Great!",

    };
    static String[] brutusNpcQuestOlgaInProgText = {
            "Brutus: Did you find Olga?",
            "Adlez: No, can you tell me where she is?",
            "Brutus: She is close-by, just south-east of us.",
            "Adlez: Ok, thank you"
    };



    static String[] brutusNpcQuestOlgaCompleteText = {
            "Brutus: I heard you killed the monsters on the beach.",
            "Brutus: Many thanks to you!",
            "Brutus: But there is still more to do!",
            "Brutus: My friend Steve is upset about something.",
            "Brutus: Go find him to the west and see what you can...",
            "Brutus: .. do for him."
    };

    static String[] brutusNpcQuestSteveInProgText = {
            "Brutus: Did you find Steve?",
            "Adlez: No, can you tell me where he is?",
            "Brutus: He is near, just west of us.",
            "Adlez: Ok, thank you!"
    };



    static String[] brutusNpcQuestSteveCompleteText = {
            "Brutus: Steve told me you got rid of the pumpkin.",
            "Brutus: Much appreciated!",
            "Brutus: I have another task for you.",
            "Brutus: Bob seems so sad lately.",
            "Brutus: Maybe you can check in on him?.",
            "Adlez: Not a problem! I'll be right back."
    };



    static String[] brutusNpcQuestBobInProgText = {
            "Brutus: Did you find Bob?",
            "Adlez: No, can you tell me where he is?",
            "Brutus: He is near, just east of us.",
            "Adlez: Ok, thank you!"
    };



    static String[] brutusNpcQuestBobCompleted = {
            "Brutus: I just saw Bob's cat and dog run around.",
            "Brutus: I'm glad you could save them. Thank you!",
            "Adlez: They were really close. Bob was just too scared..",
            "Adlez: to walk over the bridge.",
            "Brutus: Really? Bob? He's scared of water?",
            "Brutus: That's news to me.",
            "Brutus: Thank you! I'll have a chat with him.",
    };



    static String[] brutusNpcQuestCompleteText = {
            "Brutus: Did you eat the pumpkin?",
            "Adlez: Yes.",
            "Brutus: Thank you! It was freaking me out!",
            "Adlez: You're welcome!",
            "Adlez: *What a strange guy..*"
    };



    static String[] brutusNpcQuestPostText = {
            "Brutus: Oh, it's you again!",
            "Brutus: Thank you for all the help around here!"

    };

    public BrutusNpc(double x, double y, int width, int height, ImageRef imageRef) {
        super(x, y, width, height, imageRef);
    }

    @Override
    public String[] getNpcQuestText(QuestState q) {
        switch(q) {
            case START:
                return brutusNpcQuestStartText;
            case OLGA:
            case OLGA_SWORD_SEARCH:
            case OLGA_SWORD_SEARCH_COMPLETED:
            case OLGA_MONSTERS:
            case OLGA_COMPLETED:
                return brutusNpcQuestOlgaInProgText;
            case STEVE:
                return brutusNpcQuestOlgaCompleteText;
            case STEVE_START:
            case STEVE_IN_PROGRESS:
            case STEVE_COMPLETED:
                return brutusNpcQuestSteveInProgText;
            case BOB:
                return brutusNpcQuestSteveCompleteText;
            case BOB_START:
            case BOB_IN_PROGRESS_CAT:
            case BOB_IN_PROGRESS_DOG:
            case BOB_PETS_FOUND:
                return brutusNpcQuestBobInProgText;
            case BOB_COMPLETED:
                return brutusNpcQuestBobCompleted;

            case BOSS:
            default:  // Java's a pretty horrible language, huh? 
                return brutusNpcQuestPostText;
        }
    }

    @Override
    public boolean progressFromTalk(QuestState q) {
        switch(q) {
            case START:
                return true;
            case OLGA:
            case OLGA_SWORD_SEARCH:
            case OLGA_SWORD_SEARCH_COMPLETED:
            case OLGA_MONSTERS:
            case OLGA_COMPLETED:
                return false;
            case STEVE:
                return true;
            case STEVE_START:
            case STEVE_IN_PROGRESS:
            case STEVE_COMPLETED:
                return false;
            case BOB:
                return true;
            case BOB_START:
            case BOB_IN_PROGRESS_CAT:
            case BOB_IN_PROGRESS_DOG:
            case BOB_PETS_FOUND:
                return false;
            case BOB_COMPLETED:
                return true;
            case BOSS:
            default:  // Java's a pretty horrible language, huh? 
                return false;
        }
    }
}
