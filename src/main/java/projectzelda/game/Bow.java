package projectzelda.game;

import projectzelda.*;
import java.util.List;
import java.util.LinkedList;
import java.awt.Color;

import projectzelda.engine.*;

public class Bow extends RectangularGameObject {

    public double lifeSeconds = 0;

    public boolean flippedX = false;
    ImageRef arrowImage;
    Arrow arrow;
    final double COOLDOWN = 0.7;
    double weaponTemp = 0;
    List<ImageRef> frames;

    public Bow(List<ImageRef> frames, ImageRef arrow) {
        super(0, 0, 0, 0, frames.get(0).x2, frames.get(0).y2, Color.WHITE);
        this.isMoving = false;
        this.imageRef = frames.get(0).clone();
        this.frames = frames;
        this.arrowImage = arrow;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
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
            gs.drawImage(imageRef, imgX, imgY, imgX+imageRef.x2, imgY+imageRef.y2);
            return;
        }
        imgX -= 2;
        gs.drawImage(imageRef, imgX+imageRef.x2, imgY, imgX, imgY+imageRef.y2);
    }

    public void fire(Arrow.Dir dir) {
        if (arrow != null && arrow.isLiving) { return; }
        if (weaponTemp > 0) { return; }
        weaponTemp = COOLDOWN;
        int imgX = (int)x-width/2;

        arrow = new Arrow(imgX, y, dir, arrowImage);
        world.gameObjects.add(arrow);
    }

    public int type() { return Const.Type.BOW.ordinal(); }

    public GameObject clone() {
        List<ImageRef> newF = new LinkedList<ImageRef>();
        for (ImageRef f : frames) {
            newF.add(f.clone());
        }
        Bow b = new Bow(newF, arrowImage.clone());
        b.isMoving = isMoving;
        b.hasDestination = hasDestination;
        b.isLiving = isLiving;
        b.x = x;
        b.y = y;
        b.width = width;
        b.height = height;
        if (flippedX) { b.flip(); }
        return b;
    }
}
