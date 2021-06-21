package projectzelda.game;

import projectzelda.engine.ImageRef;

public class OlgaNpc extends NPC {

    private String[] olgaNpcQuestText;


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
            "Just east of here! Hurry we don't have much time."
    };

    private String[] olgaNpcChestQuestCompleteText = {
            "Hey Olga I found the chest!",
            "Do you have the sword and shield?",
            "Yes! They're perfect!",
            "What's next?",
            "There are monsters south of us at the beach.",
            "My kids can't play there anymore.",
            "Can you get rid of them for us?",
            "Yes, of course!"
    };

    private String[] olgaNpcMonsterQuestStartText = {
            "Did you kill the monsters yet?",
            "No, I didn't",
            "Don't come back until you do"
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


    // overall quest completion
    private boolean olgaNpcQuestCompleted = false;

    // Chest portion of quest
    private  boolean olgaNpcChestQuestStart = false;
    private boolean olgaNpcChestQuestInProg = false;
    private  boolean olgaNpcChestQuestCompleted = false;
    private boolean olgaNpcChestQuestPost = false;



    // monster portion of quest
    private boolean olgaNpcMonsterQuestStart = false;
    private boolean olgaNpcMonsterQuestCompleted = false;

    public OlgaNpc(double x, double y, int width, int height, ImageRef imageRef)
    {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;
        olgaNpcQuestText = olgaNpcQuestStartText;
    }

    // obtain text
    public String[] getOlgaNpcQuestText() { return olgaNpcQuestText; }
    public String getOlgaNpcText(int index) { return olgaNpcQuestText[index]; }
    public void setOlgaNpcQuestText(String[] questText) { olgaNpcQuestText = questText; }
    public String[] getOlgaNpcQuestWaiting() { return olgaNpcQuestWaiting; }
    public String[] getOlgaNpcQuestStartText() { return olgaNpcQuestStartText; }
    public String[] getOlgaNpcChestQuestInProgText() { return olgaNpcChestQuestInProgText; }
    public String[] getOlgaNpcChestQuestCompleteText() { return olgaNpcChestQuestCompleteText; }
    public String[] getOlgaNpcMonsterQuestStartText() { return olgaNpcMonsterQuestStartText; }
    public String[] getOlgaNpcMonsterQuestCompletedText() { return olgaNpcMonsterQuestCompletedText; }
    public String[] getOlgaNpcPostQuestText() { return olgaNpcPostQuestText; }

    // track quest state
    public boolean isOlgaNpcQuestCompleted() { return olgaNpcQuestCompleted;
    }

    public void setOlgaNpcQuestCompleted(boolean olgaNpcQuestCompleted) { this.olgaNpcQuestCompleted = olgaNpcQuestCompleted;
    }



    // track Chest quest
    public boolean isOlgaNpcChestQuestStart() { return olgaNpcChestQuestStart; }
    public void setOlgaNpcChestQuestStart(boolean olgaNpcChestQuestStart) { this.olgaNpcChestQuestStart = olgaNpcChestQuestStart; }

    public boolean isOlgaNpcChestQuestInProg() { return olgaNpcChestQuestInProg; }
    public void setOlgaNpcChestQuestInProg(boolean olgaNpcChestQuestInProg) { this.olgaNpcChestQuestInProg = olgaNpcChestQuestInProg; }

    public boolean isOlgaNpcChestQuestCompleted() { return olgaNpcChestQuestCompleted; }
    public void setOlgaNpcChestQuestCompleted(boolean olgaNpcChestQuestCompleted) { this.olgaNpcChestQuestCompleted = olgaNpcChestQuestCompleted; }

    public boolean isOlgaNpcQuestPost() { return olgaNpcChestQuestPost; }
    public void setOlgaNpcQuestPost(boolean olgaNpcQuestPost) { this.olgaNpcChestQuestPost = olgaNpcQuestPost; }

    // track monster quest
    public boolean isOlgaNpcMonsterQuestCompleted() { return olgaNpcMonsterQuestCompleted; }
    public void setOlgaNpcMonsterQuestCompleted(boolean olgaNpcMonsterQuestCompleted) { this.olgaNpcMonsterQuestCompleted  = olgaNpcMonsterQuestCompleted; }

    public boolean isOlgaNpcMonsterQuestStart() { return olgaNpcMonsterQuestStart; }
    public void setOlgaNpcMonsterQuestStart(boolean olgaNpcMonsterQuestStart) { this.olgaNpcMonsterQuestStart = olgaNpcMonsterQuestStart; }

    public int type() { return Const.TYPE_OLGA; }

}
