package com.corbandalas.shelterbot.telegram.menu;

import com.corbandalas.shelterbot.telegram.ShelterBotStateEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ShelterBotMenuConstructorType {

    ShelterBotStateEnum type();

}