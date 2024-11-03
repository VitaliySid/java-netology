package org.example;

import org.example.nasa.NasaClient;
import org.example.nasa.models.NasaAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static org.telegram.telegrambots.meta.api.objects.EntityType.URL;

public class NasaTelegramBot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final String NASA_API_KEY;
    private static final Logger logger = LoggerFactory.getLogger(NasaTelegramBot.class);

    public NasaTelegramBot(String botName, String botToken, String nasaApiKey) throws TelegramApiException {
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        this.NASA_API_KEY = nasaApiKey;

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        // TODO
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String action = update.getMessage().getText();
            String[] splittedAction = action.split(" ");
            NasaClient nasaClient;
            NasaAnswer nasaAnswer;

            switch (splittedAction[0]) {
                case "/start":
                    sendMessage("Я бот НАСА, я присылаю фото дня", chatId);
                    break;
                case "/help":
                    sendMessage("Введите /image для сегодняшней фотографии.\n" +
                            "Введите /date YYYY-MM-DD для фотографии за конкретную дату", chatId);
                    break;
                case "/image":
                case "/фото":
                    nasaClient = new NasaClient(NASA_API_KEY);
                    nasaAnswer = nasaClient.getPictureDay(null);

                    if (nasaAnswer == null) {
                        sendMessage("Не удалось получить фото", chatId);
                        break;
                    }

                    sendMessage(nasaAnswer.getUrl(), chatId);

                    break;
                case "/date":

                    nasaClient = new NasaClient(NASA_API_KEY);
                    nasaAnswer = nasaClient.getPictureDay(splittedAction[1]);

                    if (nasaAnswer == null) {
                        sendMessage("Не удалось получить фото", chatId);
                        break;
                    }

                    sendMessage(nasaAnswer.getUrl(), chatId);
                    break;
                default:
                    sendMessage("Я не знаю такой команды", chatId);
            }
        }
    }

    private void sendMessage(String text, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        // TODO
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        // TODO
        return BOT_TOKEN;
    }
}