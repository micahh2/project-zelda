
// (c) Thorsten Hasbargen

package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;

import java.awt.Color;

public class Avatar extends CircularGameObject
{
    private final double COOLDOWN = 0.5;
    private double weaponTemp = 0;

    private boolean flippedX = false;
    private ImageRef sword;

    private SwordSwing swordSwing;

    private ChatBoxButton chatBox;
    private String chatBoxText;

    // place of chatbox
    private int posXChatBox = world.worldInfo.getPartWidth()/2-300;
    private int posYChatBox = world.worldInfo.getPartHeight()-100;

    private boolean talkedToNPC = false;





    public double life = 1.0;
    public HealthBar healthBar;




    public Avatar(double x, double y, ImageRef imageRef, ImageRef sword) 
    { 
        super(x,y,0,200,15, new Color(96,96,255));

        this.isMoving = false;

        //imageRef = new ImageRef("Rocks2", 0, 0, 32, 32);

        int healthBarWidth = (int)(0.3 * world.worldInfo.getPartWidth());
        int healthBarHeight = (int)(0.03 * world.worldInfo.getPartHeight());
        int healthBarX = (int)(0.005 * world.worldInfo.getPartWidth());
        int healthBarY = (int)(0.01 * world.worldInfo.getPartHeight());
        healthBar = new HealthBar(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
        healthBar.isHudElement = true;
        this.imageRef = imageRef;
        this.sword = sword;
    }

    public void move(double diffSeconds)
    {
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
        for(int i = 0; i < collisions.size(); i++) {
            GameObject obj = collisions.get(i);

            switch (obj.type()) {
                // if Object is a tree, move back one step
                case Const.TYPE_TREE:
                    this.moveBack();
                    break;

                case Const.TYPE_CHEST:
                    this.moveBack();
                    world.gameState = GameState.DIALOG;
                    Chest chest = (Chest)((RPGWorld)world).chest;
                    chatBoxText= chest.chestTexts[0];
                    ((RPGWorld)world).addChatBox(chatBoxText, chest);
                    obj.isLiving = false;
                    break;

                case Const.TYPE_PUMPKIN:
                    this.moveBack();
                    world.gameState = GameState.DIALOG;
                    Pumpkin pumpkin = (Pumpkin)((RPGWorld)world).pumpkin;
                    chatBoxText = pumpkin.pumpkinTexts[0];
                    ((RPGWorld)world).addChatBox(chatBoxText, pumpkin);
                    obj.isLiving = false;
                    break;

                case Const.TYPE_STEVE:
                    this.moveBack();
                    world.gameState = GameState.DIALOG;
                    SteveNpc steveNpc = (SteveNpc) ((RPGWorld)world).steve;
                    chatBoxText = steveNpc.steveNpcTexts[0];
                    ((RPGWorld)world).addChatBox(chatBoxText, steveNpc);
                    break;

                case Const.TYPE_DOG:
                    this.moveBack();
                    world.gameState = GameState.DIALOG;
                    DogNpc dogNpc = (DogNpc)((RPGWorld)world).dog;
                    chatBoxText = dogNpc.dogNpcTexts[0];
                    ((RPGWorld)world).addChatBox(chatBoxText, dogNpc);
                    break;

                case Const.TYPE_CAT:
                    this.moveBack();
                    world.gameState = GameState.DIALOG;
                    CatNpc catNpc = (CatNpc)((RPGWorld)world).cat;
                    chatBoxText = catNpc.catNpcTexts[0];
                    ((RPGWorld)world).addChatBox(chatBoxText, catNpc);
                    break;

                case Const.TYPE_BRUTUS:
                    this.moveBack();
                    world.gameState = GameState.DIALOG;
                    BrutusNpc brutusNpc = (BrutusNpc) ((RPGWorld)world).brutus;
                    chatBoxText = brutusNpc.brutusNpcTexts[0];
                    ((RPGWorld)world).addChatBox(chatBoxText, brutusNpc);
                    break;

                case Const.TYPE_OLGA:
                    this.moveBack();
                    world.gameState = GameState.DIALOG;
                    OlgaNpc olgaNpc = (OlgaNpc) ((RPGWorld)world).olga;
                    chatBoxText = olgaNpc.olgaNpcTexts[0];
                    ((RPGWorld)world).addChatBox(chatBoxText, olgaNpc);
                    break;

                case Const.TYPE_BOB:
                    this.moveBack();
                    world.gameState = GameState.DIALOG;
                    BobNpc bobNpc = (BobNpc) ((RPGWorld)world).bob;
                    chatBoxText = bobNpc.bobNpcTexts[0];
                    ((RPGWorld)world).addChatBox(chatBoxText, bobNpc);
                    break;


                case Const.TYPE_GOBLIN:
                    this.moveBack(); 
                    break;

                // pick up Bones
                case Const.TYPE_BONES:
                    ((RPGWorld)world).addBones();
                    world.gameState = GameState.DIALOG;
                    chatBox = new ChatBoxButton( posXChatBox, posYChatBox, 600, 100, "Bones picked up", Const.TYPE_BONES);
                    world.chatBoxObjects.add(chatBox);
                    obj.isLiving = false;
                    break;

                // pick up Grenades
                case Const.TYPE_GRENADE:
                    ((RPGWorld)world).addGrenade();
                    world.gameState = GameState.DIALOG;
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

    @Override
    public void draw(GraphicSystem gs, long tick) {
        int swordx = (int)Math.round(flippedX ? x : (x-radius)); //-radius*1.5;
        int swordy = (int)Math.round(y - radius * 1.2);
        int width = (int)Math.round((sword.x2 - sword.x1)*0.8);
        int height = (int)Math.round((sword.y2 - sword.y1)*0.8);

        if (swordSwing == null || !swordSwing.isLiving) {
            gs.drawImage(sword, swordx, swordy, swordx+width, swordy+height);
        }
        gs.draw(this);
    }

    public void swingSword(ImageRef imageRef) {
        if (weaponTemp > 0) { return; }

        Sound sword = new Sound("/music/sword-sound-1_16bit.wav");
        sword.setVolume(-30.0f);
        sword.playSound();

        weaponTemp = COOLDOWN;
        swordSwing = new SwordSwing(x, y, imageRef, flippedX);
        world.gameObjects.add(swordSwing);
    }

    public int type() { return Const.TYPE_AVATAR; }

    public void hit() {
        // every hit decreases life
        life -= 0.05;
        healthBar.health = life;

        if (life <= 0) { die(); }
    }

    public void die() {
        this.isLiving = false;
        ((RPGWorld)world).throwGrenade(x, y);
    }
}
