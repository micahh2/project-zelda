package projectzelda.game;

import projectzelda.engine.*;
import java.awt.Color;

// not in use
class GrenadePickedUpText extends TextObject
{
    public GrenadePickedUpText(int x, int y)
    { super(x,y, new Color(255,255,0,210));
    }

    public String toString()
    { String display = "You picked up a grenade!";
        return display;
    }

}