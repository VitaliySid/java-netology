package org.example;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.example.nasa.NasaClient;

import java.io.*;
import java.lang.module.Configuration;
import java.nio.file.Path;
import java.util.Properties;

public class Main {
    static Properties configProp = new Properties();

    public static void main(String[] args) throws IOException {
        loadProps1();

        // create folder if not exists
        var imageDir = new java.io.File(configProp.getProperty("nasa.image.dir"));
        imageDir.mkdir();

        var nasaClient = new NasaClient(configProp.getProperty("nasa.api.key"));

        var answer = nasaClient.getPictureDay();
        var image = nasaClient.getImageData(answer);

        var filename = answer.getPictureName();
        FileOutputStream fos = new FileOutputStream(Path.of(configProp.getProperty("nasa.image.dir"),filename).toString());
        image.getEntity().writeTo(fos);
    }

    public static void loadProps1() throws FileNotFoundException {
        InputStream in = new FileInputStream("application.properties");
        try {
            configProp.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
