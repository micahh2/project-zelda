package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.ImageRef;
import projectzelda.engine.RectangularGameObject;

public class SteveNpc extends RectangularGameObject {



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




    private boolean steveNpcQuestUnlocked = false;
    private  boolean steveNpcQuestStart = false;
    private boolean steveNpcQuestInProg = false;
    private  boolean steveNpcQuestCompleted = false;
    private boolean steveNpcQuestPost = false;


    public SteveNpc(double x, double y, int width, int height, ImageRef imageref) {
        super(x, y, 0,0, width, height, null);
        this.imageRef = imageref;
        this.isMoving = false;
        steveNpcQuestText = steveNpcQuestStartText;

    }



    // obtain text
    public String[] getSteveNpcQuestText() { return steveNpcQuestText; }
    public String getSteveNpcText(int index) { return steveNpcQuestText[index]; }
    public void setSteveNpcQuestText(String[] questText) { steveNpcQuestText = questText; }
    public String[] getSteveNpcQuestWaiting() { return steveNpcQuestWaiting; }
    public String[] getSteveNpcQuestStartText() { return steveNpcQuestStartText; }
    public String[] getSteveNpcQuestInProgText() { return steveNpcQuestInProgText; }
    public String[] getSteveNpcQuestCompleteText() { return steveNpcQuestCompleteText; }
    public String[] getGetSteveNpcQuestPostText() { return steveNpcQuestPostText; }

    // track quest state

    public boolean isSteveNpcQuestUnlocked() { return steveNpcQuestUnlocked; }
    public void setSteveNpcQuestUnlocked(boolean steveNpcQuestUnlocked) { this.steveNpcQuestUnlocked = steveNpcQuestUnlocked; }

    public boolean isSteveNpcQuestStart() { return steveNpcQuestStart; }
    public void setSteveNpcQuestStart(boolean steveNpcQuestStart) { this.steveNpcQuestStart = steveNpcQuestStart; }

    public boolean isSteveNpcQuestInProg() { return steveNpcQuestInProg; }
    public void setSteveNpcQuestInProg(boolean steveNpcQuestInProg) { this.steveNpcQuestInProg = steveNpcQuestInProg; }

    public boolean isSteveNpcQuestCompleted() { return steveNpcQuestCompleted; }
    public void setSteveNpcQuestCompleted(boolean steveNpcQuestCompleted) { this.steveNpcQuestCompleted = steveNpcQuestCompleted; }

    public boolean isSteveNpcQuestPost() { return steveNpcQuestPost; }
    public void setSteveNpcQuestPost(boolean steveNpcQuestPost) { this.steveNpcQuestPost = steveNpcQuestPost; }

    public int type() { return Const.TYPE_STEVE; }

}