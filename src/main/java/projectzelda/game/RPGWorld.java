
// (c) Thorsten Hasbargen

package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;
import projectzelda.map.MapObject;

import java.util.List;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;


public class RPGWorld extends World {

    private double timePassed = 0;
    private double timeSinceLastShot = 0;

    // for grenades
    private int grenades = 5;
    private Counter counterG;
    private Counter counterB;
    private HelpText helpText;


    private double spawnGrenade = 0;
    private projectzelda.map.Map map;

    private double lifeHelpText = 10.0;
    private double lifeChatBox = 2.0;

    private Sound sound = new Sound("/music/Forest_Ventures.wav");

    public RPGWorld(projectzelda.map.Map map) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.map = map;
        worldInfo = map; // Implements world dim
        physicsSystem = new RPGPhysicsSystem(this);
    }

    public void init() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        // add the Avatar
        MapObject playerMO = map.getFirstObject("Player");
        MapObject sword = map.getFirstObject("Swords");
        avatar = new Avatar(playerMO.x, playerMO.y, playerMO.imageRef, sword.imageRef);
        // avatar = new Avatar(100, 50, new ImageRef("Rocks2", 0, 0, 32, 32));
        gameObjects.add(avatar);

        MapObject bossMo = map.getFirstObject("Boss");
        boss = new BossNMonster(bossMo.x, bossMo.y, bossMo.imageRef, (Avatar)avatar);
        gameObjects.add(boss);

        MapObject chestMo = map.getFirstObject("Chests");
        int widthC = chestMo.startingBounds.x2 - chestMo.startingBounds.x1;
        int heightC = chestMo.startingBounds.y2 - chestMo.startingBounds.y1;
        chest = new Chest(chestMo.x, chestMo.y, widthC, heightC, chestMo.imageRef);
        gameObjects.add(chest);

        MapObject pumpkinMo = map.getFirstObject("Pumpkin");
        int widthP = pumpkinMo.startingBounds.x2 - pumpkinMo.startingBounds.x1;
        int heightP = pumpkinMo.startingBounds.y2 - pumpkinMo.startingBounds.y1;
        pumpkin = new Pumpkin(pumpkinMo.x, pumpkinMo.y, widthP, heightP, pumpkinMo.imageRef);
        gameObjects.add(pumpkin);


        //play the background music
        sound.playBackgroundMusic();
        sound.setVolume(-20.0F);

        // set WorldPart position
        worldPartX = 0;
        worldPartY = 0;


        // create houses and trees
        List<MapObject> houses = map.getAllObjects("Housebases");
        for(MapObject house : houses) {
            int width = house.startingBounds.x2 - house.startingBounds.x1;
            int height = house.startingBounds.y2 - house.startingBounds.y1;
            gameObjects.add(new House(house.startingBounds.x1, house.startingBounds.y1, width, height));
        }

        List<MapObject> walls = map.getAllObjects("Walls");
        for(MapObject wall : walls) {
            int width = wall.startingBounds.x2 - wall.startingBounds.x1;
            int height = wall.startingBounds.y2 - wall.startingBounds.y1;
            gameObjects.add(new Wall(wall.startingBounds.x1, wall.startingBounds.y1, width, height));
        }

        List<MapObject> waters = map.getAllObjects("Water");
        for(MapObject water : waters) {
            int width = water.startingBounds.x2 - water.startingBounds.x1;
            int height = water.startingBounds.y2 - water.startingBounds.y1;
            gameObjects.add(new Water(water.startingBounds.x1, water.startingBounds.y1, width, height));
        }

        List<MapObject> lavas = map.getAllObjects("Lava");
        for(MapObject lava : lavas) {
            int width = lava.startingBounds.x2 - lava.startingBounds.x1;
            int height = lava.startingBounds.y2 - lava.startingBounds.y1;
            gameObjects.add(new Lava(lava.startingBounds.x1, lava.startingBounds.y1, width, height));
        }

        List<MapObject> npcs = map.getAllObjects("Npcs");
        for(MapObject npc : npcs) {
            int width = npc.startingBounds.x2 - npc.startingBounds.x1;
            int height = npc.startingBounds.y2 - npc.startingBounds.y1;
            gameObjects.add(new Npc(npc.startingBounds.x1, npc.startingBounds.y1, width, height, npc.imageRef));
        }

        List<MapObject> trees = map.getAllObjects("Treebases");
        for(MapObject tree : trees){
            int radius = Math.round((tree.startingBounds.x2 - tree.startingBounds.x1)/2);
            gameObjects.add(new Tree(tree.startingBounds.x1, tree.startingBounds.y1, radius));
        }

        // create some monsters!
        List<MapObject> monsters = map.getAllObjects("Monsters");
        for(MapObject monster : monsters){
            gameObjects.add(new BossNMonster(monster.startingBounds.x1, monster.startingBounds.y1, monster.imageRef, (Avatar)avatar));
        }

        // got have rock
        MapObject rocks = map.getFirstObject("Rocks");
        gameObjects.add(new Rock(rocks.startingBounds.x1, rocks.startingBounds.y1));


        counterB = new Counter("Bones: ", 20, 40);
        counterG = new Counter("Grenades: ", 770, 40);
        helpText = new HelpText(400, 450);


        counterG.setNumber(grenades);
        textObjects.add(counterB);
        textObjects.add(counterG);
        textObjects.add(helpText);

        // calculate relative ui button width and height
        int buttonWidth = (int) (0.2 * worldInfo.getPartWidth());
        int buttonHeight = (int) (0.1 * worldInfo.getPartHeight());

        // add the pause menu buttons
        int relX = (int)(0.4 * worldInfo.getPartWidth());
        int relY = (int)(0.3 * worldInfo.getPartHeight());

        pauseMenuObjects.add(new UIButton(relX, relY, buttonWidth, buttonHeight, "Resume"));
        relY = (int)(0.6 * worldInfo.getPartHeight());
        pauseMenuObjects.add(new UIButton(relX, relY, buttonWidth, buttonHeight, "Quit"));

        // add the main menu buttons
        relY = (int)(0.45 * worldInfo.getPartHeight());
        mainMenuObjects.add(new UIButton(relX, relY, buttonWidth, buttonHeight, "Play"));

        // add the hud elements
        hudObjects.add(((Avatar) avatar).healthBar);

        int itemSlotX = (int)(0.9 * worldInfo.getPartWidth());
        int itemSlotY = (int)(0.045 * worldInfo.getPartHeight());
        int itemSlotRadius = (int)(0.02 * worldInfo.getPartWidth());
        hudObjects.add(new ItemSlot(itemSlotX,itemSlotY, itemSlotRadius, (Avatar) avatar, "GRENADE"));
        itemSlotX = (int)(0.95 * worldInfo.getPartWidth());
        hudObjects.add(new ItemSlot(itemSlotX,itemSlotY, itemSlotRadius, (Avatar) avatar, "BONES"));

    }

    public void processUserInput(UserInput userInput, double diffSeconds) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        // distinguish if Avatar shall move or shoots	  
        int button = userInput.mouseButton;


        //
        // Mouse events
        //
        // if (userInput.isMouseEvent) {
        //     // move
        //     if(button==1) { 
        //         avatar.setDestination(userInput.mousePressedX+worldPartX, 
        //             userInput.mousePressedY+worldPartY);
        //     }
        // }

        //
        // Mouse still pressed?
        //
        if (userInput.isMousePressed && button == 1) {
            if (this.gameState == GameState.PAUSE) {
                UIButton resumeButton = (UIButton) pauseMenuObjects.get(0);
                UIButton quitButton = (UIButton) pauseMenuObjects.get(1);

                if (userInput.mouseMovedX >= resumeButton.x && userInput.mouseMovedX <= resumeButton.getMaxX()
                        && (userInput.mouseMovedY >= resumeButton.y && userInput.mouseMovedY <= resumeButton.getMaxY())) {
                    gameState = GameState.PLAY;
                }

                if (userInput.mouseMovedX >= quitButton.x && userInput.mouseMovedX <= quitButton.getMaxX()
                        && (userInput.mouseMovedY >= quitButton.y && userInput.mouseMovedY <= quitButton.getMaxY())) {
                    System.exit(0);
                }

            } else {
                // only 1 shot every ... seconds:
                timeSinceLastShot += diffSeconds;
                if (timeSinceLastShot > 0.2) {
                    timeSinceLastShot = 0;


                    Shot shot = new Shot(
                            avatar.x, avatar.y, userInput.mouseMovedX + worldPartX, userInput.mouseMovedY + worldPartY);
                    this.gameObjects.add(shot);
                }
            }
        }


        UIButton playButton = (UIButton) mainMenuObjects.get(0);

        if (userInput.isMousePressed) {
            if (userInput.mouseMovedX >= playButton.x && userInput.mouseMovedX <= playButton.getMaxX()
                    && (userInput.mouseMovedY >= playButton.y && userInput.mouseMovedY <= playButton.getMaxY())) {
                gameState = GameState.PLAY;
            }
        }


        //
        // Keyboard events
        //
        if (userInput.isKeyEvent) {
            switch (userInput.keyPressed) {
                case ' ':
                    throwGrenade(userInput.mouseMovedX + worldPartX, userInput.mouseMovedY + worldPartY);
                    ((Avatar) avatar).removeItem("GRENADE");
                    Sound sword = new Sound("/music/sword-sound-1_16bit.wav");
                    sword.setVolume(-30.0f);
                    sword.playSound();
                    break;
                case 'q':
                    System.exit(0);
                    break;
                case (char) 27:

                    if (this.gameState != GameState.PAUSE) {
                        this.gameState = GameState.PAUSE;
                        sound.setVolume(-40.0f);
                    } else {
                        //sound.setVolume(-20.0f);
                        this.gameState = GameState.PLAY;
                        sound.setVolume(-20.0f);

                    }

                    //this.gameState = this.gameState == GameState.PAUSE ? GameState.PLAY : GameState.PAUSE;
                    break;

                case 'w':
                case 'a':
                case 's':
                case 'd':
                    break;
                default:
                    System.out.println("Unknown key code " + userInput.keyPressed);
                    break;
            }

        }
        int vert = 0;
        int horz = 0;
        if (userInput.keysPressed.contains('w')) {
            vert -= 10;

        }
        if (userInput.keysPressed.contains('a')) {
            horz -= 10;
        }
        if (userInput.keysPressed.contains('s')) {
            vert += 10;
        }
        if (userInput.keysPressed.contains('d')) {
            horz += 10;
        }
        // Move character
        if (horz != 0 || vert != 0) {
            avatar.setDestination(avatar.x + horz, avatar.y + vert);
        }


    }


    private void throwGrenade(double x, double y) {
        if (grenades <= 0) return;

        // throw grenade
        for (int i = 0; i < 2000; i++) {
            double alfa = Math.random() * Math.PI * 2;
            double speed = 50 + Math.random() * 200;
            double time = 0.2 + Math.random() * 0.4;
            Shot shot = new Shot(x, y, alfa, speed, time);
            this.gameObjects.add(shot);
        }

        // inform counter
        grenades--;
        counterG.setNumber(grenades);
    }


    public void createNewObjects(double diffSeconds) {
        // createZombie(diffSeconds);
        createGrenade(diffSeconds);

        // delete HelpText after ... seconds
        if (helpText != null) {
            lifeHelpText -= diffSeconds;
            if (lifeHelpText < 0) {
                textObjects.remove(helpText);
                helpText = null;
            }
        }


    }


    private void createGrenade(double diffSeconds) {
        final double INTERVAL = Const.SPAWN_GRENADE;

        spawnGrenade += diffSeconds;
        if (spawnGrenade > INTERVAL) {
            spawnGrenade -= INTERVAL;

            // create new Grenade
            double x = worldPartX + Math.random() * worldInfo.getPartWidth();
            double y = worldPartY + Math.random() * worldInfo.getPartHeight();

            // if too close to Avatar, cancel
            double dx = x - avatar.x;
            double dy = y - avatar.y;
            if (dx * dx + dy * dy < 200 * 200) {
                spawnGrenade = INTERVAL;
                return;
            }


            // if collisions occur, cancel
            Grenade grenade = new Grenade(x, y);
            GameObjectList list = getPhysicsSystem().getCollisions(grenade);
            if (list.size() != 0) {
                spawnGrenade = INTERVAL;
                return;
            }

            // else add zombie to world
            this.gameObjects.add(grenade);
            counterG.setNumber(grenades);
        }

    }

    public void addGrenade() {
        if (grenades < 999) {
            grenades++;
        }
        counterG.setNumber(grenades);
    }

    public void addBones() {
        counterB.increment();
    }
}
