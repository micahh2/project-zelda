package projectzelda.game;

import projectzelda.*;
import java.awt.Color;
import projectzelda.engine.CircularGameObject;
import projectzelda.engine.GraphicSystem;
import projectzelda.engine.GameObjectList;
import projectzelda.engine.GameObject;
import projectzelda.engine.ImageRef;

public class SwordSwing extends CircularGameObject {

    public double lifeSeconds = 0;

    public boolean flippedX = false;
    public boolean hasHit = false;

    public SwordSwing(double x, double y, ImageRef imageRef, boolean flippedX) {
        super(x + (flippedX ? 10 : -10), y, 0, 0, 22, Color.WHITE);

        this.isMoving = false;
        this.imageRef = imageRef;
        this.flippedX = flippedX;
    }

    public void offset(int x, int y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public void move(double diffSeconds) {
        lifeSeconds += diffSeconds;
        if (lifeSeconds > .2) { isLiving = false; }

        if (hasHit) { return; }

        GameObjectList collisions = world.getPhysicsSystem().getCollisions(this);
        for (int i = 0; i < collisions.size(); i++) {
            GameObject obj = collisions.get(i);
            int type = obj.type();
            switch (type) {
                case Const.TYPE_GOBLIN:
                    ((EnemyAI)obj).hit();
                    hasHit = true;
            }
        }
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        int imgX = (int)x-radius;
        int imgY = (int)y-radius;
        if (flippedX) {
            gs.drawImage(imageRef, (int)imgX, (int)imgY, (int)imgX+imageRef.x2, (int)imgY+imageRef.y2);
            return;
        }
        gs.drawImage(imageRef, (int)imgX+imageRef.x2, (int)imgY, (int)imgX, (int)imgY+imageRef.y2);
    }

    public int type() { return Const.TYPE_SWORD_SWING; }
}