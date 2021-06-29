package projectzelda.game;

import projectzelda.engine.*;

public class Monster extends EnemyAI
{
    public GameObject target;

    private int posXChatBox = world.worldInfo.getPartWidth() / 2 - 300;
    private int posYChatBox = world.worldInfo.getPartHeight() - 100;
    boolean hasHadFreeMove = false;

    public Monster(double x, double y, ImageRef imageRef,  GameObject target)
    {
        super(x, y, null);
        this.imageRef = imageRef;
        this.target = target;
    }

    @Override
    public Point newDestination() {
        return new Point<Double>(target.x, target.y);
    }

    @Override
    public boolean readyForNewDestination() {
        double dist = world
                .getPhysicsSystem()
                .distance(x, y, target.x, target.y);

        return dist < 160;
    }

    @Override
    public void draw(GraphicSystem gs, long tick) {
        gs.draw(this);
        healthBar.draw(gs, tick);
    }

    public void hit() {
        if (!isLiving) { return; }
        // every hit decreases life
        if (((RPGWorld) world).questState == QuestState.OLGA_MONSTERS ) {
            // every hit decreases life
            life -= 0.21;
            healthBar.health = life;
            color = REDDER;
            colorCooldown = COLOR_COOLDOWN;

            // if Goblin is dead, delete it
            if (life <= 0) {
                die();
            }
            // make monsters easier to kill during bossfight > can quickly stack up and become too chaotic?
        } else if (((RPGWorld) world).questState == QuestState.BOSS) {
            //world.gameState = GameState.DIALOG;
            // if const.type is rock game freezes, using bones or goblin seems to work ok though
            life -= 1.0;
            healthBar.health = life;
            color = REDDER;
            colorCooldown = COLOR_COOLDOWN;

            // if Goblin is dead, delete it
            if (life <= 0) {
                die();
            }
        } else {
            ((RPGWorld) world).addChatBox("Adlez: I should talk to someone before fighting this.", this);
        }

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
        hasHadFreeMove = hasHadFreeMove || collisions.size() == 0;
        for (int i = 0; i < collisions.size(); i++) {
            GameObject obj = collisions.get(i);

            Const.Type type = Const.Type.values()[obj.type()];

            switch (type) {
                case AVATAR:
                    isMoving = false;
                    state = State.STUCK;
                    moveBack();
                    if (hitCooldown < 0) {
                        ((Avatar)obj).hit();
                        hitCooldown = hitCooldownSeconds;
                    }
                    break;
                case NPC:
                case GOBLIN:
                    if (!hasHadFreeMove) { break; }
                    isMoving = false;
                    state = State.STUCK;
                    moveBack();
                    break;
                case TREE:
                case WALL:
                case WATER:
                case LAVA:
                    isMoving = false;
                    state = State.STUCK;
                    moveBack();
                    break;
            }
        }

    }


}
