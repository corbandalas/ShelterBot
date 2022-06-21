package com.corbandalas.shelterbot.telegram;

import io.micronaut.context.env.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Slf4j
public class ShelterBotClient extends TelegramWebhookBot {

    @Inject
    private Environment environment;

    @Override
    public String getBotUsername() {
        return environment.getProperty("telegram.bot.name", String.class).orElse("default");
    }

    @Override
    public String getBotToken() {
        return environment.getProperty("telegram.bot.token", String.class).orElse("default");
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        return environment.getProperty("telegram.bot.path", String.class).orElse("default");
    }

    public Message sendMessage(SendMessage message) throws TelegramApiException {

        return execute(message);
    }

    public Message sendMessage(SendPhoto message) throws TelegramApiException {

        return execute(message);
    }
}
