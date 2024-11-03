package org.example;

import org.example.nasa.NasaClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class Main {
    static Properties configProp = new Properties();

    public static void main(String[] args) throws IOException, TelegramApiException {
        loadProps();

        NasaTelegramBot bot = new NasaTelegramBot(
                configProp.getProperty("nasa.bot.name"),
                configProp.getProperty("nasa.bot.token"),
                configProp.getProperty("nasa.api.key"));
    }


    public static void loadProps() throws FileNotFoundException {
        InputStream in = new FileInputStream("application.properties");
        // load a properties file
        try {
            configProp.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
