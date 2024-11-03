package org.example.cmd;

import org.example.nasa.NasaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class Main {
    static Properties configProp = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(org.example.cmd.Main.class);

    public static void main(String[] args) throws IOException {
        loadProps();

        var nasaClient = new NasaClient(configProp.getProperty("nasa.api.key"));

        var answer = nasaClient.getPictureDay(null);
        var image = nasaClient.getImageData(answer);

        // create folder if not exists
        var imageDir = new File(configProp.getProperty("nasa.image.dir"));
        if(imageDir.mkdir()) {
            var filename = answer.getPictureName();
            FileOutputStream fos = new FileOutputStream(Path.of(configProp.getProperty("nasa.image.dir"),filename).toString());
            image.getEntity().writeTo(fos);
        }

    }

    public static void loadProps() throws FileNotFoundException {
        InputStream in = new FileInputStream("org/example/bot/application.properties");
        try {
            configProp.load(in);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
