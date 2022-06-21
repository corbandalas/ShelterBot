package com.corbandalas.shelterbot.repository.impl;

import com.corbandalas.shelterbot.model.Storage;
import com.corbandalas.shelterbot.model.Country;
import com.corbandalas.shelterbot.repository.CountryRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import javax.validation.constraints.NotBlank;

import java.util.Optional;
import java.util.Set;

@Singleton
public class CountryRepositoryImpl implements CountryRepository {

    private Storage storage;

    public CountryRepositoryImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Optional<Set<Country>> getCountries() {
        return Optional.ofNullable(storage.root());
    }

    @Override
    public Optional<Country> getCountry(@NonNull @NotBlank  String countryName) {
        return storage.root().parallelStream().filter(t -> t.getName().equalsIgnoreCase(countryName)).findFirst();
    }

    @Override
    public boolean isCountryExist(@NonNull @NotBlank String countryName) {
        return getCountry(countryName).isPresent();
    }


}
