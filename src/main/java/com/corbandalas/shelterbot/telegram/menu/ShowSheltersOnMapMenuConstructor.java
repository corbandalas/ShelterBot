package com.corbandalas.shelterbot.telegram.menu;

import com.corbandalas.shelterbot.staticmap.StaticMapAPIClient;
import com.corbandalas.shelterbot.telegram.ShelterBotState;
import jakarta.inject.Inject;
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
import java.util.ArrayList;
import java.util.List;

import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.BACK;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.NOT_SHELTERS_AROUND;

@Slf4j
@Singleton
public class ShowSheltersOnMapMenuConstructor implements ShelterMenuConstructor {

    @Inject
    private StaticMapAPIClient staticMapAPIClient;


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

        try {
            byte[] mapPicture = staticMapAPIClient
                    .getMapPicture(shelterBotState.getData().get("latitude"), shelterBotState.getData().get("longitude"),
                            shelterBotState.getMarkers().toArray());

            return SendPhoto.builder()
                    .chatId("" + update.getMessage().getChatId())
                    .photo(new InputFile(new ByteArrayInputStream(mapPicture), "map"))
                    .replyMarkup(replyKeyboardMarkup).build();
        } catch (Exception e) {
            log.error("Error", e);
        }

        return SendMessage.builder()
                .chatId("" + update.getMessage().getChatId())
                .text(NOT_SHELTERS_AROUND)
                .replyMarkup(replyKeyboardMarkup).build();
    }
}
