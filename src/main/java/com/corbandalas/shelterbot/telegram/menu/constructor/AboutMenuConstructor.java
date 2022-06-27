package com.corbandalas.shelterbot.telegram.menu.constructor;

import com.corbandalas.shelterbot.telegram.ShelterBotState;
import com.corbandalas.shelterbot.telegram.menu.ShelterBotMenuConstructorType;
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

import static com.corbandalas.shelterbot.telegram.ShelterBotStateEnum.ABOUT;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.ABOUT_TEXT;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.BACK;

@Slf4j
@Singleton
@ShelterBotMenuConstructorType(type = ABOUT)
public class AboutMenuConstructor implements ShelterMenuConstructor {


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

        keyboard.add(backRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId("" + update.getMessage().getChatId())
                .text(ABOUT_TEXT)
                .replyMarkup(replyKeyboardMarkup).build();
    }
}
