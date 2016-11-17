package org.rublin.util;

import org.rublin.model.Camera;
import org.slf4j.Logger;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * ???
 *
 * @author Ruslan Sheremet
 * @see
 * @since 1.0
 */
public class Image {

    private static final Logger LOG = getLogger(Image.class);

    public static File getImageFromCamera(Camera camera) {
        URL url = null;
        URLConnection connection = null;
        String passStr = camera.getLogin() + ":" + camera.getPassword();
        String encoding = new BASE64Encoder().encode(passStr.getBytes());
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm")) + ".jpg";
        try {
            url = new URL(camera.getURL());
            connection = url.openConnection();
            connection.setRequestProperty("Authorization", "Basic "+encoding);
//            connection.connect();
//            "http://sd.keepcalm-o-matic.co.uk/i/glory-to-ukraine-and-fuck-putin.png";
        } catch (Exception e) {
            LOG.error("Error to download image from camera " + camera.getName() + e.getMessage());
        }
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
             InputStream in = new BufferedInputStream(connection.getInputStream())
        ){
            byte[] b = new byte[2048];
            int length;
            while ((length = in.read(b)) != -1) {
                out.write(b, 0, length);
            }
        } catch (Exception e) {
            LOG.error("Error to download image from camera {}. Error is: ", camera.getName(), e.getMessage());
            return new File("D:\\cam_demo.jpg");
        }
        return new File(filename);
    }
}
