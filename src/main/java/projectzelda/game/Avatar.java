
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

public class Avatar extends CircularGameObject
{
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
    private int posXChatBox = world.worldInfo.getPartWidth()/2-300;
    private int posYChatBox = world.worldInfo.getPartHeight()-100;

    private boolean talkedToNPC = false;
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

        int healthBarWidth = (int)(0.3 * world.worldInfo.getPartWidth());
        int healthBarHeight = (int)(0.03 * world.worldInfo.getPartHeight());
        int healthBarX = (int)(0.005 * world.worldInfo.getPartWidth());
        int healthBarY = (int)(0.01 * world.worldInfo.getPartHeight());
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
        for(int i = 0; i < collisions.size(); i++) {
            GameObject obj = collisions.get(i);
            Const.Type type = Const.Type.values()[obj.type()];
            switch (type) {
                // if Object is a tree, move back one step
                case TREE:
                case WATER:
                    this.moveBack();
                    break;

                case LAVA:
                    hit(0.25);
                    break;

                case CHEST:
                    this.moveBack();
                    world.gameState = GameState.DIALOG;
                    Chest chest = (Chest)obj;
                    chatBoxText= chest.chestTexts[0];
                    ((RPGWorld)world).addChatBox(chatBoxText, chest);
                    obj.isLiving = false;
                    hasSword = true;
                    addItem("SWORD", obj);
                    break;

                case PUMPKIN:
                    this.moveBack();
                    world.gameState = GameState.DIALOG;
                    Pumpkin pumpkin = (Pumpkin)obj;
                    chatBoxText = pumpkin.pumpkinTexts[0];
                    ((RPGWorld)world).addChatBox(chatBoxText, pumpkin);
                    obj.isLiving = false;
                    break;

                case NPC:
                    this.moveBack();
                    world.gameState = GameState.DIALOG;
                    NPC npc = (NPC)obj;
                    chatBoxText = npc.npcTexts[0];
                    ((RPGWorld)world).addChatBox(chatBoxText, npc);
                    break;

                case GOBLIN:
                    this.moveBack(); 
                    break;

                // pick up Bones
                case BONES:
                    ((RPGWorld)world).addBones();
                    world.gameState = GameState.DIALOG;
                    chatBox = new ChatBoxButton(posXChatBox, posYChatBox, 600, 100, "Bones picked up", Const.Type.BONES);
                    world.chatBoxObjects.add(chatBox);
                    obj.isLiving = false;
                    break;

                // pick up Grenades
                case GRENADE:
                    ((RPGWorld)world).addGrenade();
                    world.gameState = GameState.DIALOG;
                    chatBox = new ChatBoxButton(posXChatBox, posYChatBox, 600, 100, "Grenade picked up", Const.Type.GRENADE);
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
        
    @Override
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
