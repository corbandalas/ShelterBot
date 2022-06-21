package com.corbandalas.shelterbot.repository.impl;

import com.corbandalas.shelterbot.model.Country;
import com.corbandalas.shelterbot.model.Region;
import com.corbandalas.shelterbot.repository.CountryRepository;
import com.corbandalas.shelterbot.repository.RegionRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;

@Singleton
public class RegionRepositoryImpl implements RegionRepository {

    @Inject
    private CountryRepository countryRepository;

    @Override
    public Optional<Set<Region>> getRegionsByCountry(@NonNull Country country) {

        return Optional.ofNullable(country.getRegions());
    }

    @Override
    public Optional<Set<Region>> getRegionsByCountry(@NonNull String countryName) {
        return countryRepository.getCountry(countryName).map(c -> c.getRegions());
    }

    @Override
    public Optional<Region> getRegion(@NonNull String regionName, @NonNull @Valid Country country) {
        return getRegionsByCountry(country)
                .flatMap(r -> r.parallelStream().filter(t -> t.getName().equalsIgnoreCase(regionName)).findFirst());
    }

    @Override
    public Optional<Region> getRegion(@NonNull String regionName, @NonNull @NotBlank String countryName) {
        return getRegionsByCountry(countryName)
                .flatMap(r -> r.parallelStream().filter(t -> t.getName().equalsIgnoreCase(regionName)).findFirst());
    }
}
