package com.corbandalas.shelterbot.telegram.menu;

import com.corbandalas.shelterbot.model.Shelter;
import com.corbandalas.shelterbot.telegram.ShelterBotState;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@FunctionalInterface
public interface ShelterMenuConstructor {
    int MAX_BUTTON_ENTRIES_PER_ROW = 3;

    PartialBotApiMethod menuConstruct(Update update, ShelterBotState shelterBotState);

    default  String printShelterEntry(Shelter shelter) {

        StringBuilder builder = new StringBuilder(shelter.getAddress());

        builder.append(" ");

        if (StringUtils.isNotBlank(shelter.getCapacity())) {
            builder.append("(" );
            builder.append(shelter.getCapacity());
            builder.append(" мест) ");
        }

        if (StringUtils.isNotBlank(shelter.getDescription())) {
            builder.append(shelter.getDescription());
            builder.append(" ");
        }

        if (StringUtils.isNotBlank(shelter.getResponsible())) {
            builder.append(shelter.getResponsible());
            builder.append(" ");
        }

        return builder.toString();
    }
}

