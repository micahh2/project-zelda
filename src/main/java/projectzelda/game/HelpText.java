
// (c) Thorsten Hasbargen


package projectzelda.game;

import projectzelda.engine.*;
import java.awt.Color;

class HelpText extends TextObject
{
  public HelpText(int x, int y)
  { super(x,y, new Color(255,255,255,255));
  }
  
  public String toString()
  { String display = "Move: WASD      Shoot: Mouse Left      "+
                     "Grenade: Space Bar    Pause: Esc   End: q";
    return display;
  }
  
}
