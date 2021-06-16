package projectzelda.game;

import projectzelda.Const;
import projectzelda.engine.*;

public class Monster extends EnemyAI
{
    public Avatar avatar;

    public Monster(double x, double y, ImageRef imageRef, Avatar avatar)
    {
        super(x, y, null);
        this.imageRef = imageRef;
        this.avatar = avatar;
    }

    @Override
    public Point newDestination() {
        return new Point<Double>(avatar.x, avatar.y);
    }

    @Override
    public boolean readyForNewDestination() {
        double dist = world
                .getPhysicsSystem()
                .distance(x, y, avatar.x, avatar.y);

        return dist < 160;
    }

    @Override
    public void draw(GraphicSystem gs) {
        gs.draw(this);
        healthBar.draw(gs);
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


}
