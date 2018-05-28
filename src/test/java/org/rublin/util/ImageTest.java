package org.rublin.util;

import org.junit.Test;
import org.rublin.model.Camera;

import java.io.File;

import static java.lang.String.format;
import static org.junit.Assert.assertNotNull;


public class ImageTest {

    public static final String LOGIN = "imagesaver";
    public static final String PASSWORD = "";

    @Test
    public void hikVisionTest() {
        File image = Image.getImageFromCamera(hikVisionCamera());
        assertNotNull(image);
    }

    @Test
    public void reoLinkTest() {
        File image = Image.getImageFromCamera(reoLinkCamera());
        assertNotNull(image);
    }

    private Camera reoLinkCamera() {
        Camera camera = new Camera();
        camera.setURL(format("http://192.168.0.32/cgi-bin/api.cgi?cmd=Snap&channel=0&user=%s&password=%s", LOGIN, PASSWORD));
        return camera;
    }

    private Camera hikVisionCamera() {
        Camera camera = new Camera();
        camera.setURL("http://192.168.0.31/Streaming/channels/1/picture");
        camera.setLogin(LOGIN);
        camera.setPassword(PASSWORD);
        return camera;
    }
}