
// (c) Thorsten Hasbargen

package projectzelda.engine;

import java.util.List;
import java.util.LinkedList;

public final class UserInput 
{
  // everything a user can press on keyboard or mouse	
  public int mousePressedX;
  public int mousePressedY;
  public int mouseMovedX;
  public int mouseMovedY;
  public int mouseButton;
  
  public char keyPressed;
  public List<Character> keysPressed;
  
  // if Mouse was clicked, Key was pressed or Mouse is still hold down
  public boolean isMouseEvent;
  public boolean isKeyEvent;
  public boolean isMousePressed; 
  
  // ... is returned as a data set
  public UserInput() { 
      keysPressed = new LinkedList<Character>();
      this.clear();
  }
  
  public final void clear()
  { 
      isMouseEvent=false;
      isKeyEvent=false;
  }
}
