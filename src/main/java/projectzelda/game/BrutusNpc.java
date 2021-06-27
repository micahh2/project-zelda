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
            "Brutus: Thank you! I'll have a chat with him..",
            "Brutus: .. I've only known him to be brave. ",
            "Brutus: In that case I don't think I can..",
            "Brutus: .. rely on him for the next task.",
            "Brutus: I have one last thing to ask of you.",
            "Brutus: We recently found the monster's lair..",
            "Brutus: .. I believe their king lives there and ..",
            "Brutus: .. if he's killed the monsters will disappear.",
            "Brutus: Can you please deal with it?",
            "Adlez: After all I've already done I don't think.. ",
            "Adlez: .. that'll be an issue!",
            "Brutus: Before you go, take this.",
            "Brutus: *hands you a bow and some arrows*",
            "Adlez: What's this for?",
            "Brutus: A gift. It belonged to my grandfather.",
            "Brutus: The arrows are made of a special metal..",
            "Brutus: ..perhaps they'll be of use to you.",
            "Brutus: I'm too old to use it nowadays.",
            "Adlez: Thank you very much. I'll make good use of it.",
            "Brutus: You're a true hero, thank you.",
    };

    static String[] brutusNpcQuestBoss = {
            "Brutus: Did you kill it yet?",
            "Adlez: No, not yet.",
            "Adlez: I forgot where to go.",
            "Brutus: Just south-west of us. Please hurry!"

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
                return brutusNpcQuestBoss;
          /*  default:
                return brutusNpcQuestPostText; */
        }
    }

    @Override
    public boolean progressFromTalk(QuestState q) {
        switch(q) {

            case START:
            case STEVE:
            case BOB:
            case BOB_COMPLETED:
                return true;
            case OLGA:
            case OLGA_SWORD_SEARCH:
            case OLGA_SWORD_SEARCH_COMPLETED:
            case OLGA_MONSTERS:
            case OLGA_COMPLETED:
            case STEVE_START:
            case STEVE_IN_PROGRESS:
            case STEVE_COMPLETED:
            case BOB_START:
            case BOB_IN_PROGRESS_CAT:
            case BOB_IN_PROGRESS_DOG:
            case BOB_PETS_FOUND:
            case BOSS:
            default:  // Java's a pretty horrible language, huh? 
                return false;


           /* case BOB_PETS_FOUND:
                return true;
            default:
                return false;*/
        }
    }
}
