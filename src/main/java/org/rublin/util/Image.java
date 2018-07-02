package org.rublin.util;

import org.rublin.model.Camera;
import org.slf4j.Logger;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

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

    public static File getImageFromCamera(Camera camera, String path) {
        URL url = null;
        URLConnection connection = null;
        String encoding = null;
        if (Objects.nonNull(camera.getLogin()) && Objects.nonNull(camera.getPassword())) {
            String passStr = camera.getLogin() + ":" + camera.getPassword();
            encoding = new BASE64Encoder().encode(passStr.getBytes());
        }
        long timeMillis = System.currentTimeMillis();
        String filename = path + timeMillis + ".jpg";
//        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm")) + ".jpg";
        try {
            url = new URL(camera.getURL());
            connection = url.openConnection();
            if (Objects.nonNull(encoding)) {
                connection.setRequestProperty("Authorization", "Basic "+encoding);
            }
        } catch (Exception e) {
            LOG.error("Error to download image from camera " + camera.getName() + e.getMessage());
        }
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
             InputStream in = new BufferedInputStream(connection.getInputStream())){
            byte[] b = new byte[2048];
            int length;
            while ((length = in.read(b)) != -1) {
                out.write(b, 0, length);
            }
        } catch (Exception e) {
            LOG.error("Error to download image from camera {}. Error is: ", camera.getName(), e.getMessage());
            return null;
        }
        LOG.info("New image {} created", filename);
        return new File(filename);
    }
}
