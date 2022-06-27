package com.corbandalas.shelterbot.telegram.menu.constructor;

import com.corbandalas.shelterbot.telegram.ShelterBotState;
import com.corbandalas.shelterbot.telegram.menu.ShelterBotMenuConstructorType;
import jakarta.inject.Singleton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.corbandalas.shelterbot.telegram.ShelterBotStateEnum.MAIN_MENU;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.*;

@Singleton
@ShelterBotMenuConstructorType(type = MAIN_MENU)
public class MainMenuConstructor implements ShelterMenuConstructor {

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
        keyboardFirstRow.add(new KeyboardButton(MAIN_MENU_OPTION1));


        KeyboardButton keyboardButton = new KeyboardButton(MAIN_MENU_OPTION2);
        keyboardButton.setRequestLocation(true);
        keyboardFirstRow.add(keyboardButton);

        KeyboardRow keyBoardSecondRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyBoardSecondRow.add(new KeyboardButton(MAIN_MENU_OPTION3));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyBoardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId("" + update.getMessage().getChatId())
                .text(update.getMessage().getFrom().getFirstName() + CHOOSE_CAPTION)
                .replyMarkup(replyKeyboardMarkup).build();
    }


}
