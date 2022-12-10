package io.proj3ct.Obormotius_bot.service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import com.sun.source.doctree.TextTree;
import io.proj3ct.Obormotius_bot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config=config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    sendMsg(message, MessageText.START_TEXT);
                    break;
                case "/help":
                    sendMsg(message, MessageText.HELP_TEXT);
                    break;
                case "/photo":
                    sendMsg(message, MessageText.PHOTO_TEXT);
                    break;
                case "/resultphoto":
                    String caption = "Image Detection Finished";
                    long chat_id = update.getMessage().getChatId();
                    SendPhoto msg = new SendPhoto()
                            .setChatId(chat_id)
                            .setPhoto("2_out.jpg")
                            .setCaption(caption);
                    try {
                        sendPhoto(msg);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                default:
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    }
                    catch (IOException e) {
                        sendMsg(message, MessageText.ERROR_TEXT);
                    }
            }
        }

    private void startCommandReceived(long chatId,String name)  {

        String answer = "Привет, " + name + ", как делишки?";

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend)  {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try{
            execute(message);
        }
        catch (TelegramApiException e){

        }

    }
}
