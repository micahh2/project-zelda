package projectzelda.engine;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;


public class Sound {

    private String fileName;
    private Clip clip;
    private InputStream inputStream;
    private AudioInputStream audioInputStream;
    private long currentFrame;

    //constructor to initialize streams and clip
    public Sound(String fileName) {

        this.fileName = fileName;
        inputStream = getClass().getResourceAsStream(fileName);


        // the stream holding the file content
        try {
            if (inputStream == null) {
                throw new IllegalArgumentException("file not found! " + fileName);
            } else {

                audioInputStream = AudioSystem.getAudioInputStream(inputStream);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);

            }
        } catch (Exception e) {
            System.out.println("Exception with Sound");
            e.printStackTrace();
        }


    }

    public void playBackgroundMusic()  {
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void playSound()  {
        clip.start();
    }

    // volume in decibels
    public void setVolume (float volume) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);

    }

    public void stopBackgroundMusic () {

        currentFrame = clip.getMicrosecondPosition();
        clip.stop();
        clip.close();
    }

    public void pauseBackgroundMusic () {

        this.currentFrame = this.clip.getMicrosecondPosition();
        clip.stop();

    }



}
