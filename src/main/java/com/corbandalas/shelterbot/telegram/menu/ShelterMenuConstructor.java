package com.corbandalas.shelterbot.telegram.menu;

import com.corbandalas.shelterbot.telegram.ShelterBotState;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@FunctionalInterface
public interface ShelterMenuConstructor {
    int MAX_BUTTON_ENTRIES_PER_ROW = 3;
    PartialBotApiMethod menuConstruct(Update update, ShelterBotState shelterBotState);
}

