package com.corbandalas.shelterbot.telegram.menu.constructor;

import com.corbandalas.shelterbot.model.Shelter;
import com.corbandalas.shelterbot.repository.DistrictRepository;
import com.corbandalas.shelterbot.repository.ShelterRepository;
import com.corbandalas.shelterbot.telegram.ShelterBotState;
import com.corbandalas.shelterbot.telegram.menu.ShelterBotMenuConstructorType;
import io.micronaut.context.env.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.corbandalas.shelterbot.telegram.ShelterBotStateEnum.SHOW_SHELTERS_CITY_DISTRICT;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.*;

@Singleton
@ShelterBotMenuConstructorType(type = SHOW_SHELTERS_CITY_DISTRICT)
public class ShowDistrictSheltersMenuConstructor implements ShelterMenuConstructor {

    @Inject
    private Environment environment;

    @Inject
    private DistrictRepository districtRepository;

    @Inject
    private ShelterRepository shelterRepository;

    @Override
    public SendMessage menuConstruct(Update update, ShelterBotState shelterBotState) {


        String defaultCountryName = environment.getProperty("shelterbot.db.defaultCountry", String.class).orElse("ДНР");
        String defaultRegionName = environment.getProperty("shelterbot.db.defaultRegion", String.class).orElse("Донбасс");

        String cityName = shelterBotState.getData().get("city");
        String districtName = shelterBotState.getData().get("district");
        int offset = Integer.parseInt(shelterBotState.getData().get("offset"));

        Set<Shelter> shelters = districtRepository.getDistrictsByCity(cityName, defaultRegionName, defaultCountryName)
                .flatMap(districts -> districts.stream().filter(d -> d.getName().equalsIgnoreCase(districtName)).findFirst())
                .flatMap(district -> shelterRepository.getSheltersByDistrict(district)).orElse(new HashSet<>());

        StringBuilder text = new StringBuilder(districtName).append("\n\n");

        int offsetInc = Integer.parseInt(environment.getProperty("shelterbot.menu.perpage", String.class).orElse("10"));

        shelters.stream().skip(offset).limit(offsetInc).forEach(shelter -> text
                .append(printShelterEntry(shelter))
                .append("\n\n"));

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
        backRow.add(new KeyboardButton(FIND));
        backRow.add(new KeyboardButton(BACK));

        keyboard.add(keyboardFirstRow);
        keyboard.add(backRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId("" + update.getMessage().getChatId())
                .text(text.toString())
                .replyMarkup(replyKeyboardMarkup).build();

    }
}
