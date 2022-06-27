package com.corbandalas.shelterbot.telegram.menu;

import com.corbandalas.shelterbot.telegram.ShelterBotState;
import com.corbandalas.shelterbot.telegram.ShelterBotStateEnum;
import com.corbandalas.shelterbot.telegram.menu.constructor.ShelterMenuConstructor;
import com.corbandalas.shelterbot.utils.ShelterBotUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Set;


@Singleton
public class ShelterBotMenuFactory {

    @Inject
    private List<ShelterMenuConstructor> menuConstructorBeansList;


    public PartialBotApiMethod menu(Update update, ShelterBotState shelterBotState) {


        ShelterMenuConstructor shelterMenuConstructor = getMenuConstructorBean(shelterBotState.getState());


        if (shelterMenuConstructor != null)
            return shelterMenuConstructor.menuConstruct(update, shelterBotState);

        throw new IllegalStateException("Wrong telegram bot state");
    }

    private ShelterMenuConstructor getMenuConstructorBean(ShelterBotStateEnum shelterBotState) {

        Set<Class<?>> annotatedClasses = ShelterBotUtils.getAnnotatedClasses("com.corbandalas.shelterbot.telegram.menu.constructor", ShelterBotMenuConstructorType.class);

        for (Class classValue : annotatedClasses) {

            ShelterBotMenuConstructorType annotation = (ShelterBotMenuConstructorType) classValue
                    .getAnnotation(ShelterBotMenuConstructorType.class);

            if (shelterBotState.equals(annotation.type())) {

                return menuConstructorBeansList.stream().filter(t -> t.getClass().getName().equals(classValue.getName()))
                        .findFirst().orElseThrow(() -> new IllegalStateException("Cannot find menu constructor bean"));
            }
        }

        throw new IllegalStateException("Cannot find menu constructor bean");
    }

}
