
// (c) Thorsten Hasbargen


package projectzelda.game;

import projectzelda.engine.*;
import java.awt.Color;

class Counter extends TextObject
{
  private int number = 1;
  private String text;
	
  public Counter(String text, int x, int y) {
      super(x,y, new Color(255,255,0,210));
      this.text = text;
  }

  public Counter(String text, int x, int y, Color color)
  { 
      super(x,y, color);
      this.text = text;
  }
  
  public String toString()
  { 
    return text + number;
  }
  
  public void increment(){ number++; }
  public void setNumber(int n){ number = n; }
}
