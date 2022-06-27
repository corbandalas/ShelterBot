package com.corbandalas.shelterbot.telegram.menu.constructor;

import com.corbandalas.shelterbot.model.Shelter;
import com.corbandalas.shelterbot.repository.DistrictRepository;
import com.corbandalas.shelterbot.repository.ShelterRepository;
import com.corbandalas.shelterbot.telegram.ShelterBotState;
import com.corbandalas.shelterbot.telegram.menu.ShelterBotMenuConstructorType;
import io.micronaut.context.env.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.corbandalas.shelterbot.telegram.ShelterBotStateEnum.SEARCH_RESULT;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.BACK;

@Slf4j
@Singleton
@ShelterBotMenuConstructorType(type = SEARCH_RESULT)
public class SearchShowResultMenuConstructor implements ShelterMenuConstructor {

    @Inject
    private Environment environment;

    @Inject
    private DistrictRepository districtRepository;

    @Inject
    private ShelterRepository shelterRepository;

    @Override
    public PartialBotApiMethod menuConstruct(Update update, ShelterBotState shelterBotState) {

        String defaultCountryName = environment.getProperty("shelterbot.db.defaultCountry", String.class).orElse("ДНР");
        String defaultRegionName = environment.getProperty("shelterbot.db.defaultRegion", String.class).orElse("Донбасс");

        String cityName = shelterBotState.getData().get("city");
        String districtName = shelterBotState.getData().get("district");

        Set<Shelter> shelters = districtRepository.getDistrictsByCity(cityName, defaultRegionName, defaultCountryName)
                .flatMap(districts -> districts.stream().filter(d -> d.getName().equalsIgnoreCase(districtName)).findFirst())
                .flatMap(district -> shelterRepository.getSheltersByStreet(update.getMessage().getText(), district)).orElse(new HashSet<>());

        StringBuilder text = new StringBuilder(districtName).append("\n\n");

        shelters.stream().forEach(shelter -> text
                .append(printShelterEntry(shelter))
                .append("\n\n"));

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
                .text(text.toString())
                .replyMarkup(replyKeyboardMarkup).build();
    }
}
