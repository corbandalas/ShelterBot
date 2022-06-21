package com.corbandalas.shelterbot.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;


@Getter
@Builder
@Introspected
@Data
public class Country {

    @NonNull
    @NotBlank
    private String name;
    @NonNull
    Set<Region> regions;

}
