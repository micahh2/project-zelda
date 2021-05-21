
// (c) Thorsten Hasbargen

package projectzelda.engine;

public final class UserInput 
{
  // everything a user can press on keyboard or mouse	
  public int mousePressedX, mousePressedY, 
      mouseMovedX,   mouseMovedY, mouseButton;
  
  public char keyPressed;
  
  // if Mouse was clicked, Key was pressed or Mouse is still hold down
  public boolean isMouseEvent, isKeyEvent, isMousePressed; 
  
  // ... is returned as a data set
  public UserInput()
  { this.clear();
  }
  
  public final void clear()
  { isMouseEvent=false; isKeyEvent=false;
  }
}
