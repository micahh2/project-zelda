
// (c) Thorsten Hasbargen

package projectzelda.gfx;

import projectzelda.*;
import projectzelda.engine.*;
import projectzelda.game.GoblinAI;
import projectzelda.game.HealthBar;
import projectzelda.game.UIButton;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.URI;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

class SwingPanel extends JPanel implements GraphicSystem
{
  // constants
  private static final long serialVersionUID = 1L;
  private static final Font font = new Font("Arial",Font.PLAIN,24);

  
  // InputSystem is an external instance
  private AWTInputSystem inputSystem = new AWTInputSystem();
  private World       world       = null;

	
  // GraphicsSystem variables
  //
  private GraphicsConfiguration graphicsConf = 
    GraphicsEnvironment.getLocalGraphicsEnvironment().
    getDefaultScreenDevice().getDefaultConfiguration();
  private BufferedImage imageBuffer;
  private Graphics      graphics;

    // Images
    private MediaTracker tracker;
    private Map<String, Image> images = new HashMap<String, Image>();
    private BufferedImage background;
    private BufferedImage foreground;
    private MediaInfo mediaInfo;
    private WorldInfo worldInfo;
    private int width;
    private int height;
    private long lastTick = 0; 
    private List<ImageRefTo> animationTiles;

    public SwingPanel(MediaInfo mediaInfo, WorldInfo worldInfo) throws UnsupportedAudioFileException, LineUnavailableException, IOException 
    { 
        this.worldInfo = worldInfo;
        this.mediaInfo = mediaInfo;
        animationTiles = mediaInfo.getAnimationTiles(0);
        width = worldInfo.getPartWidth();
        height = worldInfo.getPartHeight();
        this.setSize(width, height);
        imageBuffer = graphicsConf.createCompatibleImage(
                this.getWidth(), this.getHeight());	 
        graphics = imageBuffer.getGraphics();

        // initialize Listeners
        this.addMouseListener(inputSystem);
        this.addMouseMotionListener(inputSystem);
        this.addKeyListener(inputSystem);

        // init media tracker
        tracker = new MediaTracker(this);
        try {
            List<String> sources = mediaInfo.getImageSources();
            for (int i = 0; i < sources.size(); i++) {
                String filepath = sources.get(i);
                URI resourceUri = getClass().getResource(filepath).toURI();
                Image img = ImageIO.read(new File(resourceUri));
                images.put(filepath, img);
                tracker.addImage(img, i);
            }
            tracker.waitForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<VirtualImage> virtualImages = mediaInfo.getVirtualImages();
        for (VirtualImage vi : virtualImages) {
            System.out.println("Loading virtual image: " + vi.name);
            BufferedImage bi = ImageDrawer.drawVirtualImage(vi, images, this);
            images.put(vi.name, bi);
        }
        background = ImageDrawer.createImage(mediaInfo.getBackgroundTiles(), worldInfo.getWidth(), worldInfo.getHeight(), images, this);
        foreground = ImageDrawer.createImage(mediaInfo.getForegroundTiles(), worldInfo.getWidth(), worldInfo.getHeight(), images, this);
    }

    public void clear(long tick)
    {
        graphics.drawImage(background, -(int)world.worldPartX, -(int)world.worldPartY, this);
        
        // Draw animated tiles
        if (tick - lastTick > 100) {
            animationTiles = mediaInfo.getAnimationTiles(tick);
            lastTick = tick;
        }
        ImageDrawer.drawTiles(graphics, animationTiles, -(int)world.worldPartX, -(int)world.worldPartY, images, this);
    }                                  

    public void drawForeground(long tick)
    {
        graphics.drawImage(foreground, -(int)world.worldPartX, -(int)world.worldPartY, this);
    }                                  


    public final void draw(GameObject dot)
    {	  
        if (dot.imageRef != null) {
            drawImage(dot);
            return;
        }

        int x = (int)(dot.x-dot.radius-world.worldPartX);
        int y = (int)(dot.y-dot.radius-world.worldPartY);
        int d = (int)(dot.radius*2);

        graphics.setColor(dot.color);
        graphics.fillOval(x, y, d, d);
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawOval(x,y,d,d);

        if(dot instanceof GoblinAI){
            draw(((GoblinAI) dot).healthBar);
        }
    }

    public final void draw(TextObject text)
    {	  
        graphics.setFont(font);
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawString(text.toString(), (int)text.x+1, (int)text.y+1);    
        graphics.setColor(text.color);
        graphics.drawString(text.toString(), (int)text.x, (int)text.y);
    }

    public final void draw(UIObject obj){
        if(obj instanceof UIButton) {
            drawButton((UIButton) obj);
        }else if(obj instanceof  HealthBar){
            drawHealthBar((HealthBar) obj);
        }
    }

    private final void drawButton(UIButton button){
        graphics.setColor(button.outlineColor);
        graphics.drawRect(button.x, button.y, button.width, button.height);
        graphics.setColor(button.color);
        graphics.fillRect(button.x, button.y, button.width, button.height);

        graphics.setColor(button.textColor);
        FontMetrics metrics = graphics.getFontMetrics(button.textFont);
        int x = button.x + (button.width - metrics.stringWidth(button.text)) / 2;
        int y = button.y + ((button.height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics.setFont(button.textFont);
        graphics.drawString(button.text, x, y);

    }

    private final void drawHealthBar(HealthBar healthBar) {
        int x = healthBar.isHudElement? healthBar.x : (int)(healthBar.x - world.worldPartX);
        int y = healthBar.isHudElement? healthBar.y :(int)(healthBar.y - world.worldPartY);

        graphics.setColor(healthBar.outlineColor);
        graphics.drawRect(x, y, healthBar.width, healthBar.height);
        graphics.setColor(healthBar.color);
        graphics.fillRect(x, y, healthBar.width, healthBar.height);

        graphics.setColor(healthBar.healthColor);
        int healthWidth = (int) (healthBar.health * healthBar.width);
        graphics.fillRect(x, y, healthWidth, healthBar.height);
    }


    public final void drawImage(GameObject dot) {
        int x = (int)(dot.x-dot.radius-world.worldPartX);
        int y = (int)(dot.y-dot.radius-world.worldPartY);
        int d = (int)(dot.radius*2);
        Image img = images.get(dot.imageRef.name);
        graphics.drawImage(img,
                x, y, x+d, y+d,
                dot.imageRef.x1, dot.imageRef.y1, dot.imageRef.x2, dot.imageRef.y2,
                this);
    }

    public void redraw()
    { this.getGraphics().drawImage(imageBuffer, 0, 0, this);
    }

    public final InputSystem getInputSystem() { return inputSystem; }
    public final void setWorld(World world_)  {this.world = world_;}
}

