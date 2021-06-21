package projectzelda.game;

import projectzelda.engine.GraphicSystem;
import projectzelda.engine.RectangularGameObject;

import java.awt.*;


public class Rock extends RectangularGameObject
{

    public HealthBar healthBar;
    public int  radius = 20;
    protected double life = 1.0;
    protected static final Color REDDER = new Color(160, 40, 20);
    protected static final double COLOR_COOLDOWN = 0.2;
    protected double colorCooldown = 0;

    public Rock(double x, double y)
        {
            super(x, y, 0, 0, 128, 46, null);
            this.isMoving = false;
            healthBar = new HealthBar(0, 0, 80, 5);
            adjustHealthBarPosition();
        }

    // Invisible
    @Override
    public void draw(GraphicSystem gs, long tick) {

        healthBar.draw(gs, tick);
        return;

    }

    public int type() { return Const.Type.ROCK.ordinal(); }

    protected void adjustHealthBarPosition() {
        int healthBarX = (int) (x - healthBar.width /10);
        int healthBarY = (int) (y - radius - 7 * healthBar.height);
        healthBar.x = healthBarX;
        healthBar.y = healthBarY;
    }

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
    }

}
