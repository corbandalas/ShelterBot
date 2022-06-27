package com.corbandalas.shelterbot.telegram.menu.constructor;

import com.corbandalas.shelterbot.model.City;
import com.corbandalas.shelterbot.repository.CityRepository;
import com.corbandalas.shelterbot.telegram.ShelterBotState;
import com.corbandalas.shelterbot.telegram.menu.ShelterBotMenuConstructorType;
import io.micronaut.context.env.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

import static com.corbandalas.shelterbot.telegram.ShelterBotStateEnum.CITY_CHOOSE;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.BACK;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.CHOOSE_CITY_CAPTION;

@Slf4j
@Singleton
@ShelterBotMenuConstructorType(type = CITY_CHOOSE)
public class CitySelectionMenuConstructor implements ShelterMenuConstructor {


    @Inject
    private CityRepository cityRepository;

    @Inject
    private Environment environment;

    @Override
    public SendMessage menuConstruct(Update update, ShelterBotState shelterBotState) {

        String defaultCountryName = environment.getProperty("shelterbot.db.defaultCountry", String.class).orElse("ДНР");
        String defaultRegionName = environment.getProperty("shelterbot.db.defaultRegion", String.class).orElse("Донбасс");

        Set<City> cities = cityRepository.getCitiesByRegion(defaultRegionName, defaultCountryName).orElse(new HashSet<>());

        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);


        int rowSize = 1;
        log.info("cities.size()  = " + cities.size() );
        if (cities.size() > 0) {
            rowSize = cities.size() / MAX_BUTTON_ENTRIES_PER_ROW + 1;
        }

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>(rowSize);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboard.add(keyboardRow);

        for (City city : cities) {
            if (keyboardRow.size() >= MAX_BUTTON_ENTRIES_PER_ROW) {
                keyboardRow = new KeyboardRow();
                keyboard.add(keyboardRow);
            }
            keyboardRow.add(new KeyboardButton(city.getName()));
        }


        KeyboardRow backRow = new KeyboardRow();
        backRow.add(new KeyboardButton(BACK));
        keyboard.add(backRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return SendMessage.builder()
                        .chatId("" + update.getMessage().getChatId())
                        .text(update.getMessage().getFrom().getFirstName() + CHOOSE_CITY_CAPTION)
                        .replyMarkup(replyKeyboardMarkup).
                build();
    }
}
