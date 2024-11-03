package org.example.bot;

import org.example.nasa.NasaClient;
import org.example.nasa.models.NasaAnswer;
import org.glassfish.jersey.internal.guava.CacheBuilder;
import org.glassfish.jersey.internal.guava.CacheLoader;
import org.glassfish.jersey.internal.guava.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class NasaTelegramBot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final String NASA_API_KEY;
    private static final Logger logger = LoggerFactory.getLogger(NasaTelegramBot.class);
    private final  LoadingCache<String, Integer> loadingCache;

    public NasaTelegramBot(String botName, String botToken, String nasaApiKey) throws TelegramApiException {
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
        this.NASA_API_KEY = nasaApiKey;

        loadingCache = CacheBuilder.newBuilder()
                .maximumSize(1500)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
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

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();

            if(!checkLimit(chatId))
                return;

            String[] splitted = update.getMessage().getText().split(" ");
            var action = splitted[0];
            NasaClient nasaClient;
            NasaAnswer nasaAnswer;

            switch (action) {
                case "/start":
                    sendMessage("Я бот НАСА, я присылаю фото дня", chatId);

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
                    var param = splitted[1];
                    nasaClient = new NasaClient(NASA_API_KEY);
                    nasaAnswer = nasaClient.getPictureDay(param);

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

    private synchronized boolean checkLimit(long chatId) {
        try {
            var count = loadingCache.get(String.valueOf(chatId));
            if (count >= 5) {
                sendMessage("Вы превысили лимит запросов", chatId);
                return false;
            }

            loadingCache.put(String.valueOf(chatId), ++count);
            return true;

        } catch (ExecutionException e) {
            loadingCache.put(String.valueOf(chatId), 1);
            return true;
        }
    }
}