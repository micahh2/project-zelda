
// (c) Thorsten Hasbargen

package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import projectzelda.map.MapObject;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Avatar extends CircularGameObject {
    private final double COOLDOWN = 0.5;
    private double weaponTemp = 0;

    private boolean flippedX = false;
    private HashMap<String, GameObject> inventory;
    private ImageRef sword;
    boolean hasSword = false;

    private SwordSwing swordSwing;

    private ChatBoxButton chatBox;
    private String chatBoxText;

    // place of chatbox
    private int posXChatBox = world.worldInfo.getPartWidth() / 2 - 300;
    private int posYChatBox = world.worldInfo.getPartHeight() - 100;

    private boolean talkedToNPC = false;

    private SteveNpc steveNpc;
    private Pumpkin pumpkin;
    private Chest chest;
    private BrutusNpc brutusNpc;
    private OlgaNpc olgaNpc;
    private BobNpc bobNpc;
    private CatNpc catNpc;
    private DogNpc dogNpc;

    private Counter counterBones;
    double timeToDie = 2.0;
    boolean dying = false;

    public double life = 1.0;
    public HealthBar healthBar;


    public Avatar(double x, double y, ImageRef imageRef, ImageRef sword) 
    { 
        super(x,y,0,200,15, new Color(96,96,255));

        this.isMoving = false;

        inventory = new HashMap<>();

        //imageRef = new ImageRef("Rocks2", 0, 0, 32, 32);

        int healthBarWidth = (int) (0.3 * world.worldInfo.getPartWidth());
        int healthBarHeight = (int) (0.03 * world.worldInfo.getPartHeight());
        int healthBarX = (int) (0.005 * world.worldInfo.getPartWidth());
        int healthBarY = (int) (0.01 * world.worldInfo.getPartHeight());
        healthBar = new HealthBar(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
        healthBar.isHudElement = true;
        this.imageRef = imageRef;
        this.sword = sword;
    }

    @Override
    public void move(double diffSeconds)
    {
        if (dying) {
            if (timeToDie < 0) {
                isLiving = false;
                world.gameState = GameState.DEATH;
                return;
            }
            timeToDie -= diffSeconds;
            return;
        }

        if (weaponTemp > 0) {
            weaponTemp -= diffSeconds;
        }

        // Save starting x-pos for calculating orientation
        int startx = (int)x;
        int starty = (int)y;

        // move Avatar one step forward
        super.move(diffSeconds);

        // calculate all collisions with other Objects 
        GameObjectList collisions = world.getPhysicsSystem().getCollisions(this);
        for (int i = 0; i < collisions.size(); i++) {
            GameObject obj = collisions.get(i);

            pumpkin = (Pumpkin) ((RPGWorld) world).pumpkin;
            steveNpc = (SteveNpc) ((RPGWorld) world).steve;
            chest = (Chest) ((RPGWorld) world).chest;
            brutusNpc = (BrutusNpc) ((RPGWorld) world).brutus;
            olgaNpc = (OlgaNpc) ((RPGWorld) world).olga;
            bobNpc = (BobNpc) ((RPGWorld) world).bob;
            catNpc = (CatNpc) ((RPGWorld) world).cat;
            dogNpc = (DogNpc) ((RPGWorld) world).dog;
            counterBones = ((RPGWorld) world).getCounterB();


            switch (obj.type()) {
                // if Object is a tree, move back one step
                case Const.TYPE_TREE:
                    this.moveBack();
                    break;

                case Const.TYPE_CHEST:
                    questChest();
                    break;

                case Const.TYPE_PUMPKIN:
                    questPumpkin();
                    break;

                case Const.TYPE_BRUTUS:
                    questBrutus();
                    break;

                case Const.TYPE_STEVE:
                    questSteve();
                    break;

                case Const.TYPE_OLGA:
                    questOlga();
                    break;

                case Const.TYPE_DOG:
                   questDog();
                    break;

                case Const.TYPE_CAT:
                    questCat();
                    break;


                case Const.TYPE_BOB:
                    questBob();
                    break;


                case Const.TYPE_GOBLIN:
                    this.moveBack();
                    // remove this if statement to make monsters attackable
                   if (olgaNpc.isOlgaNpcMonsterQuestStart()){
                        if (weaponTemp <= 0) {
                            ((EnemyAI) obj).hit();
                            weaponTemp = COOLDOWN;
                        }
                    } else {
                       world.gameState = GameState.DIALOG;
                        chatBox = new ChatBoxButton(posXChatBox, posYChatBox, 600, 100, "I'm going to need a weapon", Const.TYPE_GOBLIN);
                        world.chatBoxObjects.add(chatBox);
                    }

                    break;

                // pick up Bones
                case Const.TYPE_BONES:
                    ((RPGWorld) world).addBones();
                    if (counterBones.getNumber() >= 6) {
                        olgaNpc.setOlgaNpcMonsterQuestCompleted(true);
                    }
                    chatBox = new ChatBoxButton(posXChatBox, posYChatBox, 600, 100, "Bones picked up", Const.TYPE_BONES);
                // pick up Grenades
                case Const.TYPE_GRENADE:
                    ((RPGWorld) world).addGrenade();
                    chatBox = new ChatBoxButton(posXChatBox, posYChatBox, 600, 100, "Grenade picked up", Const.TYPE_GRENADE);
                    world.chatBoxObjects.add(chatBox);
                    obj.isLiving = false;
                    break;

            }
        }

        // Hacky, but we can flip the orientation of the avatar by switching the image coordinates to draw from
        int endx = (int)x;
        int endy = (int)y;
      
        if ((startx < endx && !flippedX) || (startx > endx && flippedX)) {
            int tempx = imageRef.x1;
            imageRef.x1 = imageRef.x2;
            imageRef.x2 = tempx;
            flippedX = !flippedX;
        }

        if (swordSwing != null && (startx != endx || starty != endy)) {
            swordSwing.offset(endx-startx, endy-starty);
        }
    }

    public boolean containsItem(String itemType){
        return inventory.containsKey(itemType);
    }

    public void removeItem(String itemType){
        if(inventory.containsKey(itemType)){
            inventory.remove(itemType);
        }
    }

    public void addItem(String itemType, GameObject item){
        inventory.put(itemType, item);
    }
       

    public void questDog() {
        this.moveBack();
        world.gameState = GameState.DIALOG;
        if (bobNpc.isBobNpcQuestInProg()) {
            dogNpc.setDogNpcText(dogNpc.getDogNpcQuestText());
            chatBoxText = dogNpc.getDogNpcText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, dogNpc);
            dogNpc.isLiving = false;
            if (!dogNpc.isLiving && !catNpc.isLiving) {
                bobNpc.setBobNpcQuestCompleted(true);
            }


        } else {
            chatBoxText = dogNpc.getDogNpcText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, dogNpc);
        }
    }
    public void questCat() {
        this.moveBack();
        world.gameState = GameState.DIALOG;
        if (bobNpc.isBobNpcQuestInProg()) {
            catNpc.setCatNpcText(catNpc.getCatNpcQuestText());
            chatBoxText = catNpc.getCatNpcText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, catNpc);
            catNpc.isLiving = false;
            if (!dogNpc.isLiving && !catNpc.isLiving) {
                bobNpc.setBobNpcQuestCompleted(true);
            }

        } else {
            chatBoxText = catNpc.getCatNpcText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, catNpc);
        }
    }
    public void questPumpkin() {
        this.moveBack();
        world.gameState = GameState.DIALOG;
        if (steveNpc.isSteveNpcQuestInProg()) {
            pumpkin.setPumpkinText(pumpkin.getPumpkinQuestText());
            chatBoxText = pumpkin.getPumpkinText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, pumpkin);
            pumpkin.isLiving = false;
            steveNpc.setSteveNpcQuestCompleted(true);

        } else {
            chatBoxText = pumpkin.getPumpkinText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, pumpkin);
        }
    }

    public void questChest() {
        this.moveBack();
        world.gameState = GameState.DIALOG;
        if (olgaNpc.isOlgaNpcChestQuestStart()) {
            chest.setChestText(chest.getChestQuestText());
            chatBoxText = chest.getChestText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, chest);
            chest.isLiving = false;
            olgaNpc.setOlgaNpcChestQuestCompleted(true);
        } else {
            chatBoxText = chest.getChestText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, chest);
        }
    }

    public void questBrutus() {
        this.moveBack();
        world.gameState = GameState.DIALOG;
        // first meeting with brutus
        if (!brutusNpc.isBrutusNpcQuestStart()) {
            chatBoxText = brutusNpc.getBrutusNpcQuestText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, brutusNpc);
            brutusNpc.setBrutusNpcQuestStart(true);
            brutusNpc.setBrutusNpcQuestInProg(true);
           // olga quest not complete
        } else if (brutusNpc.isBrutusNpcQuestInProg() && !olgaNpc.isOlgaNpcQuestCompleted()) {
            brutusNpc.setBrutusNpcQuestText(brutusNpc.getBrutusNpcQuestOlgaInProgText());
            chatBoxText = brutusNpc.getBrutusNpcQuestText(0);
            olgaNpc.setOlgaNpcQuestCompleted(true); // temp to skip parts
            ((RPGWorld) world).addChatBox(chatBoxText, brutusNpc);

            // unlock steves quest
        } else if (brutusNpc.isBrutusNpcQuestInProg()  && !steveNpc.isSteveNpcQuestUnlocked()) {
            brutusNpc.setBrutusNpcQuestText(brutusNpc.getBrutusNpcQuestOlgaCompleteText());
            chatBoxText = brutusNpc.getBrutusNpcQuestText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, brutusNpc);
            steveNpc.setSteveNpcQuestUnlocked(true);

            // steve quest incomplete
        } else if (brutusNpc.isBrutusNpcQuestInProg() && !steveNpc.isSteveNpcQuestStart() && !steveNpc.isSteveNpcQuestPost()) {
            brutusNpc.setBrutusNpcQuestText(brutusNpc.getBrutusNpcQuestSteveInProgText());
            chatBoxText = brutusNpc.getBrutusNpcQuestText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, brutusNpc);
            steveNpc.setSteveNpcQuestPost(true);// temp to skip parts


            //steve quest done && unlock bob quest
        } else if (brutusNpc.isBrutusNpcQuestInProg() && steveNpc.isSteveNpcQuestPost() && !bobNpc.isBobNpcQuestUnlocked()) {
            brutusNpc.setBrutusNpcQuestText(brutusNpc.getBrutusNpcQuestSteveCompleteText());
            chatBoxText = brutusNpc.getBrutusNpcQuestText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, brutusNpc);
            bobNpc.setBobNpcQuestUnlocked(true);

            //bob quest in progress
        } else if (brutusNpc.isBrutusNpcQuestInProg() && !bobNpc.isBobNpcQuestPost()) {
            brutusNpc.setBrutusNpcQuestText(brutusNpc.getBrutusNpcQuestBobInProgText());
            chatBoxText = brutusNpc.getBrutusNpcQuestText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, brutusNpc);
            bobNpc.setBobNpcQuestPost(true); // temp to skip parts

    // bob quest completed & final part( boss fight)
        } else if (brutusNpc.isBrutusNpcQuestInProg() && bobNpc.isBobNpcQuestPost() && !brutusNpc.isBrutusNpcQuestFinalBoss()) {
            brutusNpc.setBrutusNpcQuestText(brutusNpc.getBrutusNpcQuestBobCompleted());
            chatBoxText = brutusNpc.getBrutusNpcQuestText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, brutusNpc);
            brutusNpc.setBrutusNpcQuestFinalBoss(true);

            // brutus asks you to kill the boss and upon coming back game ends
        } else if (brutusNpc.isBrutusNpcQuestInProg() && brutusNpc.isBrutusNpcQuestFinalBoss()) {
            brutusNpc.setBrutusNpcQuestText(brutusNpc.getBrutusNpcQuestCompleteText()); // random string atm
            chatBoxText= brutusNpc.getBrutusNpcQuestText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, brutusNpc);
        }


    }
    public void questBob() {
        this.moveBack();
        world.gameState = GameState.DIALOG;
        // if steve quest complete bob is unlocked
        if (steveNpc.isSteveNpcQuestPost()) {
            if (!bobNpc.isBobNpcQuestStart()) {
                bobNpc.setBobNpcQuestText(bobNpc.getBobNpcQuestStartText());
                chatBoxText = bobNpc.getBobNpcText(0);
                ((RPGWorld)world).addChatBox(chatBoxText, bobNpc);
                bobNpc.setBobNpcQuestStart(true);
                bobNpc.setBobNpcQuestInProg(true);
                // talked to but not pets found
            } else if (bobNpc.isBobNpcQuestInProg() && !bobNpc.isBobNpcQuestCompleted()) {
                bobNpc.setBobNpcQuestText(bobNpc.getBobNpcQuestInProgText());
                chatBoxText = bobNpc.getBobNpcText(0);
                ((RPGWorld)world).addChatBox(chatBoxText, bobNpc);

                // pets found
            } else if (bobNpc.isBobNpcQuestCompleted() && bobNpc.isBobNpcQuestInProg()) {
                bobNpc.setBobNpcQuestText(bobNpc.getBobNpcQuestCompleteText());
                chatBoxText = bobNpc.getBobNpcText(0);
                ((RPGWorld)world).addChatBox(chatBoxText, bobNpc);
                bobNpc.setBobNpcQuestPost(true);
                bobNpc.setBobNpcQuestInProg(false);

                // dialog that happens after quest done
            } else if (bobNpc.isBobNpcQuestPost() && !bobNpc.isBobNpcQuestInProg()){
                bobNpc.setBobNpcQuestText(bobNpc.getGetBobNpcQuestPostText());
                chatBoxText = bobNpc.getBobNpcText(0);
                ((RPGWorld)world).addChatBox(chatBoxText, bobNpc);
            }

        } else if (!bobNpc.isBobNpcQuestPost()) {
            bobNpc.setBobNpcQuestText(bobNpc.getBobNpcQuestWaiting());
            chatBoxText = bobNpc.getBobNpcText(0);
            ((RPGWorld)world).addChatBox(chatBoxText,bobNpc);
        }
    }

    public void questOlga() {
        this.moveBack();
        world.gameState = GameState.DIALOG;
        if (brutusNpc.isBrutusNpcQuestStart()) {
        // initial convo
         if (!olgaNpc.isOlgaNpcChestQuestStart()) {
            olgaNpc.setOlgaNpcQuestText(olgaNpc.getOlgaNpcQuestStartText());
            chatBoxText = olgaNpc.getOlgaNpcText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, olgaNpc);
            olgaNpc.setOlgaNpcChestQuestStart(true);
            olgaNpc.setOlgaNpcChestQuestInProg(true);
            // if keys not found and talked to again
         } else if (olgaNpc.isOlgaNpcChestQuestInProg() && !olgaNpc.isOlgaNpcChestQuestCompleted()) {
            olgaNpc.setOlgaNpcQuestText(olgaNpc.getOlgaNpcChestQuestInProgText());
            chatBoxText = olgaNpc.getOlgaNpcText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, olgaNpc);
            // keys found and talked to
         } else if (olgaNpc.isOlgaNpcChestQuestCompleted() && !olgaNpc.isOlgaNpcMonsterQuestStart()) {
            olgaNpc.setOlgaNpcQuestText(olgaNpc.getOlgaNpcChestQuestCompleteText());
            chatBoxText = olgaNpc.getOlgaNpcText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, olgaNpc);
            olgaNpc.setOlgaNpcMonsterQuestStart(true);

            // monster quest started but no monsters killed
         } else if (olgaNpc.isOlgaNpcMonsterQuestStart() && !olgaNpc.isOlgaNpcMonsterQuestCompleted()) {
            olgaNpc.setOlgaNpcQuestText(olgaNpc.getOlgaNpcMonsterQuestStartText());
            chatBoxText = olgaNpc.getOlgaNpcText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, olgaNpc);
            // monsters killed
         } else if (olgaNpc.isOlgaNpcMonsterQuestCompleted() && !olgaNpc.isOlgaNpcQuestCompleted()) {
            olgaNpc.setOlgaNpcQuestText(olgaNpc.getOlgaNpcMonsterQuestCompletedText());
            chatBoxText = olgaNpc.getOlgaNpcText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, olgaNpc);
            olgaNpc.setOlgaNpcQuestCompleted(true);
         } else if (olgaNpc.isOlgaNpcQuestCompleted()) {
             olgaNpc.setOlgaNpcQuestText(olgaNpc.getOlgaNpcPostQuestText());
             chatBoxText = olgaNpc.getOlgaNpcText(0);
             ((RPGWorld) world).addChatBox(chatBoxText, olgaNpc);
         }

       }
       else if(!brutusNpc.isBrutusNpcQuestStart())

        {
        olgaNpc.setOlgaNpcQuestText(olgaNpc.getOlgaNpcQuestWaiting());
        chatBoxText = olgaNpc.getOlgaNpcText(0);
        ((RPGWorld) world).addChatBox(chatBoxText, olgaNpc);
       }

    }
    public void questSteve() {
        this.moveBack();
        world.gameState = GameState.DIALOG;
        // if olga quest complete steve is unlocked
        if (olgaNpc.isOlgaNpcQuestCompleted()) {
            if (!steveNpc.isSteveNpcQuestStart()) {
                steveNpc.setSteveNpcQuestText(steveNpc.getSteveNpcQuestStartText());
                chatBoxText = steveNpc.getSteveNpcText(0);
                ((RPGWorld)world).addChatBox(chatBoxText, steveNpc);
                steveNpc.setSteveNpcQuestStart(true)steveNpc.setSteveNpcQuestInProg(true);
            // talked to but not pumpkin found
            } else if (steveNpc.isSteveNpcQuestInProg() && !steveNpc.isSteveNpcQuestCompleted()) {
              .setSteveNpcQuestText(steveNpc.getSteveNpcQuestInProgText());
                chatBoxText = steveNpc.getSteveNpcText(0);
                ((RPGWorld)world).addChatBox(chatBoxText, steveNpc);

            // pumpkin eaten
            } else if (steveNpc.isSteveNpcQuestCompleted() && steveNpc.isSteveNpcQuestInProg()) {
                steveNpc.setSteveNpcQuestText(steveNpc.getSteveNpcQuestCompleteText());
                chatBoxText = steveNpc.getSteveNpcText(0);
                ((RPGWorld)world).addChatBox(chatBoxText, steveNpc);
                steveNpc.setSteveNpcQuestPost(true);
                steveNpc.setSteveNpcQuestInProg(false);

                // dialog that happens after quest done
            } else if (steveNpc.isSteveNpcQuestPost() && !steveNpc.isSteveNpcQuestInProg()){
                steveNpc.setSteveNpcQuestText(steveNpc.getGetSteveNpcQuestPostText());
                chatBoxText = steveNpc.getSteveNpcText(0);
                ((RPGWorld)world).addChatBox(chatBoxText, steveNpc);
            }

        } else if (!olgaNpc.isOlgaNpcQuestCompleted()) {
            steveNpc.setSteveNpcQuestText(steveNpc.getSteveNpcQuestWaiting());
            chatBoxText = steveNpc.getSteveNpcText(0);
            ((RPGWorld)world).addChatBox(chatBoxText,steveNpc);
        }
    }

    public void draw(GraphicSystem gs, long tick) {
        int swordx = (int)Math.round(flippedX ? x : (x-radius)); //-radius*1.5;
        int swordy = (int)Math.round(y - radius * 1.2);
        int width = (int)Math.round((sword.x2 - sword.x1)*0.8);
        int height = (int)Math.round((sword.y2 - sword.y1)*0.8);

        if (hasSword && (swordSwing == null || !swordSwing.isLiving)) {
            gs.drawImage(sword, swordx, swordy, swordx+width, swordy+height);
        }
        gs.draw(this);
    }

    public void swingSword(ImageRef imageRef) {
        if (weaponTemp > 0 || !hasSword) { return; }

        Sound sword = new Sound("/music/sword-sound-1_16bit.wav");
        sword.setVolume(-30.0f);
        sword.playSound();

        weaponTemp = COOLDOWN;
        swordSwing = new SwordSwing(x, y, imageRef, flippedX);
        world.gameObjects.add(swordSwing);
    }

    public int type() { return Const.Type.AVATAR.ordinal(); }

    public void hit(double val) {
        // every hit decreases life
        life -= 0.05;
        healthBar.health = life;

        if (life <= 0) { die(); }
    }
    public void hit() {
        hit(0.05);
    }

    public void die() {
        dying = true;
        ((RPGWorld)world).throwGrenade(x, y);
    }
}
