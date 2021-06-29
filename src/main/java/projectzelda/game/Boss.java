package projectzelda.game;

import projectzelda.engine.*;

public class Boss extends EnemyAI
{

    public double voidCannonCooldown = 2.2;
    public double VOID_CANNON_THRESHOLD = 1;
    public double voidCannonTemp = 0;

    public double monsterCannonCooldown = 3.2;
    public double MONSTER_CANNON_THRESHOLD = 1;
    public double monsterCannonTemp = 0;

    public double teleCannonCooldown = 2.2;
    public double TELE_CANNON_THRESHOLD = 1;
    public double teleCannonTemp = 0;
    public int teleportIndex = 0;

    public double lineOfSight = 400;

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

    public void fireVoidCannon() {
        if (voidCannonTemp > VOID_CANNON_THRESHOLD) { return; }
        world.gameObjects.add(new VoidOrb(x, y, target.x, target.y, this));
        voidCannonTemp += voidCannonCooldown;
    }
    public void fireMonsterCannon() {
        if (monsterCannonTemp > MONSTER_CANNON_THRESHOLD) { return; }
        world.gameObjects.add(new MonsterOrb(x, y, target));
        monsterCannonTemp += monsterCannonCooldown;
    }
    public void fireTeleCannon() {
        if (teleCannonTemp > TELE_CANNON_THRESHOLD) { return; }
        world.gameObjects.add(new TeleportOrb(x, y, target.x, target.y, teleportIndex));
        teleportIndex++;
        teleCannonTemp +=teleCannonCooldown;
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
            if (life < 0.3) {
                fireVoidCannon();
                fireMonsterCannon();
                fireTeleCannon();
            } else if (life < 0.6) {
                fireTeleCannon();
                fireVoidCannon();
            } else if (life < 0.7) {
                fireVoidCannon();
                fireMonsterCannon();
            } else if (life < 0.8) {
                fireMonsterCannon();
            } else if (life < 0.9) {
                fireTeleCannon();
            } else if (life < 1) {
                fireVoidCannon();
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

        life -= 0.08;
        healthBar.health = life;
        colorCooldown = COLOR_COOLDOWN;

        if (life <= 0) { die(); }
    }

    public void die() {
        this.isLiving = false;
        ((RPGWorld)world).addBones(x, y);
        if (!((Avatar)world.avatar).dying) {
            world.gameState = GameState.COMPLETE;
        }
    }

    public GameObject clone() {
        Boss b = new Boss(x, y, imageRef, target);
        setClone(b);
        return b;
    }

    public void setClone(Boss b) {
        super.setClone(b);
        b.voidCannonCooldown = voidCannonCooldown;
        b.voidCannonTemp = voidCannonTemp;

        b.monsterCannonCooldown = monsterCannonCooldown;
        b.monsterCannonTemp = monsterCannonTemp;

        b.teleCannonCooldown = teleCannonCooldown;
        b.teleCannonTemp = teleCannonTemp;
        b.teleportIndex = teleportIndex;

        b.lineOfSight = lineOfSight;
    }
}


