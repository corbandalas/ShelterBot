package com.corbandalas.shelterbot.telegram.menu;

import com.corbandalas.shelterbot.telegram.ShelterBotClient;
import com.corbandalas.shelterbot.telegram.ShelterBotState;
import com.corbandalas.shelterbot.telegram.ShelterBotStateStorage;
import io.micronaut.context.env.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;

import static com.corbandalas.shelterbot.telegram.ShelterBotStateEnum.*;
import static com.corbandalas.shelterbot.telegram.menu.ShelterBotTexts.*;

@Slf4j
@Singleton
public class ShelterBotMenuProcessor {

    @Inject
    private ShelterBotStateStorage shelterBotStateStorage;

    @Inject
    private ShelterBotClient shelterBotClient;

    @Inject
    private ShelterBotMenuFactory shelterBotMenuFactory;

    @Inject
    private Environment environment;

    public Message process(Update update) throws TelegramApiException {
        Long chatId = update.getMessage().getChatId();

        ShelterBotState shelterBotState = null;

        //Get bot state from S3 storage
        try {
            shelterBotState = shelterBotStateStorage.getBotState(chatId);

        } catch (Exception e) {
            log.error("Telegram bot state retrieval error");
        }

        //Bot storage null - then create new and show main menu
        if (shelterBotState == null) {


            shelterBotState = new ShelterBotState(MAIN_MENU, new HashMap<>(), new ArrayList<>());

            return sendResponse(update, shelterBotState);

        } else {

            //Processing input text depending on state

            //Delete command processing
            if (update.getMessage().hasText() && update.getMessage().getText().contains("menu")) {

                shelterBotStateStorage.deleteBotState(update.getMessage().getChatId());
                shelterBotState.setState(MAIN_MENU);
                return sendResponse(update, shelterBotState, true);
            }

            //About command processing
            if (update.getMessage().hasText() && update.getMessage().getText().contains("about")) {

                shelterBotState.setState(ABOUT);
                return sendResponse(update, shelterBotState, true);
            }

            //Help command processing
            if (update.getMessage().hasText() && update.getMessage().getText().contains("help")) {

                shelterBotState.setState(HELP);
                return sendResponse(update, shelterBotState, true);
            }

            if (shelterBotState.getState().equals(ABOUT) && update.getMessage().getText().contains(BACK)) {
                shelterBotState.setState(MAIN_MENU);
                return sendResponse(update, shelterBotState);
            }

            if (shelterBotState.getState().equals(HELP) && update.getMessage().getText().contains(BACK)) {
                shelterBotState.setState(MAIN_MENU);
                return sendResponse(update, shelterBotState);
            }

            //MAIN MENU entries processing

            //Shelters: choose city
            if (update.getMessage().hasText() && shelterBotState.getState().equals(MAIN_MENU) && update.getMessage().getText().contains(MAIN_MENU_OPTION1)) {
                shelterBotState.setState(CITY_CHOOSE);

                return sendResponse(update, shelterBotState);
            }

            //Shelters: choose district
            if (shelterBotState.getState().equals(CITY_CHOOSE) && update.getMessage().hasText()) {

                if (update.getMessage().getText().contains(BACK)) {
                    shelterBotState.setState(MAIN_MENU);

                } else {
                    shelterBotState.setState(DISTRICT_CHOOSE);
                    shelterBotState.getData().put("city", update.getMessage().getText());
                }
                return sendResponse(update, shelterBotState);
            }

            //Show shelters
            if (shelterBotState.getState().equals(DISTRICT_CHOOSE) && update.getMessage().hasText()) {

                if (update.getMessage().getText().contains(BACK)) {
                    shelterBotState.setState(CITY_CHOOSE);

                } else {
                    shelterBotState.setState(SHOW_SHELTERS_CITY_DISTRICT);
                    shelterBotState.getData().put("district", update.getMessage().getText());
                    shelterBotState.getData().put("offset", "0");
                }
                return sendResponse(update, shelterBotState);
            }

            //Show shelters by district
            if (shelterBotState.getState().equals(SHOW_SHELTERS_CITY_DISTRICT) && update.getMessage().hasText()) {

                if (update.getMessage().getText().contains(BACK)) {
                    shelterBotState.setState(DISTRICT_CHOOSE);

                } else if (update.getMessage().getText().contains(FIND)) {
                    shelterBotState.setState(SEARCH_ENTER);

                } else {

                    int offset = Integer.parseInt(shelterBotState.getData().get("offset"));

                    int offsetInc = Integer.parseInt(environment.getProperty("shelterbot.menu.perpage", String.class).orElse("10"));

                    if (update.getMessage().getText().contains(NEXT_PAGE)) {
                        offset += offsetInc;
                    } else if (update.getMessage().getText().contains(PREV_PAGE)) {
                        if ((offset - offsetInc) >= 0) {
                            offset -= offsetInc;
                        } else {
                            offset = 0;
                        }
                    } else {
                        shelterBotState.setState(MAIN_MENU);
                        return sendResponse(update, shelterBotState);
                    }

                    shelterBotState.setState(SHOW_SHELTERS_CITY_DISTRICT);
                    shelterBotState.getData().put("offset", String.valueOf(offset));

                }
                return sendResponse(update, shelterBotState);
            }

            //Show shelters by GPS
            if (shelterBotState.getState().equals(MAIN_MENU) && update.getMessage().getLocation() != null) {

                shelterBotState.setState(SHOW_SHELTERS_BY_GPS);
                return sendResponse(update, shelterBotState);
            }

            if (shelterBotState.getState().equals(SHOW_SHELTERS_BY_GPS) && update.getMessage().hasText()) {

                if (update.getMessage().getText().contains(BACK)) {
                    shelterBotState.setState(MAIN_MENU);
                } else {
                    shelterBotState.setState(SHOW_SHELTERS_ON_MAP_BY_GPS);
                }
                return sendResponse(update, shelterBotState);
            }

            if (shelterBotState.getState().equals(SHOW_SHELTERS_ON_MAP_BY_GPS) && update.getMessage().hasText()) {

                shelterBotState.setState(MAIN_MENU);
                return sendResponse(update, shelterBotState);
            }

            //Show bombing attack defence rules
            if (update.getMessage().hasText() && shelterBotState.getState().equals(MAIN_MENU) && update.getMessage().getText().contains(MAIN_MENU_OPTION3)) {
                shelterBotState.setState(SHOW_RULE);
                shelterBotState.getData().put("rule", "1");

                return sendResponse(update, shelterBotState);
            }

            if (shelterBotState.getState().equals(SHOW_RULE) && update.getMessage().hasText()) {


                if (update.getMessage().getText().contains(BACK)) {
                    shelterBotState.setState(MAIN_MENU);

                } else {

                    int rulePage = Integer.parseInt(shelterBotState.getData().get("rule"));

                    if (update.getMessage().getText().contains(NEXT_PAGE)) {
                        rulePage += 1;
                        if (rulePage > 6) {
                            rulePage = 1;
                        }
                    } else if (update.getMessage().getText().contains(PREV_PAGE)) {
                        if ((rulePage - 1) >= 0) {
                            rulePage -= 1;
                        } else {
                            rulePage = 6;
                        }
                    } else {
                        shelterBotState.setState(MAIN_MENU);
                        return sendResponse(update, shelterBotState);
                    }

                    shelterBotState.setState(SHOW_RULE);
                    shelterBotState.getData().put("rule", String.valueOf(rulePage));

                }
                return sendResponse(update, shelterBotState);
            }

            //Search result
            if (shelterBotState.getState().equals(SEARCH_ENTER) && update.getMessage().hasText()) {

                if (update.getMessage().getText().contains(BACK)) {
                    shelterBotState.setState(DISTRICT_CHOOSE);

                } else {
                    shelterBotState.setState(SEARCH_RESULT);
                }
                return sendResponse(update, shelterBotState);
            }

            if (shelterBotState.getState().equals(SEARCH_RESULT) && update.getMessage().hasText()) {

                if (update.getMessage().getText().contains(BACK)) {
                    shelterBotState.setState(DISTRICT_CHOOSE);

                }
                return sendResponse(update, shelterBotState);
            }

        }

        shelterBotState.setState(MAIN_MENU);
        return sendResponse(update, shelterBotState);
    }

    private Message sendResponse(Update update, ShelterBotState shelterBotState) throws TelegramApiException {

        return sendResponse(update, shelterBotState, false);
    }

    private Message sendResponse(Update update, ShelterBotState shelterBotState, boolean deleteCase) throws TelegramApiException {

        //Saving bot state
        if (!deleteCase)
            shelterBotStateStorage.saveBotState(update.getMessage().getChatId(), shelterBotState);

        //Create telegram bot response UI
        PartialBotApiMethod sendMessage = shelterBotMenuFactory.menu(update, shelterBotState);

        //Send response back to user
        if (sendMessage instanceof SendPhoto) {
            return shelterBotClient.sendMessage((SendPhoto) sendMessage);
        }


        return shelterBotClient.sendMessage((SendMessage) sendMessage);
    }

}
