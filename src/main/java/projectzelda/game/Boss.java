package projectzelda.game;

import projectzelda.engine.*;

public class Boss extends EnemyAI
{

    public GameObject target;

    double voidCannonCooldown = 2.2;
    double VOID_CANNON_THRESHOLD = 1;
    double voidCannonTemp = 0;

    double monsterCannonCooldown = 3.2;
    double MONSTER_CANNON_THRESHOLD = 1;
    double monsterCannonTemp = 0;

    double teleCannonCooldown = 2.2;
    double TELE_CANNON_THRESHOLD = 1;
    double teleCannonTemp = 0;

    double lineOfSight = 400;

    public Boss(double x, double y, ImageRef imageRef, GameObject target)
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
        if (colorCooldown > 0 && tick % 3 == 0) {
            int shake = (int)(Math.random()*10)+3;
            gs.setWorldOffset(shake, shake);
        } else {
            gs.setWorldOffset(0, 0);
        }
        gs.draw(this);
        healthBar.draw(gs, tick);
    }

    public void move(double diffSeconds) {
        if (voidCannonTemp >= 0) { voidCannonTemp -= diffSeconds; }
        if (monsterCannonTemp >= 0) { monsterCannonTemp -= diffSeconds; }
        if (teleCannonTemp >= 0) { teleCannonTemp -= diffSeconds; }
        if (colorCooldown >= 0) { colorCooldown -= diffSeconds; }

        // Close to goal, pick a new one
        if (readyForNewDestination()) {
            Point<Double> newDest = newDestination();
            state = State.FREE;
            setDestination(newDest.x, newDest.y);
        }

        double dist = world.getPhysicsSystem().distance(x, y, target.x, target.y);
        if (dist < lineOfSight) {
            if (life < 1 && voidCannonTemp < VOID_CANNON_THRESHOLD) {
                world.gameObjects.add(new VoidOrb(x, y, target.x, target.y, this));
                voidCannonTemp += voidCannonCooldown;
            }
            if (life < 0.7 && monsterCannonTemp < MONSTER_CANNON_THRESHOLD) {
                world.gameObjects.add(new MonsterOrb(x, y, target));
                monsterCannonTemp += monsterCannonCooldown;
            }
            if (life < 0.3 && teleCannonTemp < TELE_CANNON_THRESHOLD) {
                world.gameObjects.add(new TeleportOrb(x, y, target.x, target.y));
                teleCannonTemp +=teleCannonCooldown;
            }
        }

        super.move(diffSeconds);
        adjustHealthBarPosition();

        // handle collisions of the zombie
        GameObjectList collisions = world.getPhysicsSystem().getCollisions(this);
        for (int i = 0; i < collisions.size(); i++) {
            GameObject obj = collisions.get(i);

            Const.Type type = Const.Type.values()[obj.type()];

            // if object is target, we're being attacked
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
        voidCannonCooldown = Math.max(voidCannonCooldown-0.1, 0.4);
        if (life < 0.7) {
            monsterCannonCooldown = Math.max(monsterCannonCooldown-0.1, 0.4);
        }
        if (life < 0.3) {
            teleCannonCooldown = Math.max(teleCannonCooldown-0.1, 0.4);
        }

        lineOfSight = Math.min(lineOfSight*1.1, 1000);

        if (((RPGWorld)world).weaponState == WeaponState.SWORD) {

            world.gameState = GameState.DIALOG;
            ((RPGWorld)world).addChatBox("Adlez: His armour is too thick!", this);
        } else {
            life -= 0.08;
            healthBar.health = life;
            colorCooldown = COLOR_COOLDOWN;

            if (life <= 0) { die(); }
        }

    }

    public void die() {
        this.isLiving = false;
        ((RPGWorld)world).addBones(x, y);
        if (!((Avatar)world.avatar).dying) {
            world.gameState = GameState.COMPLETE;
        }
    }
}


