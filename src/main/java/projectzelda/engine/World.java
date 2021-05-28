
// (c) Thorsten Hasbargen

package projectzelda.engine;

import projectzelda.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

public abstract class World {
    private GraphicSystem graphicSystem;
    public PhysicsSystem physicsSystem;
    private InputSystem inputSystem;
    private UserInput userInput;

    // top left corner of the displayed pane of the world
    public double worldPartX = 0;
    public double worldPartY = 0;

    // defines maximum frame rate
    private static final int FRAME_MINIMUM_MILLIS = 10;

    // if game is over
    public boolean gameOver = false;

    // all objects in the game, including the Avatar
    public GameObjectList gameObjects = new GameObjectList();
    public GameObject avatar;
    public ArrayList<TextObject> textObjects = new ArrayList<TextObject>();

    public ArrayList<UIObject> pauseMenuObjects = new ArrayList<>();

    public Sound sound = new Sound("/music/Forest_Ventures.wav");

    public GameState gameState = GameState.PLAY;

    protected World() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
    }

    //public World(PhysicsSystem ps)
    //{ 
    //    physicsSystem = ps;//new PhysicsSystem(this);
    //}


    //
    // the main GAME LOOP
    //
    public final void run() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        long lastTick = System.currentTimeMillis();

        sound.playBackgroundMusic();
        sound.setVolume(-20.0f);

        userInput = inputSystem.getUserInput();
        while (true) {
            // calculate elapsed time
            //
            long currentTick = System.currentTimeMillis();
            long millisDiff = currentTick - lastTick;

            // don't run faster then MINIMUM_DIFF_SECONDS per frame
            //
            if (millisDiff < FRAME_MINIMUM_MILLIS) {
                try {
                    Thread.sleep(FRAME_MINIMUM_MILLIS - millisDiff);
                } catch (Exception ex) {
                }
                currentTick = System.currentTimeMillis();
                millisDiff = currentTick - lastTick;
            }

            lastTick = currentTick;


            // process User Input
            synchronized (userInput) {
                processUserInput(userInput, millisDiff / 1000.0);
                userInput.clear();
            }

            // no actions if game is over
            if (gameOver) {
                continue;
            }

            int gameSize = gameObjects.size();
            if (gameState == GameState.PLAY) {
                // move all Objects, maybe collide them etc...
                for (int i = 0; i < gameSize; i++) {
                    GameObject obj = gameObjects.get(i);
                    if (obj.isLiving) {
                        obj.move(millisDiff / 1000.0);
                    }
                }


                // delete all Objects which are not living anymore
                int num = 0;
                while (num < gameSize) {
                    if (gameObjects.get(num).isLiving == false) {
                        gameObjects.remove(num);
                        gameSize--;
                    } else {
                        num++;
                    }
                }


                // adjust displayed pane of the world
                this.adjustWorldPart();
            }

            // draw all Objects
            graphicSystem.clear();
            for (int i = 0; i < gameSize; i++) {
                graphicSystem.draw(gameObjects.get(i));
            }


            // draw all TextObjects
            for (int i = 0; i < textObjects.size(); i++) {
                graphicSystem.draw(textObjects.get(i));
            }

            if (gameState == GameState.PAUSE) {
                for (int i = 0; i < pauseMenuObjects.size(); i++) {
                    graphicSystem.draw(pauseMenuObjects.get(i));
                }
                sound.setVolume(-40.0f);
            } else {
                sound.setVolume(-20.0f);
            }

            // redraw everything
            graphicSystem.redraw();

            // create new objects if needed
            createNewObjects(millisDiff / 1000.0);
        }
    }


    // adjust the displayed pane of the world according to Avatar and Bounds
    //
    private final void adjustWorldPart() {
        final int RIGHT_END = Const.WORLD_WIDTH - Const.WORLDPART_WIDTH;
        final int BOTTOM_END = Const.WORLD_HEIGHT - Const.WORLDPART_HEIGHT;


        // if avatar is too much right in display ...
        if (avatar.x > worldPartX + Const.WORLDPART_WIDTH - Const.SCROLL_BOUNDS) {
            // ... adjust display
            worldPartX = avatar.x + Const.SCROLL_BOUNDS - Const.WORLDPART_WIDTH;
            if (worldPartX >= RIGHT_END) {
                worldPartX = RIGHT_END;
            }
        }

        // same left
        else if (avatar.x < worldPartX + Const.SCROLL_BOUNDS) {
            worldPartX = avatar.x - Const.SCROLL_BOUNDS;
            if (worldPartX <= 0) {
                worldPartX = 0;
            }
        }

        // same bottom
        if (avatar.y > worldPartY + Const.WORLDPART_HEIGHT - Const.SCROLL_BOUNDS) {
            worldPartY = avatar.y + Const.SCROLL_BOUNDS - Const.WORLDPART_HEIGHT;
            if (worldPartY >= BOTTOM_END) {
                worldPartY = BOTTOM_END;
            }
        }

        // same top
        else if (avatar.y < worldPartY + Const.SCROLL_BOUNDS) {
            worldPartY = avatar.y - Const.SCROLL_BOUNDS;
            if (worldPartY <= 0) {
                worldPartY = 0;
            }
        }

    }


    public void setGraphicSystem(GraphicSystem p) {
        graphicSystem = p;
    }

    public void setInputSystem(InputSystem p) {
        inputSystem = p;
    }

    public PhysicsSystem getPhysicsSystem() {
        return physicsSystem;
    }


    public abstract void init() throws UnsupportedAudioFileException, LineUnavailableException, IOException;

    public abstract void processUserInput(UserInput input, double diffSec) throws UnsupportedAudioFileException, LineUnavailableException, IOException;

    public abstract void createNewObjects(double diffSeconds);

}
