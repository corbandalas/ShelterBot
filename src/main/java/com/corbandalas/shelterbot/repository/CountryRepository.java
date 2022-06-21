package com.corbandalas.shelterbot.repository;

import com.corbandalas.shelterbot.model.Country;
import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;

public interface CountryRepository {

    Optional<Set<Country>> getCountries();
    Optional<Country> getCountry(@NonNull @NotBlank String countryName);
    boolean isCountryExist(@NonNull @NotBlank String countryName);
}
