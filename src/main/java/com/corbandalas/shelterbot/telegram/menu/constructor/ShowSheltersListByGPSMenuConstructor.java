package com.corbandalas.shelterbot.telegram.menu.constructor;

import com.corbandalas.shelterbot.geocode.GeoCoder;
import com.corbandalas.shelterbot.geocode.yandex.dto.Component;
import com.corbandalas.shelterbot.geocode.yandex.dto.FeatureMember;
import com.corbandalas.shelterbot.geocode.yandex.dto.YandexGeoCoderResponse;
import com.corbandalas.shelterbot.model.Shelter;
import com.corbandalas.shelterbot.repository.CityRepository;
import com.corbandalas.shelterbot.repository.DistrictRepository;
import com.corbandalas.shelterbot.repository.ShelterRepository;
import com.corbandalas.shelterbot.telegram.ShelterBotState;
import com.corbandalas.shelterbot.telegram.ShelterBotStateStorage;
import com.corbandalas.shelterbot.telegram.menu.ShelterBotMenuConstructorType;
import com.corbandalas.shelterbot.utils.ShelterBotUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.env.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.corbandalas.shelterbot.telegram.ShelterBotStateEnum.SHOW_SHELTERS_BY_GPS;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.*;

@Slf4j
@Singleton
@ShelterBotMenuConstructorType(type = SHOW_SHELTERS_BY_GPS)
public class ShowSheltersListByGPSMenuConstructor implements ShelterMenuConstructor {

    @Inject
    private Environment environment;

    @Inject
    private DistrictRepository districtRepository;

    @Inject
    private ShelterRepository shelterRepository;

    @Inject
    private CityRepository cityRepository;

    @Inject
    private GeoCoder geoCoder;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private ShelterBotStateStorage shelterBotStateStorage;


    @Override
    public PartialBotApiMethod menuConstruct(Update update, ShelterBotState shelterBotState) {


        String defaultCountryName = environment.getProperty("shelterbot.db.defaultCountry", String.class).orElse("ДНР");
        String defaultRegionName = environment.getProperty("shelterbot.db.defaultRegion", String.class).orElse("Донбасс");

        Location location = update.getMessage().getLocation();


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

        String address = "";


        try {

            String response = geoCoder.reverseGeocode(location.getLongitude().toString(), location.getLatitude().toString());

            log.info("Yandex response " + response);

            YandexGeoCoderResponse yandexGeoCoderResponse = objectMapper.readValue(response, YandexGeoCoderResponse.class);

            String districtName = "";
            String cityName = "";

            for (FeatureMember featureMember : yandexGeoCoderResponse.getResponse().getGeoObjectCollection()
                    .getFeatureMember()) {
                for (Component component : featureMember.getGeoObject().getMetaDataProperty().
                        getGeocoderMetaData().getAddress().getComponents()) {
                    if (component.getKind().equalsIgnoreCase("district") && component.getName().contains("район")) {
                        districtName = StringUtils.remove(component.getName(), " район");
                    }

                    if (component.getKind().equalsIgnoreCase("locality")) {
                        cityName = component.getName();
                    }
                }
            }


            log.info("Obtained city: " + cityName);

            log.info("Obtained district: " + districtName);

            Optional<List<Shelter>> sheltersAround;

            double radius = 0.5;

            do {
                sheltersAround = getSheltersAround(defaultCountryName, defaultRegionName, location, cityName, districtName, radius);

                radius += 0.5;

            } while (!sheltersAround.isPresent() || radius < 2.0);

            AtomicInteger index = new AtomicInteger();
            index.set(0);

            address = sheltersAround.map(shelters -> shelters.stream().map(shelter -> index.incrementAndGet() + ". " + printShelterEntry(shelter))
                    .collect(Collectors.joining("\n\n"))).orElse(NOT_SHELTERS_AROUND);

            index.set(0);

            Optional<List<String>> markers = sheltersAround.map(shelters -> shelters.stream().map(shelter -> "color:red|label:" + index.incrementAndGet() + "|" + shelter.getLat() + "," +
                            shelter.getLng())
                    .collect(Collectors.toList()));

            if (markers.isPresent()) {
                List<String> strings = markers.get();
                strings.add(0, "color:blue|" + location.getLatitude() + "," + location.getLongitude());

                shelterBotState.setMarkers(strings);
            }


            shelterBotState.getData().put("latitude", String.valueOf(location.getLatitude()));
            shelterBotState.getData().put("longitude", String.valueOf(location.getLongitude()));

            shelterBotStateStorage.saveBotState(update.getMessage().getChatId(), shelterBotState);

            sheltersAround.map(shelters -> shelters.stream().map(shelter -> shelter.getAddress() + " (" + shelter.getCapacity() + " мест)" + " " + shelter.getDescription())
                    .collect(Collectors.joining("\n\n"))).orElse(NOT_SHELTERS_AROUND);

            if (sheltersAround.isPresent()) {
                backRow.add(new KeyboardButton(SHOW_ON_MAP));
            }


        } catch (Exception e) {
            log.error("Error", e);
            address = NOT_SHELTERS_AROUND;
        }


        return SendMessage.builder()
                .chatId("" + update.getMessage().getChatId())
                .text(address)
                .replyMarkup(replyKeyboardMarkup).build();
    }

    private Optional<List<Shelter>> getSheltersAround(String defaultCountryName, String defaultRegionName, Location location, String cityName, String districtName, Double radius) {
        return cityRepository.getCity(cityName, defaultRegionName, defaultCountryName)
                .flatMap(city -> districtRepository.getDistrict(districtName, city))
                .flatMap(district -> shelterRepository.getSheltersByDistrict(district))
                .map(shelters -> shelters.stream()
                        .filter(sh -> ShelterBotUtils.haversineDistance(location.getLatitude(), location.getLongitude(),
                                Double.parseDouble(sh.getLat()), Double.parseDouble(sh.getLng())) < radius).sorted((sh1, sh2) -> ShelterBotUtils.haversineDistance(location.getLatitude(), location.getLongitude(),
                                Double.parseDouble(sh1.getLat()), Double.parseDouble(sh1.getLng())).compareTo(ShelterBotUtils.haversineDistance(location.getLatitude(), location.getLongitude(),
                                Double.parseDouble(sh2.getLat()), Double.parseDouble(sh2.getLng())))).limit(10)
                        .collect(Collectors.toList()));


    }
}
