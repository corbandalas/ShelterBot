package com.corbandalas.shelterbot.repository.impl;

import com.corbandalas.shelterbot.model.City;
import com.corbandalas.shelterbot.model.Country;
import com.corbandalas.shelterbot.model.Region;
import com.corbandalas.shelterbot.repository.CityRepository;
import com.corbandalas.shelterbot.repository.RegionRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;

@Singleton
public class CityRepositoryImpl implements CityRepository {

    private RegionRepository regionRepository;

    public CityRepositoryImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public Optional<Set<City>> getCitiesByRegion(@NonNull @Valid Region region) {
        return getCitiesByRegion(region);
    }

    @Override
    public Optional<Set<City>> getCitiesByRegion(@NonNull @NotBlank String regionName, @NonNull @NotBlank String countryName) {
       return regionRepository.getRegionsByCountry(countryName)
               .flatMap(s -> s.parallelStream().filter(r -> r.getName().equalsIgnoreCase(regionName)).findFirst())
               .map(t -> t.getCities());
    }

    @Override
    public Optional<City> getCity(@NonNull @NotBlank String city, @NonNull @Valid Region region) {
        return region.getCities().parallelStream().filter(c -> c.getName().equalsIgnoreCase(city)).findFirst();
    }

    @Override
    public Optional<City> getCity(@NonNull @NotBlank String city, @NonNull @NotBlank String regionName, @NonNull @Valid  Country country) {
        return regionRepository.getRegion(regionName, country).flatMap(r -> r.getCities().stream().filter(c -> c.getName().equalsIgnoreCase(city)).findFirst());
    }

    @Override
    public Optional<City> getCity(@NonNull String city, @NonNull String regionName, @NonNull String country) {
        return regionRepository.getRegion(regionName, country).flatMap(r -> r.getCities().stream().filter(c -> c.getName().equalsIgnoreCase(city)).findFirst());

    }

}
