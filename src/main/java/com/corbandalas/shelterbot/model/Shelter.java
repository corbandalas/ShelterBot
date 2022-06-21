package com.corbandalas.shelterbot.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Builder
@Introspected
public class Shelter {

    @NonNull
    @NotBlank
    private UUID id;
    @NonNull
    @NotBlank
    private String address;
    @NonNull
    @NotBlank
    private String owner;
    @NonNull
    @NotBlank
    private String responsible;
    @NonNull
    @NotBlank
    private String capacity;
    @NonNull
    @NotBlank
    private String description;

    private String lng;
    private String lat;
}
