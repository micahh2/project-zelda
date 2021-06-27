
// (c) Thorsten Hasbargen

package projectzelda.game;

import projectzelda.engine.*;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Avatar extends CircularGameObject {
    private boolean flippedX = false;
    private HashMap<String, GameObject> inventory;
    public WeaponState weaponState;

    private Sword sword;
    private Bow bow;

    private ChatBoxButton chatBox;
    private String chatBoxText;

    // place of chatbox
    private int posXChatBox = world.worldInfo.getPartWidth() / 2 - 300;
    private int posYChatBox = world.worldInfo.getPartHeight() - 100;

    private int counterBones = 0;
    double timeToDie = 2.0;
    boolean dying = false;

    public double life = 1.0;
    public HealthBar healthBar;
    Arrow.Dir fireDir = Arrow.Dir.RIGHT;


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
        this.imageRef = imageRef;
        this.sword = sword;
        this.bow = bow;
        weaponState = WeaponState.NONE;
        bow.offset((int) x, (int) y);
        sword.offset((int) x, (int) y);
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
                // if Object is a tree, move back one step
                case TREE:
                case WATER:
                case ROCK:
                case WALL:
                case NPC:
                case ANIMAL:
                case CHEST:
                case PUMPKIN:
                    this.moveBack();
                    break;
                case LAVA:
                    hit(0.1);
                    this.moveBack();
                    break;
                case GOBLIN:
                    this.moveBack();
                    if (weaponState == WeaponState.NONE) {
                        world.gameState = GameState.DIALOG;
                        chatBox = new ChatBoxButton(posXChatBox, posYChatBox, 600, 100, "I'm going to need a weapon", Const.Type.GOBLIN);
                        world.chatBoxObjects.add(chatBox);
                    }
                    break;

                // pick up Bones
                case BONES:
                    hit(-0.25);

                    counterBones++;
                    chatBox = new ChatBoxButton(posXChatBox, posYChatBox, 600, 100, "Bones picked up", Const.Type.BONES);
                    world.chatBoxObjects.add(chatBox);
                    if (q == QuestState.OLGA_MONSTERS && counterBones >= 6) {
                        ((RPGWorld) world).nextQuest();
                    }
                    obj.isLiving = false;
                    break;
            }
        }

        // Hacky, but we can flip the orientation of the avatar by switching the image coordinates to draw from
        int endx = (int) x;
        int endy = (int) y;

        if ((startx < endx && !flippedX) || (startx > endx && flippedX)) {
            int tempx = imageRef.x1;
            imageRef.x1 = imageRef.x2;
            imageRef.x2 = tempx;
            flippedX = !flippedX;
            bow.flip();
            sword.flip();
        }

        int diffx = endx - startx;
        int diffy = endy - starty;
        sword.offset(diffx, diffy);
        bow.offset(diffx, diffy);
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

    public void interactWithNpc() {
        List<GameObject> interactiveObjects = 
            world.gameObjects.stream().filter(obj -> obj instanceof NPC ||
                obj instanceof Chest || obj instanceof Pumpkin).collect(Collectors.toList());

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
        }
    }


    public boolean containsItem(String itemType) {
        return inventory.containsKey(itemType);
    }


    public void addItem(String itemType, GameObject item) {
        inventory.put(itemType, item);
    }

    public void questPumpkin(Pumpkin pumpkin) {
        world.gameState = GameState.DIALOG;
        QuestState q = ((RPGWorld) world).questState;
        if (q == QuestState.STEVE_IN_PROGRESS) {
            pumpkin.setPumpkinText(pumpkin.getPumpkinQuestText());
            chatBoxText = pumpkin.getPumpkinText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, pumpkin);
            pumpkin.isLiving = false;
            System.out.println("Next! " + ((RPGWorld) world).questState);
            ((RPGWorld) world).nextQuest();
        } else {
            chatBoxText = pumpkin.getPumpkinText(0);
            ((RPGWorld) world).addChatBox(chatBoxText, pumpkin);
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
            weaponState = WeaponState.SWORD;
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

        if (weaponState == WeaponState.SWORD) {
            sword.draw(gs, tick);
        } else if (weaponState == WeaponState.BOW) {
            bow.draw(gs, tick);
        }
        gs.draw(this);
    }

    public void switchWeapon(WeaponState weaponState){
        this.weaponState = weaponState;
    }

    public void fire() {
        if (weaponState == WeaponState.BOW) {
            bow.fire(fireDir);
        }
        if (weaponState == WeaponState.SWORD) {
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
}
