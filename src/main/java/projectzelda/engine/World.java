
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

    public WorldInfo worldInfo;

    // defines maximum frame rate
    private static final int FRAME_MINIMUM_MILLIS = 16;

    // if game is over
    public boolean gameOver = false;

    // all objects in the game, including the Avatar
    public GameObjectList gameObjects = new GameObjectList();
    public GameObject avatar;
    public GameObject boss ;
    public GameObject chest ;
    public GameObject pumpkin ;
    public GameObject dog ;
    public GameObject cat ;
    public GameObject steve ;
    public GameObject brutus ;
    public GameObject olga ;
    public GameObject bob ;
    public ArrayList<TextObject> textObjects = new ArrayList<TextObject>();

    public ArrayList<UIObject> pauseMenuObjects = new ArrayList<>();
    public ArrayList<UIObject> chatBoxObjects = new ArrayList<>();
    public ArrayList<UIObject> mainMenuObjects = new ArrayList<>();
    public ArrayList<UIObject> hudObjects = new ArrayList<>();


    //public GameState gameState = GameState.PLAY;
    public GameState gameState = GameState.MAIN_MENU;

    protected World() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
    }

    //
    // the main GAME LOOP
    //
    public final void run() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        long lastTick = System.currentTimeMillis();


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
            graphicSystem.clear(currentTick);
            for (int i = 0; i < gameSize; i++) {
                gameObjects.get(i).draw(graphicSystem);
            }
            graphicSystem.drawForeground(currentTick);

            // draw all Chatboxes

            for (int i = 0; i < chatBoxObjects.size(); i++) {
                    chatBoxObjects.get(i).draw(graphicSystem);

            }

            // draw all TextObjects
            for (int i = 0; i < textObjects.size(); i++) {
                graphicSystem.draw(textObjects.get(i));
            }

            if (gameState == GameState.PAUSE) {
                for (int i = 0; i < pauseMenuObjects.size(); i++) {
                    pauseMenuObjects.get(i).draw(graphicSystem);
                }
            }


            // draw play button of main_menu
            if (gameState == GameState.MAIN_MENU) {
                for (int i = 0; i < mainMenuObjects.size(); i++) {
                    mainMenuObjects.get(i).draw(graphicSystem);
                }
            }

            // draw HUD
            for (int i = 0; i < hudObjects.size(); i++) {
                hudObjects.get(i).draw(graphicSystem);
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
        final int partWidth = worldInfo.getPartWidth();
        final int partHeight = worldInfo.getPartHeight();
        final int scrollBounds = worldInfo.getScrollBounds();

        final int rightEnd = worldInfo.getWidth() - partWidth;
        final int bottomEnd = worldInfo.getHeight() - partHeight;


        // if avatar is too much right in display ...
        if (avatar.x > worldPartX + partWidth - scrollBounds) {
            // ... adjust display
            worldPartX = avatar.x + scrollBounds - partWidth;
            if (worldPartX >= rightEnd) {
                worldPartX = rightEnd;
            }
        }

        // same left
        else if (avatar.x < worldPartX + scrollBounds) {
            worldPartX = avatar.x - worldInfo.getScrollBounds();
            if (worldPartX <= 0) {
                worldPartX = 0;
            }
        }

        // same bottom
        if (avatar.y > worldPartY + partHeight - scrollBounds) {
            worldPartY = avatar.y + scrollBounds - partHeight;
            if (worldPartY >= bottomEnd) {
                worldPartY = bottomEnd;
            }
        }

        // same top
        else if (avatar.y < worldPartY + scrollBounds) {
            worldPartY = avatar.y - scrollBounds;
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
