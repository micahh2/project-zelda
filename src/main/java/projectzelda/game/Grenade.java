
// (c) Thorsten Hasbargen


package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;

class Grenade extends CircularGameObject
{
    double life = Const.LIFE_GRENADE;

    public Grenade(double x, double y)
    {
        super(x,y,0,0,15,Color.ORANGE);
    }

    public void move(double diffSeconds)
    {
        life -= diffSeconds;
        if(life<0)
        { 
            this.isLiving=false;
            return;
        }

    }

    public int type() { return Const.TYPE_GRENADE; }
}
