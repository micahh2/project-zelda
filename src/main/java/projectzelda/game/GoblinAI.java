package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;


public class GoblinAI extends CircularGameObject {
    // hit points
    protected double life = 1.0;

    protected static final Color NORMAL_COLOR = new Color(160, 80, 40);
    protected static final Color REDDER = new Color(160, 40, 20);
    protected static final double COLOR_COOLDOWN = 0.2;
    protected double colorCooldown = 0;
    protected WorldInfo worldInfo;

    protected enum State {
        STUCK,
        FREE
    }

    protected State state;

    public HealthBar healthBar;

    public GoblinAI(double x, double y, WorldInfo worldInfo) {
        super(x, y, 0, 60, 15, new Color(160, 80, 40));
        this.imageRef = imageRef;
        this.worldInfo = worldInfo;
        isMoving = true;
        state = State.FREE;
        healthBar = new HealthBar(0, 0, 50, 5);
        adjustHealthBarPosition();
        setDestination(x, y); // Start still
    }

    public Point newDestination() {
        double x = Math.random() * worldInfo.getWidth();
        double y = Math.random() * worldInfo.getHeight();
        return new Point<Double>(x, y);
    }

    public boolean readyForNewDestination() {
        double dist = world
                .getPhysicsSystem()
                .distance(x, y, destX, destY);
        return state == State.STUCK || dist < 8;
    }

    public void move(double diffSeconds) {
        if (colorCooldown >= 0) {
            colorCooldown -= diffSeconds;
        } else {
            color = NORMAL_COLOR;
        }

        // Close to goal, pick a new one
        if (readyForNewDestination()) {
            Point<Double> newDest = newDestination();
            state = State.FREE;
            setDestination(newDest.x, newDest.y);
        }

        super.move(diffSeconds);
        adjustHealthBarPosition();

        // handle collisions of the zombie
        GameObjectList collisions = world.getPhysicsSystem().getCollisions(this);
        for (int i = 0; i < collisions.size(); i++) {
            GameObject obj = collisions.get(i);

            int type = obj.type();

            // if object is avatar, we're being attacked
            switch (type) {
                case Const.TYPE_AVATAR:
                case Const.TYPE_TREE:
                case Const.TYPE_GOBLIN:
                case Const.TYPE_ZOMBIE:
                    isMoving = false;
                    state = State.STUCK;
                    moveBack();
                    break;
            }
        }

    }


    // inform goblin it is hit
    public void hit() {
        // every hit decreases life
        life -= 0.21;
        healthBar.health = life;
        color = REDDER;
        colorCooldown = COLOR_COOLDOWN;

        // if Goblin is dead, delete it	
        if (life <= 0) {
            die();
        }
    }

    public void die() {
        this.isLiving = false;
        this.world.gameObjects.add(new Bones(x, y));
    }


    public int type() {
        return Const.TYPE_GOBLIN;
    }

    protected void adjustHealthBarPosition() {
        int healthBarX = (int) (x - healthBar.width / 2);
        int healthBarY = (int) (y - radius - 2 * healthBar.height);
        healthBar.x = healthBarX;
        healthBar.y = healthBarY;
    }
}
