package com.corbandalas.shelterbot.telegram.menu;

import com.corbandalas.shelterbot.telegram.ShelterBotState;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.*;

@Slf4j
@Singleton
public class SearchEnterTextMenuConstructor implements ShelterMenuConstructor {


    @Override
    public PartialBotApiMethod menuConstruct(Update update, ShelterBotState shelterBotState) {

        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow backRow = new KeyboardRow();
        backRow.add(new KeyboardButton(BACK));
        backRow.add(new KeyboardButton(SEARCH_CAPTION));

        keyboard.add(backRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId("" + update.getMessage().getChatId())
                .text(SEARCH_TITLE)
                .replyMarkup(replyKeyboardMarkup).build();
    }
}
