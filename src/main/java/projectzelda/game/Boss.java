package projectzelda.game;

import projectzelda.engine.*;

public class Boss extends EnemyAI
{
    public Avatar avatar;


    public Boss(double x, double y, ImageRef imageRef, ImageRef bones , Avatar avatar)
    {
        super(x, y, bones,null);
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
    public void draw(GraphicSystem gs, long tick) {
        gs.draw(this);
        healthBar.draw(gs, tick);
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

            Const.Type type = Const.Type.values()[obj.type()];

            // if object is avatar, we're being attacked
            switch (type) {
                case AVATAR:
                case TREE:
                case ROCK:
                case GOBLIN:
                case LAVA:
                case WATER:
                    isMoving = false;
                    state = State.STUCK;
                    moveBack();
                    break;
            }
        }

    }


}


