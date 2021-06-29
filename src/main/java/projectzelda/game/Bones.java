
package projectzelda.game;

import projectzelda.engine.*;
import java.awt.Color;

class Bones extends CircularGameObject
{
    public double life = 3;

    public Bones(double x, double y,  ImageRef bonesImage)
    {

        super(x, y, 0, 0, 10, Color.GRAY);
        this.imageRef = bonesImage;
    }

    public void move(double diffSeconds)
    {
        life -= diffSeconds;
        if (life < 0) { 
            isLiving=false;
            ((RPGWorld)world).addMonster(x, y);
        }
    }

    public int type() { return Const.Type.BONES.ordinal(); }
    
    public GameObject clone() {
        Bones b = new Bones(x, y, imageRef);
        b.life = life;
        return b;
    }
}
