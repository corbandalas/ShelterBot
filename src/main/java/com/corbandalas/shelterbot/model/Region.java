package com.corbandalas.shelterbot.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
@Introspected
@Data
public class Region {
    @NonNull
    private UUID id;
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    private Set<City> cities;
}
