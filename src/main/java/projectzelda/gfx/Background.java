
package projectzelda.gfx;

import java.util.List;
import java.util.Map;
import projectzelda.engine.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class Background {
    public static BufferedImage drawBackground(List<ImageRefTo> tiles, int width, int height, Map<String, Image> images, ImageObserver ob) {
        BufferedImage imageBuffer = new BufferedImage(16000, 16000, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D)imageBuffer.getGraphics();
        // int i = 0;
        AffineTransform clearTransform = graphics.getTransform();
        for (ImageRefTo tile : tiles) {
            // if (i % 10 == 0) { System.out.println(""); }
            // System.out.print(tile.x1+":"+tile.y1+",");

            if (tile.horizontallyFliped || tile.verticallyFliped || tile.diagonallyFliped) {

                int tilewidth = tile.destx2 - tile.destx1;
                int tileheight = tile.desty2 - tile.desty1;
                int centerx = tile.destx1 + Math.round(tilewidth/2);
                int centery = tile.desty1 + Math.round(tileheight/2);
                AffineTransform trans = AffineTransform.getTranslateInstance(centerx, centery);

                // Flip the image diagonally
                if (tile.diagonallyFliped) { 
                    trans.rotate(Math.toRadians(180));
                    trans.translate(-centerx, -centery);
                }

                // Flip the image vertically
                if (tile.verticallyFliped) { 
                    trans.scale(1, -1);
                    trans.translate(-centerx, -centery);
                }

                // Flip the image horizontally
                if (tile.horizontallyFliped) {
                    trans.scale(-1, 1);
                    trans.translate(-centerx, -centery);
                }

                graphics.transform(trans);
            }

            graphics.drawImage(images.get(tile.name), 
                    tile.destx1, tile.desty1, tile.destx2, tile.desty2, 
                    tile.x1, tile.y1, tile.x2, tile.y2, 
            ob);
            graphics.setTransform(clearTransform);
            // i++;
        }

        return imageBuffer;
    }
}
