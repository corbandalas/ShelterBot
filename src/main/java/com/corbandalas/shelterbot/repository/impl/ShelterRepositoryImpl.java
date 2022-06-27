package com.corbandalas.shelterbot.repository.impl;

import com.corbandalas.shelterbot.model.District;
import com.corbandalas.shelterbot.model.Shelter;
import com.corbandalas.shelterbot.repository.CountryRepository;
import com.corbandalas.shelterbot.repository.ShelterRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class ShelterRepositoryImpl implements ShelterRepository {

    @Override
    public Optional<Set<Shelter>> getSheltersByDistrict(@NonNull @Valid District district) {
        return Optional.ofNullable(district.getShelters());
    }

    @Override
    public Optional<Set<Shelter>> getSheltersByStreet(@NotBlank @NonNull String streetName, @NonNull @Valid District district) {
        return getSheltersByDistrict(district).map(shelters -> shelters.stream()
                .filter(sh -> StringUtils.containsAnyIgnoreCase(sh.getAddress(), streetName)).collect(Collectors.toSet()));
    }
}
