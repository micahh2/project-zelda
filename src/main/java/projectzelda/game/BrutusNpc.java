package projectzelda.game;

import projectzelda.engine.ImageRef;

public class BrutusNpc extends RectangularGameObject {


    private String[] brutusNpcQuestText;

    private String[] brutusNpcQuestStartText = {
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
    private String[] brutusNpcQuestOlgaInProgText = {
            "Did you find Olga?",
            "No, can you tell me where she is?",
            "Shes close-by, just south-west of us.",
            "Ok, thank you"
    };



    private String[] brutusNpcQuestOlgaCompleteText = {
            "I heard you killed the monsters on the beach",
            "Many thanks to you!",
            "But theres still more to do!",
            "My friend Steve is terrified of a pumpkin.",
            "Go find him to the east and see what you can...",
            ".. do for him."
    };

    private String[] brutusNpcQuestSteveInProgText = {
            "Did you find Steve?",
            "No, can you tell me where he is?",
            "Hes near, just west of us.",
            "Ok, thank you"
    };



    private String[] brutusNpcQuestSteveCompleteText = {
            "Steve told me you got rid of the pumpkin.",
            "Much appreciated",
            "I have another task for you.",
            "Bob seems to have lost his pets.",
            "He has a cat and a dog.",
            "Could you return them to him please?",
            "Not a problem! I'll be back."
    };



    private String[] brutusNpcQuestBobInProgText = {
            "Did you find Bob?",
            "No, can you tell me where he is?",
            "Hes near, just east of us.",
            "Ok, thank you"
    };



    private String[] brutusNpcQuestBobCompleted = {
            "I just saw Bob's cat and dog run around.",
            "I'm glad you could save them. Thank you!",
            "They were really close.. Bob was just too scared..",
            "To walk over the bridge.",
            "Really? Bob? He's scared of water?",
            "That's news to me.",
            "Thank you I'll have a chat with him",
    };



    private String[] brutusNpcQuestCompleteText = {
            "Did you eat the pumpkin?",
            "Yes.",
            "Thank you it was freaking me out!",
            "You're welcome!",
            "*What a strange guy..*"
    };



    private String[] brutusNpcQuestPostText = {
            "Oh it's you again!",
            "Yes, it's me is there anything else I can do?",
            "No, but perhaps the others have something for you.",
            "Thanks."
    };





    private  boolean brutusNpcQuestStart = false;
    private boolean brutusNpcQuestInProg = false;
    private  boolean brutusNpcQuestCompleted = false;
    private boolean brutusNpcQuestPost = false;
    private boolean brutusNpcQuestFinalBoss = false;

    public BrutusNpc(double x, double y, int width, int height, ImageRef imageref) {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;
        brutusNpcQuestText = brutusNpcQuestStartText;

    }

    // quest text
    public String[] getBrutusNpcQuestText() { return brutusNpcQuestText; }
    public String getBrutusNpcQuestText(int index) { return brutusNpcQuestText[index]; }
    public void setBrutusNpcQuestText(String[] brutusNpcQuestText) { this.brutusNpcQuestText = brutusNpcQuestText; }

    // Olga quest dialog
    public String[] getBrutusNpcQuestOlgaInProgText() { return brutusNpcQuestOlgaInProgText; }
    public String[] getBrutusNpcQuestOlgaCompleteText() { return brutusNpcQuestOlgaCompleteText; }

    // Steve quest dialog
    public String[] getBrutusNpcQuestSteveInProgText() { return brutusNpcQuestSteveInProgText; }
    public String[] getBrutusNpcQuestSteveCompleteText() { return brutusNpcQuestSteveCompleteText; }

    // Bob quest dialog
    public String[] getBrutusNpcQuestBobInProgText() { return brutusNpcQuestBobInProgText; }
    public String[] getBrutusNpcQuestBobCompleted() { return brutusNpcQuestBobCompleted; }

    public String[] getBrutusNpcQuestCompleteText() { return brutusNpcQuestCompleteText; }
    public String[] getBrutusNpcQuestPostText() { return brutusNpcQuestPostText; }


    // track quest state

    public boolean isBrutusNpcQuestStart() { return brutusNpcQuestStart; }
    public void setBrutusNpcQuestStart(boolean brutusNpcQuestStart) { this.brutusNpcQuestStart = brutusNpcQuestStart; }

    public boolean isBrutusNpcQuestInProg() { return brutusNpcQuestInProg; }
    public void setBrutusNpcQuestInProg(boolean brutusNpcQuestInProg) { this.brutusNpcQuestInProg = brutusNpcQuestInProg; }

    public boolean isBrutusNpcQuestCompleted() { return brutusNpcQuestCompleted; }
    public void setBrutusNpcQuestCompleted(boolean brutusNpcQuestCompleted) { this.brutusNpcQuestCompleted = brutusNpcQuestCompleted; }

    public boolean isBrutusNpcQuestPost() { return brutusNpcQuestPost; }
    public void setBrutusNpcQuestPost(boolean brutusNpcQuestPost) { this.brutusNpcQuestPost = brutusNpcQuestPost; }

    public boolean isBrutusNpcQuestFinalBoss() { return brutusNpcQuestFinalBoss; }

    public void setBrutusNpcQuestFinalBoss(boolean brutusNpcQuestFinalBoss) { this.brutusNpcQuestFinalBoss = brutusNpcQuestFinalBoss; }

    public int type() { return Const.TYPE_BRUTUS; }

}
