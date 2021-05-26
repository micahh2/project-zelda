
// (c) Thorsten Hasbargen

package projectzelda.gfx;

import projectzelda.*;
import projectzelda.engine.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import java.awt.image.*;
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
    private MediaInfo mediaInfo;

    public SwingPanel(MediaInfo mediaInfo)
    { 
        this.mediaInfo = mediaInfo;
        int width = Const.WORLDPART_WIDTH;
        int height = Const.WORLDPART_HEIGHT;
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
                String filename = sources.get(i);
                Image img = ImageIO.read(new File(filename));
                images.put(filename, img);
                tracker.addImage(img, i);
            }
            tracker.waitForAll();
        } catch (Exception e) {
            //throw new IOException(e.getMessage());
            e.printStackTrace();
        }

        background = Background.drawBackground(mediaInfo.getBackgroundTiles(), width, height, images, this);
    }

    public void clear()
    {
        int width = Const.WORLDPART_WIDTH;
        int height = Const.WORLDPART_HEIGHT;
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, Const.WORLDPART_WIDTH, Const.WORLDPART_HEIGHT);
        // graphics.drawImage(background, -(int)world.worldPartX, -(int)world.worldPartY, this);
        graphics.drawImage(background, Const.WORLDPART_WIDTH-(int)world.worldPartX, Const.WORLDPART_HEIGHT-(int)world.worldPartY, this);
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
    }

    public final void draw(TextObject text)
    {	  
        graphics.setFont(font);
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawString(text.toString(), (int)text.x+1, (int)text.y+1);    
        graphics.setColor(text.color);
        graphics.drawString(text.toString(), (int)text.x, (int)text.y);
    }

    public final void drawImage(GameObject dot) {
        //System.out.println("Draw image not implemented!");
        //graphics.drawImage(images[0], 10, 10, this);
        //int dstx1, int dsty1, int dstx2, int dsty2,
        //int srcx1, int srcy1, int srcx2, int srcy2,

        int x = (int)(dot.x-dot.radius-world.worldPartX);
        int y = (int)(dot.y-dot.radius-world.worldPartY);
        int d = (int)(dot.radius*2);
        graphics.drawImage(images.get(dot.imageRef.name),
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

