package com.corbandalas.shelterbot.telegram;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Introspected
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShelterBotState {
    private ShelterBotStateEnum state;
    private Map<String, String> data;
    private List<String> markers;
}
