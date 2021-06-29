package projectzelda.game;

import projectzelda.*;
import java.util.List;
import java.awt.Color;

import projectzelda.engine.*;

public class Sword extends RectangularGameObject {

    final double COOLDOWN = 0.5;
    double weaponTemp = 0;

    public boolean flippedX = false;
    ImageRef swordSwingImage;
    SwordSwing swordSwing;

    public Sword(ImageRef imageRef, ImageRef swordSwing) {
        super(0, 0, 0, 0, imageRef.x2, imageRef.y2, Color.WHITE);
        this.isMoving = false;
        this.imageRef = imageRef.clone();
        this.swordSwingImage = swordSwing;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
        if (this.swordSwing != null) {
            this.swordSwing.setXY(x, y);
        }
    }

    public void flip() {
        flippedX = !flippedX;
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        int swordx = (int)Math.round(flippedX ? x : (x-imageRef.x2)); //-radius*1.5;
        int swordy = (int)Math.round(y - imageRef.x2 * 1.2);
        int width = (int)Math.round((imageRef.x2)*0.8);
        int height = (int)Math.round((imageRef.y2)*0.8);

        if (swordSwing == null || !swordSwing.isLiving) {
            gs.drawImage(imageRef, swordx, swordy, swordx+width, swordy+height);
        }
    }

    public void fire() {
        if (weaponTemp > 0) { return; }
        Sound sword = new Sound("/music/sword-sound-1_16bit.wav");
        sword.setVolume(-30.0f);
        sword.playSound();

        weaponTemp = COOLDOWN;
        swordSwing = new SwordSwing(x, y, swordSwingImage, flippedX);
        world.gameObjects.add(swordSwing);
    }

    public int type() { return Const.Type.BOW.ordinal(); }

    public GameObject clone() {
        Sword s = new Sword(imageRef.clone(), swordSwingImage.clone());
        if (flippedX) { s.flip(); }

        return s;
    }
}
