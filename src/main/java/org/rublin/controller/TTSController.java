package org.rublin.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Text to Speech controller - say your text using Google translate API.
 * For current version (1.0) its open new tab in browser and play sound on it/
 *
 * @author Ruslan Sheremet
 * @see Desktop
 * @since 1.0
 */
@Controller
public class TTSController {
    private static final Logger LOG = getLogger(TTSController.class);

    private static final String TTS_SERVICE = "https://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=%s&client=tw-ob";

    /**
     * Say text using Google translate API
     * @param text
     * @param language
     */
    public void say(String text, String language) {
        String url = String.format(TTS_SERVICE, text, language);
        if (Desktop.isDesktopSupported()){

            try {
                Desktop.getDesktop().browse(new URI(url));
                LOG.info("Saying {} in {} location was successful", text, language);
            } catch (IOException e) {
                LOG.error("Unexpected error", e);
            } catch (URISyntaxException e) {
                LOG.error("Wrong URL {}", url, e);
            }
        }
    }

}
