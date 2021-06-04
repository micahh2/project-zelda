package projectzelda.game;

import projectzelda.engine.*;
import java.awt.Color;

class BonesPickedUpText extends TextObject
{
    public BonesPickedUpText(int x, int y)
    { super(x,y, new Color(255,255,0,210));
    }

    public String toString()
    { String display = "You picked up some bones!";
        return display;
    }

}