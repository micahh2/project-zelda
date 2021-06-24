
package projectzelda.gfx;

import java.util.List;
import java.util.Map;
import projectzelda.engine.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.AlphaComposite;

public class ImageDrawer {
    public static BufferedImage createImage(List<ImageRefTo> tiles, int width, int height, Map<String, Image> images, ImageObserver ob) {
        BufferedImage imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D)imageBuffer.getGraphics();
        drawTiles(graphics, tiles, 0, 0, images, ob);
        return imageBuffer;
    }
    public static BufferedImage drawVirtualImage(VirtualImage vi, Map<String, Image> images, ImageObserver ob) {
        BufferedImage imageBuffer = new BufferedImage(vi.width, vi.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D)imageBuffer.getGraphics();
        drawTiles(graphics, vi.sources, 0, 0, images, ob);
        return imageBuffer;
    }
    public static void drawTiles(Graphics inGraphics, List<ImageRefTo> tiles, int offsetx, int offsety, Map<String, Image> images, ImageObserver ob) {
        Graphics2D graphics = (Graphics2D)inGraphics;
        AffineTransform clearTransform = graphics.getTransform();
        for (ImageRefTo tile : tiles) {

            if (tile.horizontallyFlipped || tile.verticallyFlipped || tile.diagonallyFlipped) {

                int tilewidth = tile.destx2 - tile.destx1;
                int tileheight = tile.desty2 - tile.desty1;
                int centerx = tile.destx1 + (int)(tilewidth/2);
                int centery = tile.desty1 + (int)(tileheight/2);
                AffineTransform trans = AffineTransform.getTranslateInstance(centerx, centery);

                // Flip the image diagonally
                if (tile.diagonallyFlipped) { 
                    trans.rotate(Math.toRadians(270));
                }

                // Flip the image vertically
                if (tile.verticallyFlipped) { 
                    trans.scale(1, -1);
                }

                // Flip the image horizontally
                if (tile.horizontallyFlipped) {
                    trans.scale(-1, 1);
                }

                trans.translate(-centerx, -centery);
                graphics.transform(trans);
            }

            graphics.drawImage(images.get(tile.name), 
                    tile.destx1+offsetx, tile.desty1+offsety, tile.destx2+offsetx, tile.desty2+offsety, 
                    tile.x1, tile.y1, tile.x2, tile.y2, 
            ob);
            graphics.setTransform(clearTransform);
        }
    }

    public static void clearImageAreas(BufferedImage im, List<ImageRef> areas, ImageObserver ob) {
        Graphics2D graphics = (Graphics2D)im.getGraphics();
        AlphaComposite alcom = AlphaComposite.getInstance(
                    AlphaComposite.CLEAR, 0f);
            graphics.setComposite(alcom);

        for (ImageRef area : areas) {
            int width = area.x2 - area.x1;
            int height = area.y2 - area.y1;
            graphics.fillRect(area.x1, area.y1, width, height);
        }
    }
}
