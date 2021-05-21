package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.Color;


class GoblinAI extends GameObject
{
    // hit points
    private double life = 1.0;

    private static final Color NORMAL_COLOR = new Color(160, 80, 40);
    private static final Color REDDER = new Color(160, 40, 20);
    private static final double COLOR_COOLDOWN = 0.2;
    private double colorCooldown = 0;

    private enum State {
        STUCK,
        FREE
    }
    private State state;

    public GoblinAI(double x, double y)
    {
        super(x, y, 0, 60, 15, new Color(160,80,40));
        isMoving = true;
        state = State.FREE;
    }


    public void move(double diffSeconds)
    {
        if (colorCooldown >= 0) {
            colorCooldown -= diffSeconds;
        } else {
            color = NORMAL_COLOR;
        }

        double dist = world
            .getPhysicsSystem()
            .distance(x, y, destX, destY);

        // Close to goal, pick a new one
        if(state == State.STUCK || dist < 8) { 
            double x = Math.random() * Const.WORLD_WIDTH;
            double y = Math.random() * Const.WORLD_HEIGHT;
            state = State.FREE;
            setDestination(x, y); 
        }

        super.move(diffSeconds);

        // handle collisions of the zombie
        GameObjectList collisions = world.getPhysicsSystem().getCollisions(this);
        for(int i = 0; i < collisions.size(); i++)
        {
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
    public void hit()
    { 
        // every hit decreases life
        life -= 0.21;
        color = REDDER;
        colorCooldown = COLOR_COOLDOWN;

        // if Goblin is dead, delete it	
        if(life <= 0) { die(); }
    }

    public void die() {
        this.isLiving=false;
        this.world.gameObjects.add(new Bones(x, y));
    }


    public int type() { return Const.TYPE_GOBLIN; }
}
