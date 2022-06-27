package com.corbandalas.shelterbot.telegram.menu.constructor;

import com.corbandalas.shelterbot.model.District;
import com.corbandalas.shelterbot.repository.DistrictRepository;
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

import static com.corbandalas.shelterbot.telegram.ShelterBotStateEnum.DISTRICT_CHOOSE;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.BACK;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.CHOOSE_DISTRICT_CAPTION;

@Singleton
@ShelterBotMenuConstructorType(type = DISTRICT_CHOOSE)
public class DistrictSelectionMenuConstructor implements ShelterMenuConstructor {

    @Inject
    private Environment environment;

    @Inject
    private DistrictRepository districtRepository;

    @Override
    public SendMessage menuConstruct(Update update, ShelterBotState shelterBotState) {


        String defaultCountryName = environment.getProperty("shelterbot.db.defaultCountry", String.class).orElse("ДНР");
        String defaultRegionName = environment.getProperty("shelterbot.db.defaultRegion", String.class).orElse("Донбасс");

        String city = shelterBotState.getData().get("city");

        Set<District> districtsByCity = districtRepository.getDistrictsByCity(city, defaultRegionName, defaultCountryName).orElse(new HashSet<>());

        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);


        int rowSize = 1;
        if (districtsByCity.size() > 0) {
            rowSize = districtsByCity.size() / MAX_BUTTON_ENTRIES_PER_ROW + 1;
        }

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>(rowSize);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboard.add(keyboardRow);

        for (District district : districtsByCity) {
            if (keyboardRow.size() >= MAX_BUTTON_ENTRIES_PER_ROW) {
                keyboardRow = new KeyboardRow();
                keyboard.add(keyboardRow);
            }
            keyboardRow.add(new KeyboardButton(district.getName()));
        }


        KeyboardRow backRow = new KeyboardRow();
        backRow.add(new KeyboardButton(BACK));
        keyboard.add(backRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId("" + update.getMessage().getChatId())
                .text(update.getMessage().getFrom().getFirstName() + CHOOSE_DISTRICT_CAPTION)
                .replyMarkup(replyKeyboardMarkup).build();

    }
}
