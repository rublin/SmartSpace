package org.rublin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.UUID;

/**
 * Text to Speech controller - say your text using Google translate API.
 * For current version (1.0) its open new tab in browser and play sound on it/
 *
 * @author Ruslan Sheremet
 * @see Desktop
 * @since 1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class TextToSpeechService {

    private static final String TTS_SERVICE = "https://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=%s&client=tw-ob";
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:52.0) Gecko/20100101 Firefox/52.0";
    private final MediaPlayerService mediaPlayerService;

    @Value("${tmp.directory}")
    private String tmpDir;

    /**
     * Say text using Google translate API
     *
     * @param text
     * @param language
     */
    public void say(String text, String language) {
        String url = null;
        try {
            String file = tmpDir.concat(generateRandomFilename("mp3"));
            url = String.format(TTS_SERVICE, URLEncoder.encode(text, "UTF-8"), language);
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setRequestProperty("User-Agent", USER_AGENT);
            ReadableByteChannel channel = Channels.newChannel(urlConnection.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            mediaPlayerService.play(file);
        } catch (IOException e) {
            log.error("Failed to say: {}", e);
        }
    }

    private String generateRandomFilename(String end) {
        return UUID.randomUUID().toString().concat(".").concat(end);
    }

}
