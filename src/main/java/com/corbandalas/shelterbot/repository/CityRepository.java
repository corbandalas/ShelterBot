package com.corbandalas.shelterbot.repository;

import com.corbandalas.shelterbot.model.City;
import com.corbandalas.shelterbot.model.Country;
import com.corbandalas.shelterbot.model.Region;
import io.micronaut.core.annotation.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;

public interface CityRepository {

    Optional<Set<City>> getCitiesByRegion(@NonNull @Valid Region region);
    Optional<Set<City>> getCitiesByRegion(@NonNull @NotBlank String regionName, @NonNull @NotBlank String countryName);
    Optional<City> getCity(@NonNull @NotBlank String city, @NonNull @Valid Region region);
    Optional<City> getCity(@NonNull @NotBlank String city, @NonNull @NotBlank String regionName, @NonNull @Valid Country country);

    Optional<City> getCity(@NonNull @NotBlank String city, @NonNull @NotBlank String regionName, @NonNull @NotBlank String country);

}
