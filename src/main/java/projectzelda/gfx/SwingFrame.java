
// (c) Thorsten Hasbargen

package projectzelda.gfx;

import projectzelda.*;
import projectzelda.engine.*;
import javax.swing.*;

public class SwingFrame extends JFrame implements Frame
{
  // ...ok...
  private static final long serialVersionUID = 2L;

  private SwingPanel panel = null;

  public SwingFrame()
  { 
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.setSize(Const.WORLDPART_WIDTH+2, Const.WORLDPART_HEIGHT+2);

      this.setAlwaysOnTop(true);
      this.setUndecorated(true);

      this.setResizable(false);

      panel = new SwingPanel();

      // needed for Keyboard input !!!
      panel.setFocusable(true);
      panel.requestFocusInWindow();

      this.setContentPane(panel);
  }

  public void displayOnScreen() { validate(); setVisible(true); }
  
  public GraphicSystem getGraphicSystem() { return panel; }
  public InputSystem   getInputSystem()   { return panel.getInputSystem(); }
}