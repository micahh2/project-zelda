package projectzelda;

import projectzelda.engine.*;
import projectzelda.game.*;
import projectzelda.gfx.*;

// (c) Thorsten Hasbargen

final class Main 
{
    private World world = null;

    public Main()
    { 
        Frame frame = new SwingFrame();
        frame.displayOnScreen();

        world = new RPGWorld();

        world.setGraphicSystem(frame.getGraphicSystem());
        world.setInputSystem(frame.getInputSystem());

        GameObject.setWorld(world);
        TextObject.setWorld(world);
        frame.getGraphicSystem().setWorld(world);

        world.init();
        world.run();
    }

    public static void main(String[] args) {
        new Main();
    }
}
