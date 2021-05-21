
// (c) Thorsten Hasbargen

package projectzelda.game;

import projectzelda.engine.*;
import java.awt.Color;

class CounterGrenades extends TextObject
{
  private int number = 1;
	
  public CounterGrenades(int x, int y)
  { super(x,y, new Color(255,255,0,210));
  }
  
  public String toString()
  { String display = "Grenades: ";
    display += number;
    return display;
  }
  
  public void setNumber(int n){number=n;}
}
