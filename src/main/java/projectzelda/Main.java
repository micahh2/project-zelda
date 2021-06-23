package projectzelda;

import projectzelda.engine.*;
import projectzelda.game.*;
import projectzelda.gfx.*;
import projectzelda.map.*;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

final class Main {
    private World world = null;

    public Main() throws UnsupportedAudioFileException, LineUnavailableException, IOException 
    {
        SwingScreenInfo screenInfo = new SwingScreenInfo();
        Map map = new Map("/map/world_map.tmx", screenInfo);
        System.out.println(map);

        // Map implements multiple interfaces
        Frame frame = new SwingFrame(map, map); 
        frame.displayOnScreen();

        world = new RPGWorld(map);

        world.setGraphicSystem(frame.getGraphicSystem());
        world.setInputSystem(frame.getInputSystem());

        GameObject.setWorld(world);
        TextObject.setWorld(world);
        frame.getGraphicSystem().setWorld(world);

        world.init();
        world.run();
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException 
    {
        new Main();
    }
}
