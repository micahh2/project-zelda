package projectzelda.game;

import projectzelda.engine.ImageRef;

public class BrutusNpc extends NPC {

    static String[] brutusNpcQuestStartText = {
            "Hello who are you?" ,
            "Hello I'm Brutus the mayor of Kratzerville!",
            "Hi Brutus. " ,
            "I'm a traveller looking for adventure.",
            "Is there anything to do around here?",
            "Funny you say that.",
            "I do need some help. Could you help me?",
            "Of course, what do you need?",
            "Our town used to be a fun happy place.",
            "However that changed when some monsters settled nearby.",
            "They raid our houses and occupy the beach.",
            "We are scared to live our daily lives.",
            "Please help us! Defeat the monsters and their nest.",
            "I believe I can help.",
            "What should I do first?",
            "Go find Olga she might have something for you.",
            "Great!",

    };
    static String[] brutusNpcQuestOlgaInProgText = {
            "Did you find Olga?",
            "No, can you tell me where she is?",
            "Shes close-by, just south-east of us.",
            "Ok, thank you"
    };



    static String[] brutusNpcQuestOlgaCompleteText = {
            "I heard you killed the monsters on the beach",
            "Many thanks to you!",
            "But theres still more to do!",
            "My friend Steve is terrified of a pumpkin.",
            "Go find him to the west and see what you can...",
            ".. do for him."
    };

    static String[] brutusNpcQuestSteveInProgText = {
            "Did you find Steve?",
            "No, can you tell me where he is?",
            "Hes near, just west of us.",
            "Ok, thank you"
    };



    static String[] brutusNpcQuestSteveCompleteText = {
            "Steve told me you got rid of the pumpkin.",
            "Much appreciated",
            "I have another task for you.",
            "Bob seems to have lost his pets.",
            "He has a cat and a dog.",
            "Could you return them to him please?",
            "Not a problem! I'll be back."
    };



    static String[] brutusNpcQuestBobInProgText = {
            "Did you find Bob?",
            "No, can you tell me where he is?",
            "Hes near, just east of us.",
            "Ok, thank you"
    };



    static String[] brutusNpcQuestBobCompleted = {
            "I just saw Bob's cat and dog run around.",
            "I'm glad you could save them. Thank you!",
            "They were really close.. Bob was just too scared..",
            "To walk over the bridge.",
            "Really? Bob? He's scared of water?",
            "That's news to me.",
            "Thank you I'll have a chat with him",
    };



    static String[] brutusNpcQuestCompleteText = {
            "Did you eat the pumpkin?",
            "Yes.",
            "Thank you it was freaking me out!",
            "You're welcome!",
            "*What a strange guy..*"
    };



    static String[] brutusNpcQuestPostText = {
            "Oh it's you again!",
            "Thank you for all the help around here!"

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
