package projectzelda.game;

import projectzelda.engine.*;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map.Entry;

public class Avatar extends CircularGameObject {
    public boolean flippedX = false;
    private HashMap<String, GameObject> inventory;


    private Sword sword;
    private Bow bow;

    private ChatBoxButton chatBox;
    private String chatBoxText;

    private int counterBones = 0;
    double timeToDie = 2.0;
    public boolean dying = false;

    public double life = 1.0;
    public HealthBar healthBar;
    Arrow.Dir fireDir = Arrow.Dir.RIGHT;

    double ELEMENT_HIT_COOLDOWN = 0.2;
    double elementHitTimeout = 0;

    public Avatar(double x, double y, ImageRef imageRef, Sword sword, Bow bow) {
        super(x, y, 0, 200, 15, new Color(96, 96, 255));

        this.isMoving = false;

        inventory = new HashMap<>();

        //imageRef = new ImageRef("Rocks2", 0, 0, 32, 32);
        int healthBarWidth = (int) (0.3 * world.worldInfo.getPartWidth());
        int healthBarHeight = (int) (0.03 * world.worldInfo.getPartHeight());
        int healthBarX = (int) (0.005 * world.worldInfo.getPartWidth());
        int healthBarY = (int) (0.01 * world.worldInfo.getPartHeight());
        healthBar = new HealthBar(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
        healthBar.world = world;
        healthBar.isHudElement = true;
        this.imageRef = imageRef.clone();
        this.sword = sword;
        this.bow = bow;
        //weaponState = ((RPGWorld) world).weaponState;
        bow.setXY(x, y);
        sword.setXY(x, y);
    }

    @Override
    public void move(double diffSeconds) {
        if (dying) {
            if (timeToDie < 0) {
                isLiving = false;
                world.gameState = GameState.DEATH;
                return;
            }
            timeToDie -= diffSeconds;
            return;
        }

        if (sword.weaponTemp > 0) {
            sword.weaponTemp -= diffSeconds;
        }
        if (elementHitTimeout > 0) {
            elementHitTimeout -= diffSeconds;
        }

        // Save starting x-pos for calculating orientation
        int startx = (int) x;
        int starty = (int) y;

        // move Avatar one step forward
        super.move(diffSeconds);

        // calculate all collisions with other Objects 
        GameObjectList collisions = world.getPhysicsSystem().getCollisions(this);
        for (int i = 0; i < collisions.size(); i++) {
            GameObject obj = collisions.get(i);
            QuestState q = ((RPGWorld) world).questState;
            Const.Type type = Const.Type.values()[obj.type()];
            switch (type) {
                case ANIMAL:
                case NPC:
                case PUMPKIN:
                case CHEST:
                case ROCK:
                    ((RPGWorld)world).addNotification("Press 'E' to interact");
                    this.moveBack();
                    break;
                case TREE:
                case WATER:
                case WALL:

                    this.moveBack();
                    break;
                case LAVA:
                    if (elementHitTimeout <= 0) {
                        hit(0.1);
                        elementHitTimeout = ELEMENT_HIT_COOLDOWN;
                    }
                    this.moveBack();
                    break;
                case GOBLIN:
                    this.moveBack();
                    if (((RPGWorld)world).weaponState == WeaponState.NONE) {
                        ((RPGWorld)world).addChatBox("Adlez: I should talk to someone before fighting this.", obj);
                    }
                    break;

                    // pick up Bones
                case BONES:
                    hit(-0.25); // Heal some
                    counterBones++;
                    ((RPGWorld)world).addNotification("Bones picked up");
                    obj.isLiving = false;
                    break;
            }
        }

        // Hacky, but we can flip the orientation of the avatar by switching the image coordinates to draw from
        int endx = (int) x;
        int endy = (int) y;

        if ((startx < endx && !flippedX) || (startx > endx && flippedX)) { flip(); }

        sword.setXY(x, y);
        bow.setXY(x, y);

        int diffx = endx - startx;
        int diffy = endy - starty;

        if (diffx < 0 && diffy == 0) {
            fireDir = Arrow.Dir.LEFT;
        } else if (diffx < 0 && diffy < 0) {
            fireDir = Arrow.Dir.UP_LEFT;
        } else if (diffx < 0 && diffy > 0) {
            fireDir = Arrow.Dir.DOWN_LEFT;
        } else if (diffx > 0 && diffy == 0) {
            fireDir = Arrow.Dir.RIGHT;
        } else if (diffx > 0 && diffy < 0) {
            fireDir = Arrow.Dir.UP_RIGHT;
        } else if (diffx > 0 && diffy > 0) {
            fireDir = Arrow.Dir.DOWN_RIGHT;
        } else if (diffx == 0 && diffy < 0) {
            fireDir = Arrow.Dir.UP;
        } else if (diffx == 0 && diffy > 0) {
            fireDir = Arrow.Dir.DOWN;
        }
    }

    public void flip() {
        int tempx = imageRef.x1;
        imageRef.x1 = imageRef.x2;
        imageRef.x2 = tempx;
        flippedX = !flippedX;
        bow.flip();
        sword.flip();
    }

    public void interactWithNpc() {
        List<GameObject> interactiveObjects = 
            world.gameObjects.stream().filter(obj -> obj instanceof NPC ||
                obj instanceof Chest || obj instanceof Pumpkin || obj instanceof Rock).collect(Collectors.toList());

        GameObject closestObject = interactiveObjects.get(0);
        double shortestDistance = Double.MAX_VALUE;
        for (GameObject obj : interactiveObjects) {
            double objX = obj.x;
            double objY = obj.y;
            if (obj instanceof RectangularGameObject) {
                objX += ((RectangularGameObject)obj).width / 2;
                objY += ((RectangularGameObject)obj).height / 2;
            }
            double distance = world.physicsSystem.distance(x, y, objX, objY);
            if (shortestDistance > distance) {
                closestObject = obj;
                shortestDistance = distance;
            }
        }

        switch (Const.Type.values()[closestObject.type()]) {
            case NPC:
            case ANIMAL:
                if (shortestDistance <= 40) {
                    questNPC((NPC) closestObject);
                }
                break;
            case CHEST:
                if (shortestDistance <= 35) {
                    questChest((Chest) closestObject);
                }
                break;
            case PUMPKIN:
                if (shortestDistance <= 30) {
                    questPumpkin((Pumpkin) closestObject);
                }
                break;
            case ROCK:
                if (shortestDistance <= 100) {
                   questRock((Rock) closestObject);
                }
        }
    }


    public boolean containsItem(String itemType) {
        return inventory.containsKey(itemType);
    }


    public void teleport(int toX, int toY) {
        bow.setXY(toX, toY);
        sword.setXY(toX, toY);
        x = toX;
        y = toY;
        destX = toX;
        destY = toY;
    }

    public void addItem(String itemType, GameObject item) {
        inventory.put(itemType, item);
    }

    public void addBow() {
        addItem("BOW", bow);
    }

    public void addSword() {
        addItem("SWORD", sword);
    }

    public void questPumpkin(Pumpkin pumpkin) {
        world.gameState = GameState.DIALOG;
        QuestState q = ((RPGWorld) world).questState;
        if (q == QuestState.STEVE_IN_PROGRESS) {
            pumpkin.setPumpkinText(pumpkin.getPumpkinQuestText());
            chatBoxText = pumpkin.getPumpkinText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, pumpkin);
            pumpkin.isLiving = false;
            life = 1.0;
            healthBar.health = life;
            System.out.println("Next! " + ((RPGWorld) world).questState);
            ((RPGWorld) world).nextQuest();
        } else {
            chatBoxText = pumpkin.getPumpkinText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, pumpkin);
        }
    }
    public void questRock(Rock rock) {
        world.gameState = GameState.DIALOG;
        QuestState q = ((RPGWorld) world).questState;
        if (q != QuestState.BOSS) {
            chatBoxText = rock.getRockText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, rock);
        } else if (q == QuestState.BOSS) {
          rock.setRockText(rock.getRockQuestText());
          chatBoxText = rock.getRockText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, rock);

        }
    }
    public void questChest(Chest chest) {
        world.gameState = GameState.DIALOG;
        QuestState q = ((RPGWorld) world).questState;
        if (q == QuestState.OLGA_SWORD_SEARCH) {
            chest.setChestText(chest.getChestQuestText());
            chatBoxText = chest.getChestText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, chest);
            chest.isLiving = false;
            ((RPGWorld)world).weaponState = WeaponState.SWORD;
            addItem("SWORD", sword);
            System.out.println("Next! " + ((RPGWorld) world).questState);
            ((RPGWorld) world).nextQuest();
        } else {
            chatBoxText = chest.getChestText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, chest);
        }
    }

    public void questNPC(NPC npc) {
        world.gameState = GameState.DIALOG;
        QuestState q = ((RPGWorld) world).questState;
        chatBoxText = npc.getNpcQuestText(q, 0);
        ((RPGWorld) world).addChatBox(chatBoxText, npc);
    }

    public void draw(GraphicSystem gs, long tick) {
        if (dying) { return; }

        if (((RPGWorld)world).weaponState == WeaponState.SWORD) {
            sword.draw(gs, tick);
        }         
        gs.draw(this);
        if (((RPGWorld)world).weaponState == WeaponState.BOW) {
            bow.draw(gs, tick);
        }

    }

    public void switchWeapon(WeaponState weaponState){
        ((RPGWorld)world).weaponState = weaponState;
    }

    public HashMap<String, GameObject> getInventory() { return inventory; }
    public void setInventory(HashMap<String, GameObject> in) { inventory = in; }

    public void fire() {
        if (((RPGWorld)world).weaponState == WeaponState.BOW) {
            bow.fire(fireDir);
        }
        if (((RPGWorld)world).weaponState == WeaponState.SWORD) {
            sword.fire();
        }
    }

    public int type() {
        return Const.Type.AVATAR.ordinal();
    }

    public void hit(double val) {
        if (dying) { return; }
        // every hit decreases life
        life -= val;
        life = Math.min(1.0, life);
        healthBar.health = life;

        if (life <= 0) {
            die();
        }
    }

    public void hit() {
        hit(0.05);
    }

    public void die() {
        dying = true;
        ((RPGWorld) world).throwGrenade(x, y);
    }

    public GameObject clone() {
        Sword s = (Sword)sword.clone();
        Bow b = (Bow)bow.clone();
        Avatar a = new Avatar(x, y, imageRef.clone(), s, b);
        super.setClone(a);
        setClone(a);

        HashMap<String, GameObject> inv = new HashMap<>();
        for (Entry<String, GameObject> entry : inventory.entrySet()) {
            if (entry.getValue() == sword) {
                inv.put(entry.getKey(), s);
                continue;
            }
            if (entry.getValue() == bow) {
                inv.put(entry.getKey(), b);
                continue;
            }
            System.out.println("ERROR Cloning Avatar: " + entry);
        }
        a.inventory = inv;

        return a;
    }
    public void setClone(Avatar a) {
        super.setClone(a);
        a.dying = dying;
        a.life = life;
        a.flippedX = flippedX;
    }
}
