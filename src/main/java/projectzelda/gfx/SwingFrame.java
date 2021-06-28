
// (c) Thorsten Hasbargen

package projectzelda.gfx;

import projectzelda.*;
import projectzelda.engine.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

public class SwingFrame extends JFrame implements Frame
{

  ImageIcon icon;
  // ...ok...
  private static final long serialVersionUID = 2L;

  private SwingPanel panel = null;

  public SwingFrame(MediaInfo mediaInfo, WorldInfo worldInfo, ScreenInfo screenInfo)
  { 
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.setSize(screenInfo.getWidth()+2, screenInfo.getHeight()+2);
      this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

      this.setAlwaysOnTop(true);
      this.setUndecorated(true);
      this.getContentPane().setBackground(Color.BLACK);

      this.setResizable(false);

      panel = new SwingPanel(mediaInfo, worldInfo);
      panel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
      panel.setPreferredSize(new Dimension(worldInfo.getPartWidth(), worldInfo.getPartHeight()));
      panel.setMaximumSize(new Dimension(worldInfo.getPartWidth(), worldInfo.getPartHeight()));

      // needed for Keyboard input !!!
      panel.setFocusable(true);
      panel.requestFocusInWindow();


      //this.setContentPane(panel);
      this.getContentPane().add(panel);
      this.setTitle("The Legend of Adlez");

      icon = new ImageIcon("src/main/resources/images/icon.png");
      setIconImage(icon.getImage());
  }

  public void displayOnScreen() { validate(); setVisible(true); }
  
  public GraphicSystem getGraphicSystem() { return panel; }
  public InputSystem   getInputSystem()   { return panel.getInputSystem(); }
}
