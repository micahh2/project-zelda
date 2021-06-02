
// (c) Thorsten Hasbargen

package projectzelda.gfx;

import projectzelda.*;
import projectzelda.engine.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

public class SwingFrame extends JFrame implements Frame
{
  // ...ok...
  private static final long serialVersionUID = 2L;

  private SwingPanel panel = null;

  public SwingFrame(MediaInfo mediaInfo, WorldInfo worldInfo) throws UnsupportedAudioFileException, LineUnavailableException, IOException
  { 
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.setSize(Const.WORLDPART_WIDTH+2, Const.WORLDPART_HEIGHT+2);

      this.setAlwaysOnTop(true);
      this.setUndecorated(true);

      this.setResizable(false);

      panel = new SwingPanel(mediaInfo, worldInfo);

      // needed for Keyboard input !!!
      panel.setFocusable(true);
      panel.requestFocusInWindow();

      this.setContentPane(panel);
  }

  public void displayOnScreen() { validate(); setVisible(true); }
  
  public GraphicSystem getGraphicSystem() { return panel; }
  public InputSystem   getInputSystem()   { return panel.getInputSystem(); }
}
