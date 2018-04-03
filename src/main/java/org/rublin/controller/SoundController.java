package org.rublin.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

import javax.sound.sampled.*;
import java.io.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Send sound notification is deprecated now. Use {@link org.rublin.service.MediaPlayerService}
 *
 * @author Ruslan Sheremet
 * @see AudioSystem
 * @since 1.0
 */
@Deprecated
@Controller
public class SoundController {
    private static final Logger LOG = getLogger(SoundController.class);

    /**
     * Play wav file {@link File}. Playing time equals length of file
     * @param file wav file
     */
    public void play(File file) {
        play(file, 0);
    }

    /**
     * Play wav file during the time(sec)
     * @param file wav file
     * @param timeout time to play (sec)
     */
    public void play(File file, int timeout) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            long clipLength = clip.getMicrosecondLength() / 1000;
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            Thread.sleep(timeout * 1000);
            if (timeout == 0)
                Thread.sleep(clipLength);
            clip.stop();
        } catch (UnsupportedAudioFileException e) {
            LOG.error("Wrong file {} format: {}",file.getName(), e);
        } catch (IOException e) {
            LOG.error("File {} not found: {}", file.getName(), e);
        } catch (LineUnavailableException e) {
            LOG.error("File {} is in use: {}", file.getName(), e);
        } catch (InterruptedException e) {
            LOG.error("Unexpected error: {}", e);
        }
    }

    public void stop() {
//// TODO: 25.11.2016 make a method to stop sound 
    }
}
