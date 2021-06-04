
// (c) Thorsten Hasbargen


package projectzelda.game;

import projectzelda.engine.*;
import java.awt.Color;

class HelpText extends TextObject
{
  public HelpText(int x, int y)
  { super(x,y, new Color(0,120,255,60));
  }
  
  public String toString()
  { String display = "MOVE:WASD      SHOOT:Mouse left      "+
                     "Grenade:Space bar     END: q";
    return display;
  }
  
}
