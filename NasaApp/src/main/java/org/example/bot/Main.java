package org.example.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    static Properties configProp = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, TelegramApiException {
        loadProps();

        new NasaTelegramBot(
                configProp.getProperty("nasa.bot.name"),
                configProp.getProperty("nasa.bot.token"),
                configProp.getProperty("nasa.api.key"));
    }


    public static void loadProps() throws FileNotFoundException {
        //InputStream in = new FileInputStream("application.properties");

        try( InputStream in = Main.class.getResourceAsStream("application.properties")) {
            configProp.load(in);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
