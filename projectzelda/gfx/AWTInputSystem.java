
// (c) Thorsten Hasbargen

package projectzelda.gfx;

import projectzelda.engine.*;
import java.awt.event.*;

class AWTInputSystem 
  implements InputSystem, KeyListener, MouseListener, MouseMotionListener
{
	  
  // UserInput variables
  private UserInput userInput = new UserInput();
	  
  public void mousePressed(MouseEvent evt)
  {
    // an input Event occurs
    userInput.isMouseEvent      = true;
    userInput.mousePressedX     = evt.getX();
    userInput.mousePressedY     = evt.getY();
    userInput.mouseButton       = evt.getButton();
    userInput.isMousePressed    = true;
  }  
	  
  public void mouseReleased(MouseEvent evt)
  { userInput.isMousePressed = false;
  }

	  
  public void mouseMoved(MouseEvent evt)
  { userInput.mouseMovedX=evt.getX();
    userInput.mouseMovedY=evt.getY();
  }
	  
	  
  public void mouseDragged(MouseEvent evt)
  { userInput.mouseMovedX=evt.getX();
    userInput.mouseMovedY=evt.getY();
  }
	  
	  
  public void keyPressed(KeyEvent evt)
  { userInput.isKeyEvent = true;
    userInput.keyPressed = evt.getKeyChar();
  }	
	
  
  public void mouseEntered(MouseEvent evt){}
  public void mouseExited(MouseEvent evt){}
  public void mouseClicked(MouseEvent evt){}
  public void keyReleased(KeyEvent evt){}
  public void keyTyped(KeyEvent evt){}	
  
  
  public UserInput getUserInput()
  { 
      return userInput;
  }  
}
