
package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;

class Bones extends CircularGameObject
{
    double life = 10;

    public Bones(double x, double y)
    {
        super(x, y, 0, 0, 10, Color.GRAY);
    }

    public void move(double diffSeconds)
    {
        life -= diffSeconds;
        if (life < 0) { isLiving=false; }
    }

    public int type() { return Const.Type.BONES.ordinal(); }
}
