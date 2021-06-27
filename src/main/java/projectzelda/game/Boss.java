package projectzelda.game;

import projectzelda.engine.*;

public class Boss extends EnemyAI
{
    public GameObject taget;
    private int posXChatBox = world.worldInfo.getPartWidth() / 2 - 300;
    private int posYChatBox = world.worldInfo.getPartHeight() - 100;

    public Boss(double x, double y, ImageRef imageRef, GameObject taget)
    {
        super(x, y, null);
        this.imageRef = imageRef;
        this.taget = taget;
    }

    @Override
    public Point newDestination() {
        return new Point<Double>(taget.x, taget.y);
    }

    @Override
    public boolean readyForNewDestination() {

        double dist = world
                .getPhysicsSystem()
                .distance(x, y, taget.x, taget.y);
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

            // if object is taget, we're being attacked
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
    public void hit() {
        if (!isLiving) { return; }
        // every hit decreases life


        if (world.weaponState == WeaponState.SWORD) {

            world.gameState = GameState.DIALOG;
            ChatBoxButton chatBox = new ChatBoxButton(posXChatBox, posYChatBox, 600, 100, "Adlez: His armour is too thick!",this);
            world.chatBoxObjects.add(chatBox);
        } else {
            life -= 0.21;
            healthBar.health = life;
            color = REDDER;
            colorCooldown = COLOR_COOLDOWN;

            // if Goblin is dead, delete it
            if (life <= 0) {
                die();
            }
        }

    }

    public void die() {
        this.isLiving = false;
        ((RPGWorld)world).addBones(x, y);
        world.gameState = GameState.COMPLETE;
    }

}


