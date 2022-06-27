package com.corbandalas.shelterbot.repository;

import com.corbandalas.shelterbot.model.District;
import com.corbandalas.shelterbot.model.Shelter;
import io.micronaut.core.annotation.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;

public interface ShelterRepository {

    Optional<Set<Shelter>> getSheltersByDistrict(@NonNull @Valid District district);
    Optional<Set<Shelter>> getSheltersByStreet(@NotBlank @NonNull String streetName, @NonNull @Valid District district);

}
