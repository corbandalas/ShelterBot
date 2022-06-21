package com.corbandalas.shelterbot.repository;

import com.corbandalas.shelterbot.model.Country;
import com.corbandalas.shelterbot.model.Region;
import io.micronaut.core.annotation.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;

public interface RegionRepository {

    Optional<Set<Region>> getRegionsByCountry(@NonNull @Valid Country country);
    Optional<Set<Region>> getRegionsByCountry(@NonNull @NotBlank String countryName);
    Optional<Region> getRegion(@NonNull @NotBlank String regionName, @NonNull @Valid Country country);
    Optional<Region> getRegion(@NonNull @NotBlank String regionName, @NonNull @NotBlank String countryName);
}
