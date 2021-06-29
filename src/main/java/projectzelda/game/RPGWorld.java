package projectzelda.game;

import projectzelda.engine.*;
import projectzelda.map.MapObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RPGWorld extends World {

    private double timeSinceLastShot = 0;

    public WeaponState weaponState = WeaponState.NONE;

    private HelpText helpText;


    private double spawnGrenade = 0;
    private projectzelda.map.Map map;

    private double lifeHelpText = 10.0;


    private Sound sound = new Sound("/music/Forest_Ventures.wav");

    QuestState questState;

    private int chatTrack = 1;

    private List<MapObject> npcs;

    ImageRef bonesImage;
    ImageRef monsterImage;
    ImageRef swordImage;
    ImageRef bowImage;

    int posXChatBox;
    int posYChatBox;

    Save lastSave;

    public RPGWorld(projectzelda.map.Map map) {
        this.map = map;
        worldInfo = map; // Implements world dim
        physicsSystem = new RPGPhysicsSystem(this);
        posXChatBox = worldInfo.getPartWidth() / 2 - 300;
        posYChatBox = worldInfo.getPartHeight() - 100;
    }

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
        this.swordImage = sword.imageRef;
        this.bowImage = bow.imageRef;

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

        npcs = new ArrayList<>();

        // set WorldPart position
        worldPartX = 0;
        worldPartY = 0;


        // create houses and trees
        List<MapObject> houses = map.getAllObjects("Housebases");
        for (MapObject house : houses) {
            int width = house.startingBounds.x2 - house.startingBounds.x1;
            int height = house.startingBounds.y2 - house.startingBounds.y1;
            gameObjects.add(new HitBox(house.startingBounds.x1, house.startingBounds.y1, width, height, Const.Type.TREE));
        }

        List<MapObject> walls = map.getAllObjects("Walls");
        for (MapObject wall : walls) {
            int width = wall.startingBounds.x2 - wall.startingBounds.x1;
            int height = wall.startingBounds.y2 - wall.startingBounds.y1;
            gameObjects.add(new HitBox(wall.startingBounds.x1, wall.startingBounds.y1, width, height, Const.Type.TREE));
        }

        List<MapObject> waters = map.getAllObjects("Water");
        for (MapObject water : waters) {
            int width = water.startingBounds.x2 - water.startingBounds.x1;
            int height = water.startingBounds.y2 - water.startingBounds.y1;
            gameObjects.add(new HitBox(water.startingBounds.x1, water.startingBounds.y1, width, height, Const.Type.WATER));
        }

        List<MapObject> lavas = map.getAllObjects("Lava");
        for (MapObject lava : lavas) {
            int width = lava.startingBounds.x2 - lava.startingBounds.x1;
            int height = lava.startingBounds.y2 - lava.startingBounds.y1;
            gameObjects.add(new HitBox(lava.startingBounds.x1, lava.startingBounds.y1, width, height, Const.Type.LAVA));
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
        ImageRef gameOverRef = new ImageRef("/images/game-over.png", 0, 0, 1103, 437);
        ImageRef gameCompleteRef = new ImageRef("/images/game-complete.png", 0, 0, 1103, 437);
        mainMenuObjects.add(new UIImage(worldWidth/2-logoRef.x2/2, (int)(worldHeight*0.1), logoRef));
        deathMenuObjects.add(new UIImage(worldWidth/2-gameOverRef.x2/2, (int)(worldHeight*0.1), gameOverRef));
        completeGameMenuObjects.add(new UIImage(worldWidth/2-gameCompleteRef.x2/2, (int)(worldHeight*0.1), gameCompleteRef));


        // add the death menu buttons
        relY = (int) (0.4 * worldHeight);
        deathMenuObjects.add(new UIButton(relX, relY, buttonWidth, buttonHeight, "Restart"));
        relY = (int) (0.6 * worldHeight);
        deathMenuObjects.add(new UIButton(relX, relY, buttonWidth, buttonHeight, "Quit"));

        // add screen upon completion
        relY = (int) (0.65 * worldHeight);
        completeGameMenuObjects.add(new UIButton(relX, relY, buttonWidth, buttonHeight, "Done."));


        // initialize the background and add it to different screens
        Background background = new Background(0, 0, map.getPartWidth(), map.getHeight(), new Color(1f, 1f, 1f, 0.2f));
        pauseMenuObjects.add(background);
        mainMenuObjects.add(background);
        deathMenuObjects.add(background);
        completeGameMenuObjects.add(background);

        // add the hud elements
        addHudElements();

        questState = QuestState.START;

        // UNCOMMENT TO SKIP TO END
        ((Avatar)avatar).addItem("SWORD", sword);
        ((Avatar)avatar).addItem("BOW", bow);
        ((Avatar)avatar).switchWeapon(WeaponState.BOW);
        questState = QuestState.BOB_COMPLETED;
        //// 

    }

    public void addHudElements() {
        hudObjects.add(((Avatar) avatar).healthBar);

        int itemSlotX = (int) (0.31 * worldInfo.getPartWidth());
        int itemSlotY = (int) (0.01 * worldInfo.getPartHeight());
        hudObjects.add(new ItemSlot(itemSlotX, itemSlotY, (Avatar) avatar, "SWORD", WeaponState.SWORD, "1", swordImage));
        itemSlotX = (int) (0.33 * worldInfo.getPartWidth());
        hudObjects.add(new ItemSlot(itemSlotX, itemSlotY, (Avatar) avatar, "BOW", WeaponState.BOW, "2", bowImage));
    }

    @Override
    public void reset() {
        gameObjects.clear();
        textObjects.clear();
        pauseMenuObjects.clear();
        chatBox = null;
        mainMenuObjects.clear();
        deathMenuObjects.clear();
        hudObjects.clear();
        completeGameMenuObjects.clear();
        gameState = GameState.MAIN_MENU;
        weaponState = WeaponState.NONE;
        init();
    }

    public void respawnAtCheckpoint() {
        System.out.println("Restoring save.");
        gameObjects.clear();
        chatBox = null;
        chatTrack = 1;
        hudObjects.clear();

        gameState = GameState.PLAY;

        gameObjects.addAll(lastSave.gameObjects);
        questState = lastSave.questState;
        weaponState = lastSave.weaponState;
        avatar = lastSave.avatar;
        save(); // Resave because we're running on our last save

        addHudElements();
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
            if (this.gameState == GameState.PAUSE || this.gameState == GameState.COMPLETE) {
                UIButton resumeButton = (UIButton) pauseMenuObjects.get(0);
                UIButton quitButton = (UIButton) pauseMenuObjects.get(1);
                UIButton quitButton2 = (UIButton) completeGameMenuObjects.get(1);

                if (resumeButton.contains(userInput.mouseMovedX, userInput.mouseMovedY)) {
                    gameState = GameState.PLAY;
                }

                if (quitButton.contains(userInput.mouseMovedX, userInput.mouseMovedY) || quitButton2.contains(userInput.mouseMovedX, userInput.mouseMovedY)) {
                    System.exit(0);
                }
            } 
        }


        UIButton playButton = (UIButton) mainMenuObjects.get(0);

        if (userInput.isMousePressed && playButton.contains(userInput.mouseMovedX, userInput.mouseMovedY)) {
            gameState = GameState.PLAY;
        }


        if (this.gameState == GameState.DEATH && userInput.isMousePressed) {
            UIButton restartButton = (UIButton) deathMenuObjects.get(1);
            UIButton quitButton = (UIButton) deathMenuObjects.get(2);

            if (restartButton.contains(userInput.mouseMovedX, userInput.mouseMovedY)) {
                if (lastSave != null) {
                    respawnAtCheckpoint();
                } else {
                    reset();
                }
            }

            if (quitButton.contains(userInput.mouseMovedX, userInput.mouseMovedY)) {
                System.exit(0);
            }
        }


        if (userInput.isKeyEvent) {
            switch (userInput.keyPressed) {
                case ' ':
                    // go through dialog via spacebar
                    if (chatBox != null && ((ChatBoxButton)chatBox).isBlocking) {
                        handleDialog((ChatBoxButton)chatBox);
                    } else {
                        ((Avatar) avatar).fire();
                    }

                    break;
                case 'q':
                    System.exit(0);
                    break;
                case (char) 27:

                    if (this.gameState == GameState.PLAY) {
                        this.gameState = GameState.PAUSE;
                        sound.setVolume(-40.0f);
                        
                    } else if (this.gameState == GameState.PAUSE){
                        //sound.setVolume(-20.0f);
                        this.gameState = GameState.PLAY;
                        sound.setVolume(-20.0f);

                    } 
                    break;
                case 'w':
                case 'a':
                case 's':
                case 'd':
                    // can't close menus with movement keys
                    if (gameState != GameState.PAUSE && gameState != GameState.MAIN_MENU && gameState != GameState.COMPLETE && gameState != GameState.DEATH) {
                        if (chatBox != null) {
                            ChatBoxButton chatBoxButton = (ChatBoxButton)chatBox;
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
                    if (((Avatar) avatar).containsItem("SWORD")) {
                        ((Avatar) avatar).switchWeapon(WeaponState.SWORD);
                    }
                    break;
                case '2':
                    if (((Avatar) avatar).containsItem("BOW")) {
                        ((Avatar) avatar).switchWeapon(WeaponState.BOW);
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

        // clear chatBox
        if (gameState == GameState.PLAY) {
            if (chatBox != null && chatBox instanceof Notification) {
                Notification not = (Notification)chatBox;
                not.life -= diffSeconds;
                if (not.life < 0) { chatBox = null; }
            }

            // delete HelpText after ... seconds
            if (helpText != null) {
                lifeHelpText -= diffSeconds;
                if (lifeHelpText < 0) {
                    textObjects.remove(helpText);
                    helpText = null;
                }
            }
        }

    }

    public void addChatBox(String text, GameObject obj) {
        ChatBoxButton chatBox = new ChatBoxButton(posXChatBox, posYChatBox, 600, 100, text, obj);
        this.chatBox = chatBox;
    }

    public void addNotification(String text) {
        Notification note = new Notification(posXChatBox, posYChatBox, 600, 100, text);
        this.chatBox = note;
    }

    public void handleDialog(ChatBoxButton box) {

        // not ideal
        Const.Type type = box.obj != null
                ? Const.Type.values()[box.obj.type()]
                : box.objID;

        switch (type) {
            case CHEST:
                Chest chestForText = (Chest) box.obj;
                if (chestForText.getChestText().length > chatTrack) {
                    box.setText(chestForText.getChestText()[chatTrack]);
                    chatTrack++;
                } else {
                    chatTrack = 1;
                    chatBox = null;

                }
                break;
            case PUMPKIN:
                Pumpkin pumpkinForText = (Pumpkin) box.obj;
                if (pumpkinForText.getPumpkinText().length > chatTrack) {
                    box.setText(pumpkinForText.getPumpkinText()[chatTrack]);
                    chatTrack++;
                } else {
                    chatTrack = 1;
                    chatBox = null;

                }
                break;
            case ROCK:
                Rock rockForText = (Rock) box.obj;
                if (rockForText.getRockText().length > chatTrack) {
                    box.setText(rockForText.getRockText()[chatTrack]);
                    chatTrack++;
                } else {
                    chatTrack = 1;
                    chatBox = null;
                }
                break;

            case NPC:
            case ANIMAL:
                NPC npc = (NPC) box.obj;
                if (npc.getNpcQuestText(questState).length > chatTrack) {
                    box.setText(npc.getNpcQuestText(questState, chatTrack));
                    chatTrack++;
                } else {
                    if (npc.progressFromTalk(questState)) {
                        npc.makeQuestProgress(questState);
                        nextQuest();
                    }
                    if ((questState == QuestState.BOB_IN_PROGRESS_CAT
                            || questState == QuestState.BOB_IN_PROGRESS_CAT)
                            && type == Const.Type.ANIMAL) {
                        npc.setFollow(avatar);
                    }
                    chatTrack = 1;
                    chatBox = null;
                }
                break;

            case BONES:
            case GOBLIN:
                chatBox = null;
                break;
        }

    }

    public void nextQuest() {
        int ord = questState.ordinal();
        QuestState old = questState;
        questState = QuestState.values()[ord + 1];
        save();
        System.out.println(old + " -> " + questState);
    }

    public void save() {
        System.out.println("Saving...");
        lastSave = new Save(questState, weaponState, gameObjects);
    }
}
