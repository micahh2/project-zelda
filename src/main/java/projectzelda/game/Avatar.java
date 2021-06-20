
// (c) Thorsten Hasbargen

package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import projectzelda.map.MapObject;

import java.awt.Color;
import java.util.Arrays;

public class Avatar extends CircularGameObject
{
    private final double COOLDOWN = 0.5;
    private double weaponTemp = 0;

    private boolean flippedX = false;
    private ImageRef sword;


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
                    chatBoxText = pumpkin.chestTexts[0];
                    ((RPGWorld)world).addChatBox(chatBoxText, pumpkin);
                    obj.isLiving = false;
                    break;

                case Const.TYPE_GOBLIN:
                    this.moveBack(); 
                    if (weaponTemp <= 0) {
                        ((EnemyAI)obj).hit();
                        weaponTemp = COOLDOWN;
                    }
                    break;

                 case Const.TYPE_NPC:
                    this.moveBack();
                    if (!talkedToNPC) {
                        world.gameState = GameState.DIALOG;
                        chatBoxText = ((RPGWorld)world).npcDialog[0];
                        chatBox = new ChatBoxButton( posXChatBox, posYChatBox, 600, 100, chatBoxText, Const.TYPE_NPC);
                        world.chatBoxObjects.add(chatBox);

                        talkedToNPC = true;

                    } else {

                    //  world.gameState = GameState.DIALOG;
                    //  chatBox = new ChatBoxButton( posXChatBox, posYChatBox, 600, 100, "You again?", Const.TYPE_NPC);
                    //  world.chatBoxObjects.add(chatBox);
                    }

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
        if ((startx < endx && !flippedX) || (startx > endx && flippedX)) {
            int tempx = imageRef.x1;
            imageRef.x1 = imageRef.x2;
            imageRef.x2 = tempx;
            flippedX = !flippedX;
        }


    }

    @Override
    public void draw(GraphicSystem gs) {
        int swordx = (int)Math.round(flippedX ? x : (x-radius)); //-radius*1.5;
        int swordy = (int)Math.round(y - radius * 1.2);
        int width = (int)Math.round((sword.x2 - sword.x1)*0.8);
        int height = (int)Math.round((sword.y2 - sword.y1)*0.8);

        gs.drawImage(sword, swordx, swordy, swordx+width, swordy+height);
        gs.draw(this);
    }



    public int type() { return Const.TYPE_AVATAR; }
}
