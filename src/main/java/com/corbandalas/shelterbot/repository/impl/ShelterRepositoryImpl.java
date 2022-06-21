package com.corbandalas.shelterbot.repository.impl;

import com.corbandalas.shelterbot.model.District;
import com.corbandalas.shelterbot.model.Shelter;
import com.corbandalas.shelterbot.repository.CountryRepository;
import com.corbandalas.shelterbot.repository.ShelterRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@Singleton
public class ShelterRepositoryImpl implements ShelterRepository {

    @Override
    public Optional<Set<Shelter>> getSheltersByDistrict(@NonNull @Valid District district) {
        return Optional.ofNullable(district.getShelters());
    }
}
