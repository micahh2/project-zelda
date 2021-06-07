
// (c) Thorsten Hasbargen

package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;

public class Avatar extends GameObject
{
    private final double COOLDOWN = 0.5;
    private double weaponTemp = 0;
    private double lifeBPickedUpText;
    private double lifeGPickedUpText;
    private BonesPickedUpText bPickedUpText;
    private GrenadePickedUpText gPickedUpText;
    private boolean flippedX = false;

    public double life = 1.0;
    public HealthBar healthBar;

    public Avatar(double x, double y, ImageRef imageRef) 
    { 
        super(x,y,0,200,15, new Color(96,96,255));

        this.isMoving = false;

        imageRef = new ImageRef("Rocks2", 0, 0, 32, 32);

        healthBar = new HealthBar(10, 10, 300, 25);
        healthBar.isHudElement = true;
        this.imageRef = imageRef;
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
                
                case Const.TYPE_GOBLIN:
                    this.moveBack(); 
                    if (weaponTemp <= 0) {
                        ((GoblinAI)obj).hit();
                        weaponTemp = COOLDOWN;
                    }
                    break;

                // pick up Bones
                case Const.TYPE_BONES:
                    ((RPGWorld)world).addBones();
                    bPickedUpText =new BonesPickedUpText(750,1000);
                    world.textObjects.add(bPickedUpText);
                    lifeBPickedUpText = 2.0;
                    obj.isLiving = false;
                    break;

                // pick up Grenades
                case Const.TYPE_GRENADE:
                    ((RPGWorld)world).addGrenade();
                    gPickedUpText =new GrenadePickedUpText(750,1000);
                    world.textObjects.add(gPickedUpText);
                    lifeGPickedUpText = 2.0;
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

        if (bPickedUpText != null) {
            lifeBPickedUpText -= diffSeconds;
            if (lifeBPickedUpText < 0) {
                world.textObjects.remove(bPickedUpText);
                bPickedUpText = null;
            }
        }

        if (gPickedUpText != null) {
            lifeGPickedUpText -= diffSeconds;
            if (lifeGPickedUpText < 0) {
                world.textObjects.remove(gPickedUpText);
                gPickedUpText = null;
            }
        }

    }


    public int type() { return Const.TYPE_AVATAR; }
}
