
// (c) Thorsten Hasbargen

package projectzelda.engine;

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
    public ArrayList<TextObject> textObjects = new ArrayList<TextObject>();

    public ArrayList<UIObject> pauseMenuObjects = new ArrayList<>();
    public UIObject chatBox;
    public ArrayList<UIObject> mainMenuObjects = new ArrayList<>();
    public ArrayList<UIObject> deathMenuObjects = new ArrayList<>();
    public ArrayList<UIObject> hudObjects = new ArrayList<>();
    public ArrayList<UIObject> completeGameMenuObjects = new ArrayList<>();


    public boolean dumpPerf = false;

    public GameState gameState = GameState.MAIN_MENU;

    protected World() {
    }

    //
    // the main GAME LOOP
    //
    public final void run() {
        long lastTick = System.currentTimeMillis();
        long lastStatDump = lastTick;
        long timeDrawingGO = 0;
        long timeDrawingOther = 0;
        long timeRedrawing = 0;
        long timeMoving = 0;
        long timeInputs = 0;
        long frames = 0;


        userInput = inputSystem.getUserInput();
        while (true) {
            // calculate elapsed time
            //
            long currentTick = System.currentTimeMillis();
            long millisDiff = currentTick - lastTick;
            if (dumpPerf && currentTick - lastStatDump >= 1000) {
                System.out.print("Drawing GOs: " + timeDrawingGO + "ms");
                System.out.print("\tDrawing Other: " + timeDrawingOther + "ms");
                System.out.print("\tRedrawing: " + timeRedrawing + "ms");
                System.out.print("\tMoving: " + timeMoving + "ms");
                System.out.print("\tInputs: " + timeInputs + "ms");
                System.out.println("\tFPS: " + frames);
                System.out.println("\tGO Size: " + gameObjects.size());
                timeDrawingGO = 0;
                timeDrawingOther = 0;
                timeRedrawing = 0;
                timeMoving = 0;
                timeInputs = 0;
                frames = 0;
                lastStatDump = currentTick;
            }

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


            long startUserInput = System.currentTimeMillis();
            // process User Input
            synchronized (userInput) {
                processUserInput(userInput, millisDiff / 1000.0);
                userInput.clear();
            }
            timeInputs += System.currentTimeMillis() - startUserInput;

            // no actions if game is over
            if (gameOver) {
                continue;
            }

            long startMove = System.currentTimeMillis();
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
            timeMoving += System.currentTimeMillis() - startMove;

            long startDrawGO = System.currentTimeMillis();
            // draw all Objects
            graphicSystem.clear(currentTick);
            for (int i = 0; i < gameSize; i++) {
                gameObjects.get(i).draw(graphicSystem, currentTick);
            }
            graphicSystem.drawForeground(currentTick);
            timeDrawingGO += System.currentTimeMillis() - startDrawGO;

            long startDrawOther = System.currentTimeMillis();
            // draw all Chatboxes
            if (chatBox != null) {
                chatBox.draw(graphicSystem, currentTick);
            }

            // draw all TextObjects
            for (int i = 0; i < textObjects.size(); i++) {
                textObjects.get(i).draw(graphicSystem, currentTick);
            }

            if (gameState == GameState.PAUSE) {
                for (int i = 0; i < pauseMenuObjects.size(); i++) {
                    pauseMenuObjects.get(i).draw(graphicSystem, currentTick);
                }
            }


            // draw play button of main_menu
            if (gameState == GameState.MAIN_MENU) {
                for (int i = 0; i < mainMenuObjects.size(); i++) {
                    mainMenuObjects.get(i).draw(graphicSystem, currentTick);
                }
            }
            if (gameState == GameState.COMPLETE) {
                for (int i = 0; i < completeGameMenuObjects.size(); i++) {
                    completeGameMenuObjects.get(i).draw(graphicSystem, currentTick);
                }
            }

            // draw death_menu objects
            if (gameState == GameState.DEATH) {
                for (int i = 0; i < deathMenuObjects.size(); i++) {
                    deathMenuObjects.get(i).draw(graphicSystem, currentTick);
                }
            }

            // draw HUD
            for (int i = 0; i < hudObjects.size(); i++) {
                hudObjects.get(i).draw(graphicSystem, currentTick);
            }

            timeDrawingOther += System.currentTimeMillis() - startDrawOther;

            long startRedraw = System.currentTimeMillis();
            // redraw everything
            graphicSystem.redraw();

            timeRedrawing += System.currentTimeMillis() - startRedraw;

            // create new objects if needed
            createNewObjects(millisDiff / 1000.0);

            frames++;
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


    public abstract void init();

    public abstract void reset();

    public abstract void processUserInput(UserInput input, double diffSec);

    public abstract void createNewObjects(double diffSeconds);

}
