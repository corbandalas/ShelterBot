package com.corbandalas.shelterbot.telegram.menu;

import com.corbandalas.shelterbot.telegram.ShelterBotState;
import jakarta.inject.Singleton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.*;

@Singleton
public class DisplayLocationSheltersMenuConstructor implements ShelterMenuConstructor {

    @Override
    public SendMessage menuConstruct(Update update, ShelterBotState shelterBotState) {

        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        KeyboardButton keyboardButton = new KeyboardButton(ASK_FOR_GPS_CAPTION);
        keyboardButton.setRequestLocation(true);

        keyboardFirstRow.add(keyboardButton);

        keyboardFirstRow.add(new KeyboardButton(BACK));


        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId("" + update.getMessage().getChatId())
                .text(update.getMessage().getFrom().getFirstName() + CHOOSE_CAPTION)
                .replyMarkup(replyKeyboardMarkup).build();
    }


}
