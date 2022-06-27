package com.corbandalas.shelterbot.telegram.menu.constructor;

import com.corbandalas.shelterbot.telegram.ShelterBotState;
import com.corbandalas.shelterbot.telegram.menu.ShelterBotMenuConstructorType;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.corbandalas.shelterbot.telegram.ShelterBotStateEnum.SHOW_RULE;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.*;

@Slf4j
@Singleton
@ShelterBotMenuConstructorType(type = SHOW_RULE)
public class ShowRulesMenuConstructor implements ShelterMenuConstructor {


    @Override
    public PartialBotApiMethod menuConstruct(Update update, ShelterBotState shelterBotState) {


        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton(PREV_PAGE));
        keyboardFirstRow.add(new KeyboardButton(NEXT_PAGE));

        KeyboardRow backRow = new KeyboardRow();
        backRow.add(new KeyboardButton(BACK));

        keyboard.add(keyboardFirstRow);
        keyboard.add(backRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        String rule = shelterBotState.getData().get("rule");

        String fileName = "rule" + rule + ".JPG";

        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("images/" + fileName)) {


            return SendPhoto.builder()
                    .chatId("" + update.getMessage().getChatId())
                    .photo(new InputFile(new ByteArrayInputStream(inputStream.readAllBytes()), "rules"))
                    .replyMarkup(replyKeyboardMarkup).build();

        } catch (Exception e) {
            log.error("Error while accesing image: ", e);
        }


        return SendMessage.builder()
                .chatId("" + update.getMessage().getChatId())
                .text("")
                .replyMarkup(replyKeyboardMarkup).build();
    }
}
