package com.corbandalas.shelterbot.telegram.menu;

import com.corbandalas.shelterbot.telegram.ShelterBotState;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.corbandalas.shelterbot.telegram.ShelterBotStateEnum.*;

@Singleton
public class ShelterBotMenuFactory {

    @Inject
    private MainMenuConstructor mainMenuConstructor;
    @Inject
    private CitySelectionMenuConstructor citySelectionMenuConstructor;
    @Inject
    private DistrictSelectionMenuConstructor districtSelectionMenuConstructor;
    @Inject
    private ShowDistrictSheltersMenuConstructor showDistrictSheltersMenuConstructor;
    @Inject
    private ShowSheltersListByGPSMenuConstructor showSheltersByGPSMenuConstructor;
    @Inject
    private ShowSheltersOnMapMenuConstructor showSheltersOnMapMenuConstructor;
    @Inject
    private AboutMenuConstructor aboutMenuConstructor;
    @Inject
    private HelpMenuConstructor helpMenuConstructor;
    @Inject
    private ShowRulesMenuConstructor showRulesMenuConstructor;
    @Inject
    private SearchEnterTextMenuConstructor searchEnterTextMenuConstructor;
    @Inject
    private SearchShowResultMenuConstructor searchShowResultMenuConstructor;

    public PartialBotApiMethod menu(Update update, ShelterBotState shelterBotState) {

        if (shelterBotState.getState().equals(MAIN_MENU)) {
            return mainMenuConstructor.menuConstruct(update, shelterBotState);
        }

        if (shelterBotState.getState().equals(CITY_CHOOSE)) {
            return citySelectionMenuConstructor.menuConstruct(update, shelterBotState);
        }

        if (shelterBotState.getState().equals(DISTRICT_CHOOSE)) {
            return districtSelectionMenuConstructor.menuConstruct(update, shelterBotState);
        }

        if (shelterBotState.getState().equals(SHOW_SHELTERS_CITY_DISTRICT)) {
            return showDistrictSheltersMenuConstructor.menuConstruct(update, shelterBotState);
        }

        if (shelterBotState.getState().equals(ABOUT)) {
            return aboutMenuConstructor.menuConstruct(update, shelterBotState);
        }
        if (shelterBotState.getState().equals(HELP)) {
            return helpMenuConstructor.menuConstruct(update, shelterBotState);
        }

        if (shelterBotState.getState().equals(SHOW_SHELTERS_BY_GPS)) {
            return showSheltersByGPSMenuConstructor.menuConstruct(update, shelterBotState);
        }

        if (shelterBotState.getState().equals(SHOW_SHELTERS_ON_MAP_BY_GPS)) {
            return showSheltersOnMapMenuConstructor.menuConstruct(update, shelterBotState);
        }

        if (shelterBotState.getState().equals(SHOW_RULE)) {

            return showRulesMenuConstructor.menuConstruct(update, shelterBotState);
        }

        if (shelterBotState.getState().equals(SEARCH_ENTER)) {
            return searchEnterTextMenuConstructor.menuConstruct(update, shelterBotState);
        }

        if (shelterBotState.getState().equals(SEARCH_RESULT)) {
            return searchShowResultMenuConstructor.menuConstruct(update, shelterBotState);
        }

        throw new IllegalStateException("Wrong telegram bot state");
    }
}
