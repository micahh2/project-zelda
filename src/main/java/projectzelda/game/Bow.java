package projectzelda.game;

import projectzelda.*;
import java.util.List;
import java.awt.Color;

import projectzelda.engine.*;

public class Bow extends RectangularGameObject {

    public double lifeSeconds = 0;

    public boolean flippedX = false;
    public boolean hasHit = false;
    ImageRef arrow;
    List<ImageRef> frames;

    public Bow(List<ImageRef> frames, ImageRef arrow) {
        super(0, 0, 0, 0, frames.get(0).x2, frames.get(0).y2, Color.WHITE);
        this.isMoving = false;
        this.imageRef = frames.get(0);
        this.frames = frames;
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
        int imgX = (int)x-width*2/3;
        int imgY = (int)y-height/2;
        if (flippedX) {
            gs.drawImage(imageRef, (int)imgX, (int)imgY, (int)imgX+imageRef.x2, (int)imgY+imageRef.y2);
            return;
        }
        gs.drawImage(imageRef, (int)imgX+imageRef.x2, (int)imgY, (int)imgX, (int)imgY+imageRef.y2);
    }

    public int type() { return Const.Type.BOW.ordinal(); }
}
