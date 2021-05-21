
// (c) Thorsten Hasbargen

package projectzelda.gfx;

import projectzelda.engine.*;
import java.awt.event.*;
import java.util.Collections;

class AWTInputSystem 
    implements InputSystem, KeyListener, MouseListener, MouseMotionListener
{

    // UserInput variables
    private UserInput userInput = new UserInput();

    public void mousePressed(MouseEvent evt)
    {
        synchronized(userInput) {
            // an input Event occurs
            userInput.isMouseEvent      = true;
            userInput.mousePressedX     = evt.getX();
            userInput.mousePressedY     = evt.getY();
            userInput.mouseButton       = evt.getButton();
            userInput.isMousePressed    = true;
        }
    }  

    public void mouseReleased(MouseEvent evt)
    { 
        synchronized(userInput) {
            userInput.isMousePressed = false;
        }
    }


    public void mouseMoved(MouseEvent evt)
    { 
        synchronized(userInput) {
            userInput.mouseMovedX=evt.getX();
            userInput.mouseMovedY=evt.getY();
        }
    }


    public void mouseDragged(MouseEvent evt)
    { 
        synchronized(userInput) {
            userInput.mouseMovedX=evt.getX();
            userInput.mouseMovedY=evt.getY();
        }
    }


    public void keyPressed(KeyEvent evt)
    { 
        char c = evt.getKeyChar();
        synchronized(userInput) {
            userInput.isKeyEvent = true;
            userInput.keyPressed = c;
            if (!userInput.keysPressed.contains(c)) {
                userInput.keysPressed.add(c);
            }
        }
    }	


    public void mouseEntered(MouseEvent evt){}
    public void mouseExited(MouseEvent evt){}
    public void mouseClicked(MouseEvent evt){}
    public void keyReleased(KeyEvent evt){
        char c = evt.getKeyChar();
        synchronized(userInput) {
            userInput.keysPressed.removeAll(Collections.singleton(c));
        }
    }
    public void keyTyped(KeyEvent evt){}	


    public UserInput getUserInput()
    { 
        return userInput;
    }  
}
