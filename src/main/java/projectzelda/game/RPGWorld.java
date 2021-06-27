package projectzelda.game;

import projectzelda.engine.*;
import projectzelda.map.MapObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RPGWorld extends World {

    private double timePassed = 0;
    private double timeSinceLastShot = 0;

    private boolean isMusicPlaying = false;

    private HelpText helpText;


    private double spawnGrenade = 0;
    private projectzelda.map.Map map;

    private double lifeHelpText = 10.0;


    private Sound sound = new Sound("/music/Forest_Ventures.wav");

    QuestState questState = QuestState.START;

    private int chatTrack = 1;
    private boolean isEnterKeyPressed = false;

    private List<MapObject> npcs = new ArrayList<>();

    public RPGWorld(projectzelda.map.Map map) {
        this.map = map;
        worldInfo = map; // Implements world dim
        physicsSystem = new RPGPhysicsSystem(this);
    }

    ImageRef bonesImage;
    ImageRef monsterImage;

    public void init() {

        //play the background music
        sound.playBackgroundMusic();
        sound.setVolume(-20.0F);

        // add the Avatar
        MapObject playerMO = map.getFirstObject("Player");
        MapObject swordMO = map.getFirstObject("Swords");
        ImageRef arrow = map.getFirstObject("Arrow").imageRef;
        List<ImageRef> bowFrames = map.getAllObjectImageRefs("Bow");
        ImageRef swordSwing = map.getFirstObject("Swing").imageRef;
        Bow bow = new Bow(bowFrames, arrow);
        Sword sword = new Sword(swordMO.imageRef, swordSwing);
        avatar = new Avatar(playerMO.x, playerMO.y, playerMO.imageRef, sword, bow);

        // avatar = new Avatar(100, 50, new ImageRef("Rocks2", 0, 0, 32, 32));
        StartAnimation startAnimation = new StartAnimation(playerMO.x, playerMO.y, Color.WHITE);
        gameObjects.add(startAnimation);
        gameObjects.add(avatar);

        bonesImage = map.getFirstObject("Bones").imageRef;

        MapObject bossMo = map.getFirstObject("Boss");
        Boss boss = new Boss(bossMo.x, bossMo.y, bossMo.imageRef, avatar);
        gameObjects.add(boss);

        MapObject chestMo = map.getFirstObject("Chests");
        int widthC = chestMo.startingBounds.x2 - chestMo.startingBounds.x1;
        int heightC = chestMo.startingBounds.y2 - chestMo.startingBounds.y1;
        Chest chest = new Chest(chestMo.x, chestMo.y, widthC, heightC, chestMo.imageRef);
        gameObjects.add(chest);

        MapObject pumpkinMo = map.getFirstObject("Pumpkin");
        int widthP = pumpkinMo.startingBounds.x2 - pumpkinMo.startingBounds.x1;
        int heightP = pumpkinMo.startingBounds.y2 - pumpkinMo.startingBounds.y1;
        Pumpkin pumpkin = new Pumpkin(pumpkinMo.x, pumpkinMo.y, widthP, heightP, pumpkinMo.imageRef);
        gameObjects.add(pumpkin);


        // Get a list of NPCs
        npcs = map.getAllObjects("Npcs");
        double charScale = 0.7;

        MapObject steveMo = npcs.get(0);
        int widthSteve = steveMo.startingBounds.x2 - steveMo.startingBounds.x1;
        int heightSteve = steveMo.startingBounds.y2 - steveMo.startingBounds.y1;
        NPC steve = new SteveNpc(steveMo.x, steveMo.y, (int) (widthSteve * charScale), (int) (heightSteve * charScale), steveMo.imageRef);
        gameObjects.add(steve);

        MapObject catMo = npcs.get(1);
        int widthCat = catMo.startingBounds.x2 - catMo.startingBounds.x1;
        int heightCat = catMo.startingBounds.y2 - catMo.startingBounds.y1;
        CatNpc cat = new CatNpc(catMo.x, catMo.y, (int) widthCat / 2, (int) heightCat / 2, catMo.imageRef);
        gameObjects.add(cat);

        MapObject dogMo = npcs.get(2);
        int widthDog = dogMo.startingBounds.x2 - dogMo.startingBounds.x1;
        int heightDog = dogMo.startingBounds.y2 - dogMo.startingBounds.y1;
        DogNpc dog = new DogNpc(dogMo.x, dogMo.y, (int) widthDog / 2, (int) heightDog / 2, dogMo.imageRef);
        gameObjects.add(dog);

        MapObject brutusMo = npcs.get(3);
        int widthBrutus = brutusMo.startingBounds.x2 - brutusMo.startingBounds.x1;
        int heightBrutus = brutusMo.startingBounds.y2 - brutusMo.startingBounds.y1;
        NPC brutus = new BrutusNpc(brutusMo.x, brutusMo.y, (int) (widthBrutus * charScale), (int) (heightBrutus * charScale), brutusMo.imageRef);
        gameObjects.add(brutus);

        MapObject olgaMo = npcs.get(4);
        int widthOlga = olgaMo.startingBounds.x2 - olgaMo.startingBounds.x1;
        int heightOlga = olgaMo.startingBounds.y2 - olgaMo.startingBounds.y1;
        NPC olga = new OlgaNpc(olgaMo.x, olgaMo.y, (int) (widthOlga * charScale), (int) (heightOlga * charScale), olgaMo.imageRef);
        gameObjects.add(olga);

        MapObject bobMo = npcs.get(5);
        int widthBob = bobMo.startingBounds.x2 - bobMo.startingBounds.x1;
        int heightBob = bobMo.startingBounds.y2 - bobMo.startingBounds.y1;
        NPC bob = new BobNpc(bobMo.x, bobMo.y, (int) (widthBob * charScale), (int) (heightBob * charScale), bobMo.imageRef, cat, dog);
        gameObjects.add(bob);

        // set WorldPart position
        worldPartX = 0;
        worldPartY = 0;


        // create houses and trees
        List<MapObject> houses = map.getAllObjects("Housebases");
        for (MapObject house : houses) {
            int width = house.startingBounds.x2 - house.startingBounds.x1;
            int height = house.startingBounds.y2 - house.startingBounds.y1;
            gameObjects.add(new House(house.startingBounds.x1, house.startingBounds.y1, width, height));
        }

        List<MapObject> walls = map.getAllObjects("Walls");
        for (MapObject wall : walls) {
            int width = wall.startingBounds.x2 - wall.startingBounds.x1;
            int height = wall.startingBounds.y2 - wall.startingBounds.y1;
            gameObjects.add(new Wall(wall.startingBounds.x1, wall.startingBounds.y1, width, height));
        }

        List<MapObject> waters = map.getAllObjects("Water");
        for (MapObject water : waters) {
            int width = water.startingBounds.x2 - water.startingBounds.x1;
            int height = water.startingBounds.y2 - water.startingBounds.y1;
            gameObjects.add(new Water(water.startingBounds.x1, water.startingBounds.y1, width, height));
        }

        List<MapObject> lavas = map.getAllObjects("Lava");
        for (MapObject lava : lavas) {
            int width = lava.startingBounds.x2 - lava.startingBounds.x1;
            int height = lava.startingBounds.y2 - lava.startingBounds.y1;
            gameObjects.add(new Lava(lava.startingBounds.x1, lava.startingBounds.y1, width, height));
        }


        List<MapObject> trees = map.getAllObjects("Treebases");
        for (MapObject tree : trees) {
            int radius = Math.round((tree.startingBounds.x2 - tree.startingBounds.x1) / 2);
            gameObjects.add(new Tree(tree.startingBounds.x1, tree.startingBounds.y1, radius));
        }

        // create some monsters!
        List<MapObject> monsters = map.getAllObjects("Monsters");

        for (MapObject monster : monsters) {
            monsterImage = monster.imageRef;
            addMonster(monster.startingBounds.x1, monster.startingBounds.y1);
        }

        // got have rock
        MapObject RockMo = map.getFirstObject("DestroyableRocks");
        Rock rock = new Rock(RockMo.startingBounds.x1, RockMo.startingBounds.y1, RockMo.imageRef);
        gameObjects.add(rock);




        int worldWidth = worldInfo.getPartWidth();
        int worldHeight = worldInfo.getPartHeight();
        helpText = new HelpText((int) (0.15 * worldInfo.getPartWidth()), (int) (0.85 * worldInfo.getPartHeight()));

        textObjects.add(helpText);

        // calculate relative ui button width and height
        int buttonWidth = (int) (0.2 * worldInfo.getPartWidth());
        int buttonHeight = (int) (0.1 * worldInfo.getPartHeight());

        // add the pause menu buttons
        int relX = (int) (0.4 * worldWidth);
        int relY = (int) (0.3 * worldHeight);

        pauseMenuObjects.add(new UIButton(relX, relY, buttonWidth, buttonHeight, "Resume"));
        relY = (int) (0.6 * worldHeight);
        pauseMenuObjects.add(new UIButton(relX, relY, buttonWidth, buttonHeight, "Quit"));

        // add the main menu buttons
        relY = (int) (0.65 * worldHeight);
        mainMenuObjects.add(new UIButton(relX, relY, buttonWidth, buttonHeight, "Play"));
        ImageRef logoRef = new ImageRef("/images/logo.png", 0, 0, 1438, 510);
        mainMenuObjects.add(new UIImage(worldWidth/2-logoRef.x2/2, (int)(worldHeight*0.1), logoRef));

        // add the death menu buttons
        relY = (int) (0.4 * worldHeight);
        deathMenuObjects.add(new UIButton(relX, relY, buttonWidth, buttonHeight, "Restart"));
        relY = (int) (0.6 * worldHeight);
        deathMenuObjects.add(new UIButton(relX, relY, buttonWidth, buttonHeight, "Quit"));

        // add screen upon completion
        relY = (int) (0.65 * worldHeight);
        completeGameMenuObjects.add(new UIButton(relX, relY, buttonWidth, buttonHeight, "You win the game!"));


        // initialize the background and add it to different screens
        Background background = new Background(0, 0, map.getPartWidth(), map.getHeight(), new Color(1f, 1f, 1f, 0.2f));
        pauseMenuObjects.add(background);
        mainMenuObjects.add(background);
        deathMenuObjects.add(background);
        completeGameMenuObjects.add(background);

        // add the hud elements
        hudObjects.add(((Avatar) avatar).healthBar);

        int itemSlotX = (int) (0.31 * worldInfo.getPartWidth());
        int itemSlotY = (int) (0.01 * worldInfo.getPartHeight());
        hudObjects.add(new ItemSlot(itemSlotX, itemSlotY, (Avatar) avatar, "SWORD", sword.imageRef));
        itemSlotX = (int) (0.33 * worldInfo.getPartWidth());
        hudObjects.add(new ItemSlot(itemSlotX, itemSlotY, (Avatar) avatar, "BOW", bow.imageRef));

    }

    public void addMonster(double x, double y) {
        gameObjects.add(new Monster(x, y, monsterImage, avatar));
    }

    public void addBones(double x, double y) {
        gameObjects.addFirst(new Bones(x, y, bonesImage));
    }


    public void processUserInput(UserInput userInput, double diffSeconds) {
        // distinguish if Avatar shall move or shoots	  
        int button = userInput.mouseButton;

        //
        // Mouse events
        //
        // Mouse still pressed?
        //
        if (userInput.isMousePressed && button == 1) {
            if (this.gameState == GameState.PAUSE ||this.gameState == GameState.COMPLETE) {
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
                    //this.gameObjects.add(shot);
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


        if (this.gameState == GameState.DEATH && userInput.isMousePressed) {
            UIButton restartButton = (UIButton) deathMenuObjects.get(0);
            UIButton quitButton = (UIButton) deathMenuObjects.get(1);

            if (userInput.mouseMovedX >= restartButton.x && userInput.mouseMovedX <= restartButton.getMaxX()
                    && (userInput.mouseMovedY >= restartButton.y && userInput.mouseMovedY <= restartButton.getMaxY())) {
                gameState = GameState.MAIN_MENU;
                init();
            }

            if (userInput.mouseMovedX >= quitButton.x && userInput.mouseMovedX <= quitButton.getMaxX()
                    && (userInput.mouseMovedY >= quitButton.y && userInput.mouseMovedY <= quitButton.getMaxY())) {
                System.exit(0);
            }
        }



        if (userInput.isKeyEvent) {
            switch (userInput.keyPressed) {
                case ' ':
                    // go through dialog via spacebar
                    if (!chatBoxObjects.isEmpty()) {
                        ChatBoxButton chatBox = (ChatBoxButton) chatBoxObjects.get(0);
                        handleDialog(chatBox);


                    } else {
                        ((Avatar) avatar).fire();
                    }

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
              /*  case (char) 10:
                    if (!chatBoxObjects.isEmpty()) {
                        ChatBoxButton chatBox = (ChatBoxButton) chatBoxObjects.get(0);
                        handleDialog(chatBox);

                    }
                    break; */
                case 'w':
                case 'a':
                case 's':
                case 'd':

                    /*
                     * exit dialog via movement > problem if moved accidentally on object that gets deleted on collision dialog is lost
                     *                          > if left out accidental dialog is forced upon user upon collision
                     * fixes chatloop > game frozen until wasd pressed after chatbox is cleared
                     * */

                    // can't close menus with movement keys
                 if (gameState != GameState.PAUSE && gameState != GameState.MAIN_MENU && gameState != GameState.COMPLETE) {
                        if (!chatBoxObjects.isEmpty()) {
                            ChatBoxButton chatBoxButton = (ChatBoxButton) chatBoxObjects.get(0);
                            if (chatBoxButton.obj != null) {
                                gameState = GameState.DIALOG;
                            }

                        } else {
                            gameState = GameState.PLAY;
                        }
                    }

                    break;
                case 'e':
                    if (gameState == GameState.PLAY) {
                        ((Avatar) avatar).interactWithNpc();
                    }
                    break;
                case '1':
                    if(((Avatar)avatar).containsItem("SWORD")){
                        ((Avatar)avatar).switchWeapon(WeaponState.SWORD);
                    }
                    break;
                case '2':
                    if(((Avatar)avatar).containsItem("BOW")){
                        ((Avatar)avatar).switchWeapon(WeaponState.BOW);
                    }
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


    public void throwGrenade(double x, double y) {
        // throw grenade
        for (int i = 0; i < 500; i++) {
            double alfa = Math.random() * Math.PI * 2;
            double speed = 50 + Math.random() * 200;
            double time = 0.2 + Math.random() * 7.4;
            Shot shot = new Shot(x, y, alfa, speed, time);
            this.gameObjects.add(shot);
        }
    }


    public void createNewObjects(double diffSeconds) {
        // createZombie(diffSeconds);
        createGrenade(diffSeconds);

        // delete HelpText after ... seconds
        if (helpText != null && gameState == GameState.PLAY) {
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

        }

    }


    public void addChatBox(String text, GameObject obj) {
        int posXChatBox = worldInfo.getPartWidth() / 2 - 300;
        int posYChatBox = worldInfo.getPartHeight() - 100;
        ChatBoxButton chatBox = new ChatBoxButton(posXChatBox, posYChatBox, 600, 100, text, obj);
        chatBoxObjects.add(chatBox);
    }

    public void handleDialog(ChatBoxButton chatBox) {

        // not ideal
        Const.Type type = chatBox.obj != null
                ? Const.Type.values()[chatBox.obj.type()]
                : chatBox.objID;

        switch (type) {
            case CHEST:
                Chest chestForText = (Chest) chatBox.obj;
                if (chestForText.getChestText().length > chatTrack) {
                    chatBox.setText(chestForText.getChestText()[chatTrack]);
                    chatTrack++;
                } else {
                    chatTrack = 1;
                    chatBoxObjects.remove(0);

                }
                break;
            case PUMPKIN:
                Pumpkin pumpkinForText = (Pumpkin) chatBox.obj;
                if (pumpkinForText.getPumpkinText().length > chatTrack) {
                    chatBox.setText(pumpkinForText.getPumpkinText()[chatTrack]);
                    chatTrack++;
                } else {
                    chatTrack = 1;
                    chatBoxObjects.remove(0);

                }
                break;
            case ROCK:
               Rock rockForText = (Rock) chatBox.obj;
                if (rockForText.getRockText().length > chatTrack) {
                    chatBox.setText(rockForText.getRockText()[chatTrack]);
                    chatTrack++;
                } else {
                    chatTrack = 1;
                    chatBoxObjects.remove(0);

                }
                break;

            case NPC:
            case ANIMAL:
                NPC npc = (NPC) chatBox.obj;
                if (npc.getNpcQuestText(questState).length > chatTrack) {
                    chatBox.setText(npc.getNpcQuestText(questState, chatTrack));
                    chatTrack++;
                } else {
                    if (npc.progressFromTalk(questState)) {
                        nextQuest();
                    }
                    if ((questState == QuestState.BOB_IN_PROGRESS_CAT
                            || questState == QuestState.BOB_IN_PROGRESS_CAT)
                            && type == Const.Type.ANIMAL) {
                        npc.setFollow(avatar);
                    }
                    chatTrack = 1;
                    chatBoxObjects.remove(0);
                }
                break;

            case BONES:
            case GOBLIN:
                chatBoxObjects.remove(0);
                break;
        }

    }

    public void nextQuest() {
        int ord = questState.ordinal();
        QuestState old = questState;
        questState = QuestState.values()[ord + 1];
        System.out.println(old + " -> " + questState);
    }
}
