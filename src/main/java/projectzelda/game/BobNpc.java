package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.ImageRef;
import projectzelda.engine.RectangularGameObject;

public class BobNpc extends RectangularGameObject {

    private String[] bobNpcQuestText;


    private String[] bobNpcQuestWaiting = {
            "*He ignores you*",
            "Wow that's rude!",
            "I must find Brutus."
    };

    private String[] bobNpcQuestStartText = {
            "Hi are you Bob?" ,
            "Yes I am Bob!",
            "I heard you lost your pets?",
            "Yes, sadly I was separated from them..",
            "..when the monsters attacked.",
            "That's so sad, any idea where they might be?",
            "I've looked everywhere except for down south..",
            "..past the bridge.",
            "Why haven't you looked past the bridge?",
            "I'm afraid of water...",
            "Oh wow.. ok fair enough.",
            "I'll go find your pets",
            "Thank you so much!",
    };
    private String[] bobNpcQuestInProgText = {
            "Did you find them?",
            "No, I'm still searching.",
            "Good luck and thank you."
    };

    private String[] bobNpcQuestCompleteText = {
            "Oh my god you found them!",
            "Indeed I did!",
            "Thank you so much it was freaking me out!",
            "I was so worried",
            "You're welcome!",
            "Good bye adventurer!",
    };

    private String[] bobNpcQuestPostText = {
            "Oh it's you again!",
            "Yes, it's me is there anything else I can do?",
            "No,but perhaps Brutus has something else for you.",
            "Thanks."
    };




    private boolean bobNpcQuestUnlocked = false;
    private  boolean bobNpcQuestStart = false;
    private boolean bobNpcQuestInProg = false;
    private  boolean bobNpcQuestCompleted = false;
    private boolean bobNpcQuestPost = false;


    public BobNpc(double x, double y, int width, int height, ImageRef imageref) {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;
        bobNpcQuestText = bobNpcQuestStartText;

    }



    // obtain text
    public String[] getBobNpcQuestText() { return bobNpcQuestText; }
    public String getBobNpcText(int index) { return bobNpcQuestText[index]; }
    public void setBobNpcQuestText(String[] questText) { bobNpcQuestText = questText; }
    public String[] getBobNpcQuestWaiting() { return bobNpcQuestWaiting; }
    public String[] getBobNpcQuestStartText() { return bobNpcQuestStartText; }
    public String[] getBobNpcQuestInProgText() { return bobNpcQuestInProgText; }
    public String[] getBobNpcQuestCompleteText() { return bobNpcQuestCompleteText; }
    public String[] getGetBobNpcQuestPostText() { return bobNpcQuestPostText; }

    // track quest state
    public boolean isBobNpcQuestUnlocked() { return bobNpcQuestUnlocked; }
    public void setBobNpcQuestUnlocked(boolean bobNpcQuestUnlocked) { this.bobNpcQuestUnlocked = bobNpcQuestUnlocked; }
    public boolean isBobNpcQuestStart() { return bobNpcQuestStart; }
    public void setBobNpcQuestStart(boolean BobNpcQuestStart) { this.bobNpcQuestStart = BobNpcQuestStart; }

    public boolean isBobNpcQuestInProg() { return bobNpcQuestInProg; }
    public void setBobNpcQuestInProg(boolean BobNpcQuestInProg) { this.bobNpcQuestInProg = BobNpcQuestInProg; }

    public boolean isBobNpcQuestCompleted() { return bobNpcQuestCompleted; }
    public void setBobNpcQuestCompleted(boolean BobNpcQuestCompleted) { this.bobNpcQuestCompleted = BobNpcQuestCompleted; }

    public boolean isBobNpcQuestPost() { return bobNpcQuestPost; }
    public void setBobNpcQuestPost(boolean BobNpcQuestPost) { this.bobNpcQuestPost = BobNpcQuestPost; }

    public int type() { return Const.TYPE_BOB; }

}