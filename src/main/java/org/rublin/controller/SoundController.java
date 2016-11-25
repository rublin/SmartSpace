package org.rublin.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

import javax.sound.sampled.*;
import java.io.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * ???
 *
 * @author Ruslan Sheremet
 * @see
 * @since 1.0
 */
@Controller
public class SoundController {
    private static final Logger LOG = getLogger(SoundController.class);

    public void play(File file) {
        play(file, 0);
    }

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

    }
}
