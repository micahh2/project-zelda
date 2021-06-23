package projectzelda.game;

import projectzelda.engine.ImageRef;
import projectzelda.engine.Point;
import projectzelda.engine.GameObject;
import projectzelda.engine.GameObjectList;
import projectzelda.engine.RectangularGameObject;

public class NPC extends RectangularGameObject {

    boolean stuck = true;
    static final double STUCK_COUNTDOWN = 6;
    double stuckTimer;
    double originX;
    double originY;
    boolean flippedX = false;

    public NPC(double x, double y, int width, int height, ImageRef imageRef) {
        // x, y, alpha, speed, color
        super(x, y, 0, 50, width, height, null);
        this.imageRef = imageRef;
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
                case NPC:
                case GOBLIN:
                case BONES:
                case WATER:
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

    public Point newDestination() {
        double xOff = Math.random() * 128 - 64;
        double yOff = Math.random() * 128 - 64;
        return new Point<Double>(originX + xOff, originY+yOff);
    }

    public boolean readyForNewDestination() {
        return stuck && stuckTimer < 0;
    }

    public void hit() { die(); }
    public void die() { isLiving = false; }

    public int type() { return Const.Type.NPC.ordinal(); }
}
