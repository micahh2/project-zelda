
// (c) Thorsten Hasbargen

package projectzelda.gfx;

import projectzelda.engine.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.URI;
import javax.swing.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

class SwingPanel extends JPanel implements GraphicSystem {
    // constants
    private static final long serialVersionUID = 1L;
    private static final Font font = new Font("Arial", Font.PLAIN, 24);


    // InputSystem is an external instance
    private AWTInputSystem inputSystem = new AWTInputSystem();
    private World world = null;


    // GraphicsSystem variables
    //
    private GraphicsConfiguration graphicsConf =
            GraphicsEnvironment.getLocalGraphicsEnvironment().
                    getDefaultScreenDevice().getDefaultConfiguration();
    private BufferedImage imageBuffer;
    private Graphics graphics;

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

    public SwingPanel(MediaInfo mediaInfo, WorldInfo worldInfo) {
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

    public void clear(long tick) {
        graphics.drawImage(background, -(int) world.worldPartX, -(int) world.worldPartY, this);

        // Draw animated tiles
        if (tick - lastTick > 100) {
            animationTiles = mediaInfo.getAnimationTiles(tick);
            lastTick = tick;
        }
        ImageDrawer.drawTiles(graphics, animationTiles, -(int) world.worldPartX, -(int) world.worldPartY, images, this);
    }

    public void drawForeground(long tick) {
        graphics.drawImage(foreground, -(int) world.worldPartX, -(int) world.worldPartY, this);
    }


    public final void draw(RectangularGameObject dot) {
        int x1 = (int) (dot.x - world.worldPartX);
        int y1 = (int) (dot.y - world.worldPartY);
        int x2 = x1 + dot.width;
        int y2 = y1 + dot.height;
        if (dot.imageRef != null) {
            drawImageScreen(dot.imageRef, x1, y1, x2, y2);
            return;
        }

        graphics.setColor(dot.color);
        graphics.fillRect(x1, y1, dot.width, dot.height);
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawRect(x1, y1, dot.width, dot.height);
    }

    public final void draw(CircularGameObject dot) {

        int x = (int) (dot.x - dot.radius - world.worldPartX);
        int y = (int) (dot.y - dot.radius - world.worldPartY);
        int d = (dot.radius * 2);

        if (dot.imageRef != null) {
            drawImageScreen(dot.imageRef, x, y, x + d, y + d);
            return;
        }


        graphics.setColor(dot.color);
        graphics.fillOval(x, y, d, d);
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawOval(x, y, d, d);
    }

    public final void draw(TextObject text) {
        graphics.setFont(font);
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawString(text.toString(), (int) text.x + 1, (int) text.y + 1);
        graphics.setColor(text.color);
        graphics.drawString(text.toString(), (int) text.x, (int) text.y);
    }

    // For drawing with absolute world coordinates
    public void drawRect(int xAbs, int yAbs, int width, int height, Color color) {
        int x = (int) (xAbs - world.worldPartX);
        int y = (int) (yAbs - world.worldPartY);
        drawRectScreen(x, y, width, height, color);
    }

    public void fillRect(int xAbs, int yAbs, int width, int height, Color color) {
        int x = (int) (xAbs - world.worldPartX);
        int y = (int) (yAbs - world.worldPartY);
        fillRectScreen(x, y, width, height, color);
    }

    public void drawOval(int xAbs, int yAbs, int r1, int r2, Color color) {
        int x = (int) (xAbs - world.worldPartX);
        int y = (int) (yAbs - world.worldPartY);
        drawOvalScreen(x, y, r1, r2, color);
    }

    public void fillOval(int xAbs, int yAbs, int r1, int r2, Color color) {
        int x = (int) (xAbs - world.worldPartX);
        int y = (int) (yAbs - world.worldPartY);
        fillOvalScreen(x, y, r1, r2, color);
    }

    public void drawCenteredText(int xAbs, int yAbs, int width, int height, Color color, Font font, String text) {
        int x = (int) (xAbs - world.worldPartX);
        int y = (int) (yAbs - world.worldPartY);
        drawCenteredTextScreen(x, y, width, height, color, font, text);
    }

    public void drawImage(ImageRef imageRef, int x1Abs, int y1Abs, int x2Abs, int y2Abs) {
        int x1 = (int) (x1Abs - world.worldPartX);
        int y1 = (int) (y1Abs - world.worldPartY);
        int x2 = (int) (x2Abs - world.worldPartX);
        int y2 = (int) (y2Abs - world.worldPartY);
        drawImageScreen(imageRef, x1, y1, x2, y2);
    }

    public void drawHudImage(ImageRef imageRef, int x1Abs, int y1Abs, int x2Abs, int y2Abs) {
        int x1 = x1Abs;
        int y1 = y1Abs;
        int x2 = x2Abs;
        int y2 = y2Abs;
        drawImageScreen(imageRef, x1, y1, x2, y2);
    }

    // For drawing with relative screen coordinates
    public void drawRectScreen(int x, int y, int width, int height, Color color) {
        graphics.setColor(color);
        graphics.drawRect(x, y, width, height);
    }

    public void fillRectScreen(int x, int y, int width, int height, Color color) {
        graphics.setColor(color);
        graphics.fillRect(x, y, width, height);
    }

    public void drawOvalScreen(int x, int y, int r1, int r2, Color color) {
        graphics.setColor(color);
        graphics.drawOval(x, y, r1, r2);
    }

    public void fillOvalScreen(int x, int y, int r1, int r2, Color color) {
        graphics.setColor(color);
        graphics.fillOval(x, y, r1, r2);
    }

    public void drawCenteredTextScreen(int x, int y, int width, int height, Color color, Font font, String text) {
        graphics.setColor(color);
        FontMetrics metrics = graphics.getFontMetrics(font);
        x = x + (width - metrics.stringWidth(text)) / 2;
        y = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics.setFont(font);
        graphics.drawString(text, x, y);
    }
     public void drawCenteredTextScreenWithSub(int x, int y, int width, int height, Color color, Font font,Font helpFont, String text, String helpText){
        graphics.setColor(color);
        FontMetrics metrics = graphics.getFontMetrics(font);
        int xMain = x + (width - metrics.stringWidth(text)) / 2;
        int yMain = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
        graphics.setFont(font);
        graphics.drawString(text, xMain, yMain);

        metrics = graphics.getFontMetrics(helpFont);
        int helpX = x + (width - metrics.stringWidth(helpText)) / 2;
        int helpY = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent() + 25;
        graphics.setFont(helpFont);
        graphics.drawString(helpText, helpX, helpY);

    }

    public final void drawImageScreen(ImageRef imageRef, int x1, int y1, int x2, int y2) {
        Image img = images.get(imageRef.name);
        graphics.drawImage(img,
                x1, y1, x2, y2,
                imageRef.x1, imageRef.y1, imageRef.x2, imageRef.y2,
                this);
    }

    public void redraw() {
        this.getGraphics().drawImage(imageBuffer, 0, 0, this);
    }

    public final InputSystem getInputSystem() {
        return inputSystem;
    }

    public final void setWorld(World world_) {
        this.world = world_;
    }
}

