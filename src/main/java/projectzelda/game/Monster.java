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
    public void draw(GraphicSystem gs, long tick) {
        gs.draw(this);
        healthBar.draw(gs, tick);
    }

    public void move(double diffSeconds) {
        if (hitCooldown >= 0) { hitCooldown -= diffSeconds; }
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

            switch (type) {
                case Const.TYPE_AVATAR:
                    isMoving = false;
                    state = State.STUCK;
                    moveBack();
                    if (hitCooldown < 0) {
                        ((Avatar)obj).hit();
                        hitCooldown = hitCooldownSeconds;
                    }
                    break;
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
