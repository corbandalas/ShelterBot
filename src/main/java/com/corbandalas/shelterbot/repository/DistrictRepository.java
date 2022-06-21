package com.corbandalas.shelterbot.repository;

import com.corbandalas.shelterbot.model.City;
import com.corbandalas.shelterbot.model.District;
import io.micronaut.core.annotation.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;

public interface DistrictRepository {

    Optional<Set<District>> getDistrictsByCity(@NonNull @Valid City city);

    Optional<Set<District>> getDistrictsByCity(@NonNull @NotBlank String cityName, @NonNull @NotBlank String regionName, @NonNull @NotBlank String countryName);

    Optional<District> getDistrict(@NonNull @NotBlank String districtName, @NonNull @Valid City city);


}
