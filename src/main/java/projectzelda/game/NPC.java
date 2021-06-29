package projectzelda.game;

import projectzelda.engine.ImageRef;
import projectzelda.engine.Point;
import projectzelda.engine.GameObject;
import projectzelda.engine.GameObjectList;
import projectzelda.engine.RectangularGameObject;
import projectzelda.engine.GraphicSystem;
import java.awt.Color;

public class NPC extends RectangularGameObject {

    boolean stuck = true;
    static final double STUCK_COUNTDOWN = 6;
    double stuckTimer;
    double originX;
    double originY;
    boolean flippedX = false;
    GameObject followTarget;

    public NPC(double x, double y, int width, int height, ImageRef imageRef) {
        // x, y, alpha, speed, color
        super(x, y, 0, 50, width, height, null);
        this.imageRef = imageRef.clone();
        this.isMoving = true;
        Point<Double> newDest = newDestination();
        setDestination(newDest.x, newDest.y);
        originX = x;
        originY = y;
        stuckTimer = STUCK_COUNTDOWN*Math.random();
    }

    public String getNpcQuestText(QuestState q, int index) {
        return getNpcQuestText(q)[index];
    }
    public String[] getNpcQuestText(QuestState q) { return new String[] {}; }
    public boolean progressFromTalk(QuestState q) { return false; }

    @Override
    public void move(double diffSeconds) {
        QuestState q = ((RPGWorld)world).questState;
        if (hasQuestProgress(q)) { 
            makeQuestProgress(q);
            ((RPGWorld)world).nextQuest();
        }

        double dist = world.getPhysicsSystem().distance(x, y, destX, destY);
        if (dist < 8) {
            stuck = true;
        }

        // Close to goal, pick a new one
        if (readyForNewDestination()) {
            stuck = false;
            stuckTimer = STUCK_COUNTDOWN * Math.random();
            Point<Double> newDest = newDestination();
            setDestination(newDest.x, newDest.y);
        }

        if (stuck) {
            stuckTimer -= diffSeconds;
            return;
        }

        int startx = (int)x;
        super.move(diffSeconds);

        GameObjectList collisions = world.getPhysicsSystem().getCollisions(this);
        for(int i = 0; i < collisions.size(); i++) {
            GameObject obj = collisions.get(i);
            Const.Type type = Const.Type.values()[obj.type()];
            switch (type) {
                // if Object is a tree, move back one step
                case TREE:
                case CHEST:
                case PUMPKIN:
                case GOBLIN:
                case BONES:
                case NPC:
                case ANIMAL:
                case WALL:
                case LAVA:
                case WATER:
                case AVATAR:
                    stuck = true;
                    this.moveBack(); 
                    break;
            }
        }

        // Hacky, but we can flip the orientation by switching the image coordinates to draw from
        int endx = (int)x;
        if ((startx < endx && !flippedX) || (startx > endx && flippedX)) {
            int tempx = imageRef.x1;
            imageRef.x1 = imageRef.x2;
            imageRef.x2 = tempx;
            flippedX = !flippedX;
        }
    }

    public void setFollow(GameObject go) {
        followTarget = go;
    }
    public GameObject getFollow() {
        return followTarget;
    }

    public void setOrigin(double inX, double inY) {
        originX = inX;
        originY = inY;
    }

    public Point newDestination() {
        if (followTarget == null) {
            double xOff = Math.random() * 128 - 64;
            double yOff = Math.random() * 128 - 64;
            return new Point<Double>(originX + xOff, originY+yOff);
        }
        return new Point<Double>(followTarget.x, followTarget.y);
    }

    public boolean hasQuestProgress(QuestState q) { return false; }
    public void makeQuestProgress(QuestState q) { return; }

    public boolean readyForNewDestination() {
        return followTarget != null || stuck && stuckTimer < 0;
    }

    public void flip() {
        flippedX = !flippedX;
    }

    public void hit() { die(); }
    public void die() { isLiving = false; }

    public int type() { return Const.Type.NPC.ordinal(); }

    public GameObject clone() {
        NPC n = new NPC(x, y, width, height, imageRef.clone());
        setClone(n);
        return n;
    }

    public void setClone(NPC n) {
        super.setClone(n);
        n.setOrigin(originX, originY);
        n.setFollow(followTarget);
        if (flippedX) { n.flip(); }
    }
}
