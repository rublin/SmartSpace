package org.rublin.service;

import com.sun.jna.Pointer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.co.caprica.vlcj.component.DirectAudioPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.directaudio.DirectAudioPlayer;

import javax.annotation.PostConstruct;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.SourceDataLine;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaPlayerService {

    private static final String FORMAT = "S16N";
    private static final int RATE = 44100;
    private static final int CHANNELS = 2;

    private JavaSoundDirectAudioPlayerComponent player;

    public void setVolume(int volume, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                log.info("Current volume is {}; trying to set {}", player.getMediaPlayer().getVolume(), volume);
                player.getMediaPlayer().setVolume(volume);
                log.info("Current volume is {}", player.getMediaPlayer().getVolume());
            } catch (InterruptedException e) {
                log.warn("Failed to set volume", e);
            }
        }).start();

    }

    public void play(String mrl, int volume) {
        stop();
        try {
            player.start();
            player.getMediaPlayer().playMedia(mrl);
            log.info("Starting to play {}", mrl);
            setVolume(volume, mrl.startsWith("http") ? 2000 : 500);
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

    @PostConstruct
    private void init() {
        try {
            player = new JavaSoundDirectAudioPlayerComponent(FORMAT, RATE, CHANNELS);
        } catch (Exception e) {
            log.error("Failed to create bean: {}", e);
            throw new RuntimeException(e);
        }
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
