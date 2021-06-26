package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;


public abstract class EnemyAI extends CircularGameObject {
    // hit points
    protected double life = 1.0;

    protected static final Color NORMAL_COLOR = new Color(160, 80, 40);
    protected static final Color REDDER = new Color(160, 40, 20);
    protected static final double COLOR_COOLDOWN = 0.2;
    protected double colorCooldown = 0;
    protected WorldInfo worldInfo;

    protected double hitCooldownSeconds = 0.5;
    protected double hitCooldown = 0;

    protected enum State {
        STUCK,
        FREE
    }

    protected State state;

    public HealthBar healthBar;

    public ImageRef bones;

    public EnemyAI(double x, double y, ImageRef bones , WorldInfo worldInfo) {
        super(x, y, 0, 60, 15, new Color(160, 80, 40));
        this.worldInfo = worldInfo;
        this.bones = bones;
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
        this.world.gameObjects.addFirst(new Bones(x, y, bones));
    }


    public int type() {
        return Const.Type.GOBLIN.ordinal();
    }

    protected void adjustHealthBarPosition() {
        int healthBarX = (int) (x - healthBar.width / 2);
        int healthBarY = (int) (y - radius - 2 * healthBar.height);
        healthBar.x = healthBarX;
        healthBar.y = healthBarY;
    }
}
