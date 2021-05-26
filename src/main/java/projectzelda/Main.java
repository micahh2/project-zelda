package projectzelda;

import projectzelda.engine.*;
import projectzelda.game.*;
import projectzelda.gfx.*;
import projectzelda.map.*;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

// (c) Thorsten Hasbargen

final class Main {
    private World world = null;

    public Main() throws UnsupportedAudioFileException, LineUnavailableException, IOException 
    {
        Map map = new Map("world_map.tmx");
        System.out.println(map);

        Frame frame = new SwingFrame(map);
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

    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException 
    {

        Sound sound = new Sound("/music/Forest_Ventures.wav");
        sound.playBackgroundMusic();
        new Main();
    }
}
