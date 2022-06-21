package com.corbandalas.shelterbot.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
@Introspected
public class District {

    @NonNull
    @NotBlank
    private UUID id;
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    private Set<Shelter> shelters;

}
