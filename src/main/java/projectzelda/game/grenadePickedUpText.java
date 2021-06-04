package projectzelda.game;

import projectzelda.engine.*;
import java.awt.Color;

class grenadePickedUpText extends TextObject
{
    public grenadePickedUpText(int x, int y)
    { super(x,y, new Color(255,255,0,210));
    }

    public String toString()
    { String display = "You picked up a grenade!";
        return display;
    }

}