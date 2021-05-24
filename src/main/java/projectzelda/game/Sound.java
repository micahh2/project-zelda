package projectzelda.game;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;


public class Sound {

    private String fileName;
    private AudioInputStream audioInputStream;
    private Clip clip;
    private InputStream inputStream;

    //constructor to initialize streams and clip
    public Sound(String fileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        this.fileName = fileName;
        inputStream = getClass().getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

        }


    }

    public void playBackgroundMusic() {

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-20.0f);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);

    }

    public void playSound() {


    }

}
