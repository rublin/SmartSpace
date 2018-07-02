package org.rublin.service;

import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.co.caprica.vlcj.component.DirectAudioPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.SourceDataLine;

@Slf4j
@Service
public class MediaPlayerService {

    private static final String FORMAT = "S16N";
    private static final int RATE = 44100;
    private static final int CHANNELS = 2;

    @Value("${sound.volume}")
    private int volume;

    private final JavaSoundDirectAudioPlayerComponent player;

    public MediaPlayerService() {
        try {
            player = new JavaSoundDirectAudioPlayerComponent(FORMAT, RATE, CHANNELS);
        } catch (Exception e) {
            log.error("Failed to create bean: {}", e);
            throw new RuntimeException(e);
        }
    }

    public void play(String mrl) {
        stop();
        log.info("Starting to play {}", mrl);
        try {
            player.start();
            player.getMediaPlayer().setVolume(volume);
            player.getMediaPlayer().playMedia(mrl);
        } catch (Exception e) {
            log.error("Playback error: {}", e);
        }
       log.info("Music plays");
        // audioPlayerComponent.release(true); // FIXME right now this causes a fatal JVM crash just before the JVM terminates, I am not sure why (the other direct audio player example does NOT crash)

    }

    public void stop() {
        log.info("Stop playing");
        player.stop();
    }

    /**
     *
     */
    private class JavaSoundDirectAudioPlayerComponent extends DirectAudioPlayerComponent {

        private static final int BLOCK_SIZE = 4;
        private static final int SAMPLE_BITS = 16; // BLOCK_SIZE * 8 / channels ???

        private final AudioFormat audioFormat;
        private final Info info;
        private final SourceDataLine dataLine;

        public JavaSoundDirectAudioPlayerComponent(String format, int rate, int channels) throws Exception {
            super(format, rate, channels);
            this.audioFormat = new AudioFormat(rate, SAMPLE_BITS, channels, true, false);
            this.info = new Info(SourceDataLine.class, audioFormat);
            this.dataLine = (SourceDataLine) AudioSystem.getLine(info);
        }

        private void start() throws Exception {
            log.info("start()");
            dataLine.open(audioFormat);
            dataLine.start();
        }

        private void stop() {
            log.info("stop()");
            dataLine.close();
        }

        @Override
        public void play(DirectAudioPlayer mediaPlayer, Pointer samples, int sampleCount, long pts) {
            // There may be more efficient ways to do this...
            int bufferSize = sampleCount * BLOCK_SIZE;
            // You could process these samples in some way before playing them...
            byte[] data = samples.getByteArray(0, bufferSize);
            dataLine.write(data, 0, bufferSize);
        }

        @Override
        public void drain(DirectAudioPlayer mediaPlayer) {
            log.info("drain()");
            dataLine.drain();
        }

        @Override
        public void finished(MediaPlayer mediaPlayer) {
            log.info("finished()");
            stop();
        }
    }
}
