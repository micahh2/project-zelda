
// (c) Thorsten Hasbargen

package projectzelda.game;

import projectzelda.*;
import projectzelda.engine.*;

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

    public RPGWorld(projectzelda.map.Map map) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.map = map;
        worldInfo = map; // Implements world dim
        physicsSystem = new RPGPhysicsSystem(this);
    }

    public void init() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        // add the Avatar
        avatar = new Avatar(100, 50);
        gameObjects.add(avatar);

        // set WorldPart position
        worldPartX = 0;
        worldPartY = 0;


        // add a little forrest

        for (int x = 0; x < 5000; x += 1000) {
            for (int y = 0; y < 4000; y += 800) {
                gameObjects.add(new Tree(x + 300, y + 200, 80));
                gameObjects.add(new Tree(x + 600, y + 370, 50));
                gameObjects.add(new Tree(x + 200, y + 600, 50));
                gameObjects.add(new Tree(x + 500, y + 800, 40));
                gameObjects.add(new Tree(x + 900, y + 500, 100));
                gameObjects.add(new Tree(x + 760, y + 160, 40));
            }
        }


        // add some zombies
        for (int i = 0; i < 10; i++) {
            double x = worldPartX + Math.random() * Const.WORLDPART_WIDTH;
            double y = worldPartY + Math.random() * Const.WORLDPART_HEIGHT;
            gameObjects.add(new GoblinAI(x, y, worldInfo));
        }

        counterB = new Counter("Bones: ", 20, 40);
        counterG = new Counter("Grenades: ", 770, 40);
        helpText = new HelpText(100, 400);

        counterG.setNumber(grenades);
        textObjects.add(counterB);
        textObjects.add(counterG);
        textObjects.add(helpText);

        // add the pause menu buttons
        pauseMenuObjects.add(new UIButton(600, 200, 300, 100, "Resume"));
        pauseMenuObjects.add(new UIButton(600, 500, 300, 100, "Quit"));

        sound = new Sound("/music/Forest_Ventures.wav");
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

        //
        // Keyboard events
        //
        if (userInput.isKeyEvent) {
            switch (userInput.keyPressed) {
                case ' ':
                    throwGrenade(userInput.mouseMovedX + worldPartX, userInput.mouseMovedY + worldPartY);
                    Sound sword = new Sound("/music/sword-sound-1_16bit.wav");
                    sword.setVolume(-30.0f);
                    sword.playSound();
                    break;
                case 'q':
                    System.exit(0);
                    break;
                case (char) 27:
                    this.gameState = this.gameState == GameState.PAUSE ? GameState.PLAY : GameState.PAUSE;
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
            double x = worldPartX + Math.random() * Const.WORLDPART_WIDTH;
            double y = worldPartY + Math.random() * Const.WORLDPART_HEIGHT;

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
