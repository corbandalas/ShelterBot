package com.corbandalas.shelterbot.repository.impl;

import com.corbandalas.shelterbot.model.City;
import com.corbandalas.shelterbot.model.District;
import com.corbandalas.shelterbot.repository.CityRepository;
import com.corbandalas.shelterbot.repository.CountryRepository;
import com.corbandalas.shelterbot.repository.DistrictRepository;
import com.corbandalas.shelterbot.repository.RegionRepository;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;

@Singleton
public class DistrictRepositoryImpl implements DistrictRepository {

    private CityRepository cityRepository;
    private CountryRepository countryRepository;
    private RegionRepository regionRepository;

    public DistrictRepositoryImpl(CityRepository cityRepository, CountryRepository countryRepository, RegionRepository regionRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    public Optional<Set<District>> getDistrictsByCity(@NonNull @Valid City city) {
        return Optional.ofNullable(city.getDistricts());
    }

    @Override
    public Optional<Set<District>> getDistrictsByCity(@NonNull String cityName, @NonNull String regionName, @NonNull String countryName) {
        return cityRepository.getCity(cityName, regionName, countryName).map(c -> c.getDistricts());
    }


    @Override
    public Optional<District> getDistrict(@NonNull @NotBlank  String districtName, @NonNull @NotBlank  City city) {
        return city.getDistricts().parallelStream().filter(d -> d.getName().equalsIgnoreCase(districtName)).findFirst();
    }

}
