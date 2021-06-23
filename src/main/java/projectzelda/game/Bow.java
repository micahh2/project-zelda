package projectzelda.game;

import projectzelda.*;
import java.util.List;
import java.awt.Color;

import projectzelda.engine.*;

public class Bow extends RectangularGameObject {

    public double lifeSeconds = 0;

    public boolean flippedX = false;
    ImageRef arrowImage;
    Arrow arrow;
    List<ImageRef> frames;

    public Bow(List<ImageRef> frames, ImageRef arrow) {
        super(0, 0, 0, 0, frames.get(0).x2, frames.get(0).y2, Color.WHITE);
        this.isMoving = false;
        this.imageRef = frames.get(0);
        this.frames = frames;
        this.arrowImage = arrow;
    }

    public void offset(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void flip() {
        flippedX = !flippedX;
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        int imgX = (int)x-width/2;
        int imgY = (int)y-height/2;
        if (flippedX) {
            imgX += 2;
            gs.drawImage(imageRef, (int)imgX, (int)imgY, (int)imgX+imageRef.x2, (int)imgY+imageRef.y2);
            return;
        }
        imgX -= 2;
        gs.drawImage(imageRef, (int)imgX+imageRef.x2, (int)imgY, (int)imgX, (int)imgY+imageRef.y2);
    }

    public void fire(Arrow.Dir dir) {
        if (arrow != null && arrow.isLiving) { return; }
        int imgX = (int)x-width/2;

        arrow = new Arrow(imgX, y, dir, arrowImage);
        world.gameObjects.add(arrow);
    }

    public int type() { return Const.Type.BOW.ordinal(); }
}
