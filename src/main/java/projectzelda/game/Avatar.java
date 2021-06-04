
// (c) Thorsten Hasbargen

package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;
import java.util.concurrent.TimeUnit;

class Avatar extends GameObject
{
    private final double COOLDOWN = 0.5;
    private double weaponTemp = 0;
    private double lifeBPickedUpText;
    private double lifeGPickedUpText;
    private bonesPickedUpText bPickedUpText;
    private grenadePickedUpText gPickedUpText;

    public Avatar(double x, double y) 
    { 
        super(x,y,0,200,15, new Color(96,96,255));


        this.isMoving = false;

        imageRef = new ImageRef("/map/d84e826f657c017e95645fb800fafd6d.png", 0, 0, 10, 10);
    }

    public void move(double diffSeconds)
    {
        if (weaponTemp > 0) {
            weaponTemp -= diffSeconds;
        }
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
                    bPickedUpText =new bonesPickedUpText(750,1000);
                    world.textObjects.add(bPickedUpText);
                    lifeBPickedUpText = 2.0;
                    obj.isLiving = false;
                    break;

                // pick up Grenades
                case Const.TYPE_GRENADE:
                    ((RPGWorld)world).addGrenade();
                    gPickedUpText =new grenadePickedUpText(750,1000);
                    world.textObjects.add(gPickedUpText);
                    lifeGPickedUpText = 2.0;
                    obj.isLiving = false;
                    break;
            }
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
